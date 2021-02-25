package it.univr.pcn;

import it.univr.util.StringUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static it.univr.pcn.Direction.*;
import static it.univr.pcn.PcnEdgeType.*;
import static it.univr.pcn.PcnNodeType.*;
import static it.univr.pcn.TwitterTsvProperties.*;
import static it.univr.util.StringUtil.parseLong;

/**
 * MISSING_COMMENT
 *
 * @author Mauro Gambini, Sara Migliorini
 * @version 0.0.0
 */
public class Pcn {

  // === Properties ============================================================

  private Map<Long, PcnNode> nodeMap;
  // the key is the target node
  private Map<Long, Map<Long, PcnEdge>> edgeMap;

  private List<PcnEdge> ttEdgeList;

  // === Methods ===============================================================

  public Pcn() {
    this.nodeMap = new HashMap<>();
    this.edgeMap = new HashMap<>();
    this.ttEdgeList = new ArrayList<>();
  }

  public Map<Long, PcnNode> getNodeMap() {
    return nodeMap;
  }

  public void setNodeMap( Map<Long, PcnNode> nodeMap ) {
    this.nodeMap = nodeMap;
  }

  public void addNode( PcnNode node ) {
    if( node == null ) {
      throw new NullPointerException();
    }

    if( this.nodeMap == null ) {
      this.nodeMap = new HashMap<>();
    }
    this.nodeMap.put( node.getTweetId(), node );
  }

  public Map<Long, Map<Long, PcnEdge>> getEdgeMap() {
    return edgeMap;
  }

  public void setEdgeMap( Map<Long, Map<Long, PcnEdge>> edgeMap ) {
    this.edgeMap = edgeMap;
  }

  public void addEdge( PcnEdge edge ) {
    if( edge == null ) {
      throw new NullPointerException();
    }

    if( this.edgeMap == null ) {
      edgeMap = new HashMap<>();
    }
    final Long targetId = edge.getTarget().getTweetId();
    final Long sourceId = edge.getSource().getTweetId();

    Map<Long, PcnEdge> value = edgeMap.get( targetId );
    if( value == null ) {
      value = new HashMap<>();
    }
    value.put( sourceId, edge );
    edgeMap.put( targetId, value );

    if( edge.getType() == TT_EDGE ) {
      ttEdgeList.add( edge );
    }
  }

  public Set<PcnEdge> getEdgeSet() {
    final Set<PcnEdge> result = new HashSet<>();

    if( edgeMap != null && !edgeMap.isEmpty() ) {
      for( Map<Long, PcnEdge> eSet : edgeMap.values() ) {
        result.addAll( eSet.values() );
      }
    }

    return result;
  }

  // ===========================================================================

  /**
   * The method builds a PCN network starting from a list of strings
   * representing the content of the input file. TODO: specify better the file
   * format
   *
   * @param lines
   * @param userMap
   * @param checkFriends
   * @param checkFriends
   */

  public void buildPcn
  ( List<String> lines,
    Map<Long, PcnUser> userMap,
    boolean checkFriends ) {

    if( lines == null ) {
      throw new NullPointerException();
    }
    if( userMap == null ) {
      throw new NullPointerException();
    }

    this.nodeMap.clear();
    this.edgeMap.clear();


    final Map<Long, Set<Long>> retweetIdMap = new HashMap<>();
    final Map<Long, Set<PcnNode>> userNodeMap = new HashMap<>();

    // -- Step 1: build nodes --------------------------------------------------
    final Map<Long, PcnNode> tempNodeMap = new HashMap<>();

    for( String l : lines ) {
      final PcnNode node = buildNode( l );

      // todo: fast fix
      if( node == null ) {
        continue;
      }

      // todo: fast fix
      if( node.getType() == QUOTE ) {
        // System.out.printf( "[WARN]: Tweet with IDE: %d is a quote.%n", node.getTweetId() );
        continue;
      }

      if( tempNodeMap.get( node.getTweetId() ) != null ) {
        System.out.printf
          ( "[WARN]: Tweet with ID: %d already found.%n",
            node.getTweetId() );
        continue;
      }

      tempNodeMap.put( node.getTweetId(), node );

      if( node.getType() == RETWEET ) {
        // update the retweetIdMap
        Set<Long> retweetIdSet = retweetIdMap.get( node.getSourceTweetId() );
        if( retweetIdSet == null ) {
          retweetIdSet = new HashSet<>();
        }
        retweetIdSet.add( node.getTweetId() );
        retweetIdMap.put( node.getSourceTweetId(), retweetIdSet );
      }

      // update the list of nodes for the current user
      Set<PcnNode> userNodeSet = userNodeMap.get( node.getUserId() );
      if( userNodeSet == null ) {
        userNodeSet = new HashSet<>();
      }
      userNodeSet.add( node );
      userNodeMap.put( node.getUserId(), userNodeSet );//*/
    }

    // -- debug ----------------------------------------------------------------

    int count = 0;
    for( Set<Long> v : retweetIdMap.values() ) {
      count += v.size();
    }

    System.out.printf( "[%d TWEETS and %d RETWEETS].%n",
                       retweetIdMap.keySet().size(),
                       count );

    // -------------------------------------------------------------------------

    // maintain only the elements for which we have build  node!!

    final Map<PcnNode, Set<PcnNode>> retweetNodeMap = new HashMap<>();

    for( Map.Entry<Long, Set<Long>> e : retweetIdMap.entrySet() ) {
      final PcnNode key = tempNodeMap.get( e.getKey() );

      if( key != null ) {
        if( key.getType() != TWEET ) {
          System.out.printf( "[WARN]: It must be a tweet: %d.%n", key.getTweetId() );
        }

        final Set<PcnNode> value = new HashSet<>();
        for( Long v : e.getValue() ) {
          final PcnNode n = tempNodeMap.get( v );
          if( n != null ) {
            value.add( n );

            if( n.getType() != RETWEET ) {
              System.out.printf( "[WARN]: It must be a retweet: %d.%n", n.getTweetId() );
            }
          }
        }
        retweetNodeMap.put( key, value );
      }
    }//*/

    // -- debug ----------------------------------------------------------------
    count = 0;
    for( Set<PcnNode> v : retweetNodeMap.values() ) {
      count += v.size();
    }

    System.out.printf( "[%d TWEETS and %d RETWEETS].%n",
                       retweetNodeMap.keySet().size(),
                       count );

    // -------------------------------------------------------------------------

    int tweetCount = 0;
    int retweetCount = 0;

    // add the referenced nodes to the network
    for( Map.Entry<PcnNode, Set<PcnNode>> e : retweetNodeMap.entrySet() ) {
      if( e.getValue() != null && !e.getValue().isEmpty() ) {
        // tweet with at least one retweet
        if( nodeMap.containsKey( e.getKey().getTweetId() ) ) {
          System.out.printf( "[WARN]: duplicated tweet: %d.%n", e.getKey().getTweetId() );
        } else {
          addNode( e.getKey() );
          tweetCount += 1;
        }

        for( PcnNode n : e.getValue() ) {
          if( !nodeMap.containsKey( n.getTweetId() ) ) {
            addNode( n );
            retweetCount += 1;
          }
        }
      }
    }//*/

    // -- debug ----------------------------------------------------------------

    System.out.printf
      ( "Step 1 completed: %d nodes added [%d TWEETS and %d RETWEETS].%n",
        this.nodeMap.entrySet().size(),
        tweetCount, retweetCount );//*/

    // -- Step 2: build R-T edges ----------------------------------------------

    for( Map.Entry<PcnNode, Set<PcnNode>> e : retweetNodeMap.entrySet() ) {
      final PcnNode sourceTweet = e.getKey();

      if( e.getValue() != null && !e.getValue().isEmpty() ) {
        for( PcnNode retweet : e.getValue() ) {
          final double minOwn = computeInteractionWeight
            ( retweet, sourceTweet, sourceTweet, e.getValue(), userMap, checkFriends );

          final AuthConstraint c = new AuthConstraint();
          c.addStatement
            ( new AuthStatement
                ( sourceTweet.getUserId(), minOwn, 1.0 ) );

          addEdge( new PcnEdge( retweet, sourceTweet, c, RT_EDGE, DIRECT ) );
        }
      }
    }

    // -- debug ----------------------------------------------------------------

    int rtEdges = 0;
    for( Map.Entry<Long, Map<Long, PcnEdge>> e : edgeMap.entrySet() ) {
      rtEdges += e.getValue().size();
    }
    System.out.printf( "Step 2 completed: %d RT edges added.%n", rtEdges );

    // -- Step 3: build R-R edges ----------------------------------------------

    for( Map.Entry<PcnNode, Set<PcnNode>> e : retweetNodeMap.entrySet() ) {
      final PcnNode originalTweet = e.getKey();

      for( PcnNode rNode : e.getValue() ) {
        final List<PcnNode> rSources =
          candidateRetweetSources( rNode, originalTweet, e.getValue(), userMap, checkFriends );

        for( PcnNode candidateSource : rSources ) {
          if( candidateSource.getType() == RETWEET ) {
            final double minOwn = computeInteractionWeight
              ( rNode, candidateSource, originalTweet, e.getValue(), userMap, checkFriends );
            final AuthConstraint constraint = new AuthConstraint();
            constraint.addStatement
              ( new AuthStatement
                  ( candidateSource.getUserId(), minOwn, 1.0 ) );
            addEdge( new PcnEdge( rNode, candidateSource, constraint, RR_EDGE, DIRECT ) );
          }
        }
      }
    }

    // -- debug ----------------------------------------------------------------

    int rrEdges = 0;
    for( Map.Entry<Long, Map<Long, PcnEdge>> e : edgeMap.entrySet() ) {
      rrEdges += e.getValue().size();
    }
    rrEdges -= rtEdges;
    System.out.printf( "Step 3 completed: %d RR edges added.%n", rrEdges );

    // -- Step 4: build T-T Edges ----------------------------------------------
    for( PcnNode source : this.nodeMap.values() ) {
      final Long user = source.getUserId();
      // find all the user's tweets

      final Set<PcnNode> candidateNodes = userNodeMap.get( user );
      for( PcnNode target : candidateNodes ) {
        if( target.getTimestamp() < source.getTimestamp() ) {
          final AuthStatement statement =
            new AuthStatement( user, 1.0, 1.0 );
          final AuthConstraint constraint = new AuthConstraint();
          constraint.addStatement( statement );
          final PcnEdge e = new PcnEdge( source, target, constraint, TT_EDGE, DIRECT );
          addEdge( e );
        }
      }
    }

    int ttEdges = 0;
    for( Map.Entry<Long, Map<Long, PcnEdge>> e : edgeMap.entrySet() ) {
      ttEdges += e.getValue().size();
    }
    ttEdges -= rrEdges;
    System.out.printf( "Step 4 completed: %d TT edges added.%n", ttEdges );
  }


  /**
   * The method returns the list of sub-networks with at least 3 nodes (= 1
   * tweet and 2 retweets) and 3 edges (at least one connection between the two
   * retweets).
   *
   * @return
   */
  public List<Pcn> getSubNetworks() {
    if( nodeMap != null && nodeMap.values() != null ) {
      final List<Pcn> subnetworkList = new ArrayList<>();

      int tweetCount = 0;
      int retweetCount = 0;

      for( PcnNode node : nodeMap.values() ) {
        // build a subnetwork for each tweet
        if( node.getType() == TWEET ) {
          final Pcn subnet = new Pcn();
          subnet.addNode( node );

          tweetCount += 1;

          // retrieve the connected nodes
          final Map<Long, PcnEdge> outEdges = edgeMap.get( node.getTweetId() );
          if( outEdges != null ) {
            for( PcnEdge edge : outEdges.values() ) {
              if( edge.getType() == RT_EDGE || edge.getType() == RR_EDGE ) {
                subnet.addNode( edge.getSource() );
                retweetCount += 1;
              }
            }

            int rrEdgeCount = 0;
            // at least one node has been added
            if( subnet.nodeMap.entrySet().size() > 1 ) {
              for( PcnNode n : subnet.nodeMap.values() ) {
                if( edgeMap.get( n.getTweetId() ) != null ) {
                  for( PcnEdge e : edgeMap.get( n.getTweetId() ).values() ) {
                    if( e.getType() == RT_EDGE || e.getType() == RR_EDGE ) {
                      subnet.addEdge( e );

                      if( e.getType() == RR_EDGE ) {
                        rrEdgeCount += 1;
                      }
                    }
                  }
                }
              }
            }//*/

            if( subnet.nodeMap.values().size() > 2 &&
                rrEdgeCount > 1 ) {
              subnetworkList.add( subnet );
            }
          }
        }
      }

      System.out.printf( "# TWEET: %d.%n", tweetCount );
      System.out.printf( "# RETWEET: %d.%n", retweetCount );
      System.out.printf( "# TOTAL: %d.%n", tweetCount + retweetCount );

      return subnetworkList;
    } else {
      return Collections.emptyList();
    }
  }

  /**
   * MISSING_COMMENT
   *
   * @return
   */

  public Map<Pair, Integer> getLoopEnhancements() {
    final Map<Pair, Integer> loops = new HashMap<>();

    if( ttEdgeList != null && ttEdgeList.size() > 1 ) {
      for( int i = 0; i < ttEdgeList.size(); i++ ) {
        final PcnEdge e1 = ttEdgeList.get( i );
        final PcnNode n1 = e1.getSource();
        final PcnNode n2 = e1.getTarget();

        final Map<Long, PcnEdge> en1 = edgeMap.get( n1 );
        final Map<Long, PcnEdge> en2 = edgeMap.get( n2 );

        for( int j = i + 1; j < ttEdgeList.size(); j++ ) {
          final PcnEdge e2 = ttEdgeList.get( j );
          final PcnNode n3 = e2.getSource();
          final PcnNode n4 = e2.getTarget();

          final Map<Long, PcnEdge> en3 = edgeMap.get( n3 );
          final Map<Long, PcnEdge> en4 = edgeMap.get( n4 );

          if( ( ( en3 != null && en3.get( n1 ) != null ) ||
                ( en1 != null && en1.get( n3 ) != null ) ) &&
              ( ( en4 != null && en4.get( n2 ) != null ) ||
                ( en2 != null && en2.get( n4 ) != null ) ) ) {

            final Pair key = new Pair( n1.getUserId(), n3.getUserId() );
            Integer v = loops.get( key );
            if( v == null ) {
              v = 0;
            }
            loops.put( key, v + 1 );
          }
        }
      }
    }

    return loops;
  }

  // ===========================================================================

  /**
   * MISSING_COMMENT
   *
   * @param source
   * @param target
   * @param originalTweet
   * @param retweetSet
   * @param userMap
   * @param checkFriends
   * @return
   */

  private static double computeInteractionWeight
  ( PcnNode source,
    PcnNode target,
    PcnNode originalTweet,
    Set<PcnNode> retweetSet,
    Map<Long, PcnUser> userMap,
    boolean checkFriends ) {

    if( source == null ) {
      throw new NullPointerException();
    }
    if( target == null ) {
      throw new NullPointerException();
    }
    if( originalTweet == null ) {
      throw new NullPointerException();
    }
    if( retweetSet == null ) {
      throw new NullPointerException();
    }
    if( userMap == null ) {
      throw new NullPointerException();
    }


    final double n = source.getTimestamp() - target.getTimestamp();

    double d = 0;
    final List<PcnNode> candidateRetweetSources =
      candidateRetweetSources( source, originalTweet, retweetSet, userMap, checkFriends );

    if( source == null || candidateRetweetSources.isEmpty() ) {
      System.out.printf
        ( "[WARN]: no candidate sources for tweet with ID: %d.%n",
          source.getTweetId() );
      return 0;
    }

    for( PcnNode c : candidateRetweetSources ) {
      d += source.getTimestamp() - c.getTimestamp();
    }

    if( d > 0 ) {
      return n / d;

    } else if( d == 0 && n == 0 ) {
      // fast fix
      return 1;

    } else {
      System.out.printf
        ( "[WARN]: no candidate sources for tweet with ID: %d.%n",
          source.getTweetId() );
      return 0;
    }
  }

  /**
   * MISSING_COMMENT
   *
   * @param retweet
   * @param sourceTweet
   * @param retweetSet
   * @param userMap
   * @param checkFriends
   * @return
   */

  private static List<PcnNode> candidateRetweetSources
  ( PcnNode retweet,
    PcnNode sourceTweet,
    Set<PcnNode> retweetSet,
    Map<Long, PcnUser> userMap,
    boolean checkFriends ) {

    if( retweet == null ) {
      throw new NullPointerException();
    }
    if( sourceTweet == null ) {
      throw new NullPointerException();
    }
    if( retweetSet == null ) {
      throw new NullPointerException();
    }

    final List<PcnNode> result = new ArrayList<>();

    for( PcnNode n : retweetSet ) {
      if( !n.equals( retweet ) ) {
        if( n.getTimestamp() < retweet.getTimestamp() &&
            userMap.get( n.getUserId() ) != null &&
            userMap.get( retweet.getUserId() ) != null &&
            ( !checkFriends ||
              ( userMap.get( n.getUserId() ).getFollowerSet()
                       .contains( retweet.getUserId() ) ||
                userMap.get( retweet.getUserId() ).getFriendSet()
                       .contains( n.getUserId() ) ) ) ) {
          result.add( n );
        }
      }
    }

    // add the original tweet
    result.add( sourceTweet );

    return result;
  }

  // ===========================================================================

  /**
   * MISSING_COMMENT
   *
   * @return
   */
  public String toText() {
    if( getEdgeSet() != null ) {
      final StringBuilder b = new StringBuilder();

      for( PcnEdge e : getEdgeSet() ) {
        b.append( e.toText() );
        b.append( System.lineSeparator() );
      }

      return b.toString();
    } else {
      return "";
    }
  }

  /**
   * MISSING_COMMENT
   *
   * @param lines
   */

  public void fromText( List<String> lines ) {
    if( lines == null ) {
      throw new NullPointerException();
    }

    nodeMap = new HashMap<>();
    edgeMap = new HashMap<>();
    ttEdgeList = new ArrayList<>();

    for( String l : lines ) {
      final PcnEdge edge = PcnEdge.fromText( l );

      PcnNode source = this.nodeMap.get( edge.getSource().getTweetId() );
      if( source == null ) {
        source = edge.getSource();
        this.nodeMap.put( source.getTweetId(), source );
      } else {
        // use the previously stored node
        edge.setSource( source );
      }

      PcnNode target = this.nodeMap.get( edge.getTarget().getTweetId() );
      if( target == null ) {
        target = edge.getTarget();
        this.nodeMap.put( target.getTweetId(), target );
      } else {
        // use the previously stored node
        edge.setTarget( target );
      }

      Map<Long, PcnEdge> m1 = edgeMap.get( target.getTweetId() );
      if( m1 == null ) {
        m1 = new HashMap<>();
      }
      m1.put( edge.getSource().getTweetId(), edge );
      this.edgeMap.put( edge.getTarget().getTweetId(), m1 );

      if( edge.getType() == TT_EDGE ) {
        ttEdgeList.add( edge );
      }
    }
  }

  // ===========================================================================

  /**
   * MISSING_COMMENT
   *
   * @param l
   * @return
   */

  private static PcnNode buildNode( String l ) {
    if( l == null ) {
      throw new NullPointerException();
    }

    final String[] tokens = StringUtil.splitString( l, "\t" );

    // todo: fast fix
    if( tokens.length < 8 ) {
      return null;
    }

    // -- retrieve the node attributes ---------------------------------------

    final Long id = parseLong( tokens[ID.ordinal()] );
    final Long userId = parseLong( tokens[USER_ID.ordinal()] );
    final Long timestamp = parseLong( tokens[TIMESTAMP.ordinal()] );
    final Long sourceTweet = sourceTweet( tokens );
    final PcnNodeType type = getNodeType( tokens );

    return new PcnNode( id, userId, timestamp, sourceTweet, type );
  }

  /**
   * MISSING_COMMENT
   *
   * @param tokens
   * @return
   */

  private static Long sourceTweet( String[] tokens ) {
    if( tokens == null ) {
      throw new NullPointerException();
    }

    final Long sourceTweetR = parseLong( tokens[RETWEETED_STATUS_ID.ordinal()] );
    // final Long sourceTweetQ = parseLong( tokens[QUOTED_STATUS_ID.ordinal()] );
    // return sourceTweetR != null ? sourceTweetR :
    // ( sourceTweetQ != null ? sourceTweetQ : null );
    return sourceTweetR;
  }

  /**
   * MISSING_COMMENT
   *
   * @param tokens
   * @return
   */

  private static PcnNodeType getNodeType( String[] tokens ) {
    final Long sourceTweetR = parseLong( tokens[RETWEETED_STATUS_ID.ordinal()] );
    final Long sourceTweetQ = parseLong( tokens[QUOTED_STATUS_ID.ordinal()] );

    if( sourceTweetR != null ) {
      return RETWEET;
    } else if( sourceTweetQ != null ) {
      return QUOTE;
    } else {
      return TWEET;
    }
  }

  // ===========================================================================

  public class Pair {
    private Long user1;
    private Long user2;

    public Pair( Long user1, Long user2 ) {
      this.user1 = user1;
      this.user2 = user2;
    }

    public Long getUser1() {
      return user1;
    }

    public Long getUser2() {
      return user2;
    }

    @Override
    public boolean equals( Object o ) {
      if( this == o ) return true;
      if( o == null || getClass() != o.getClass() ) return false;
      Pair pair = (Pair) o;
      return Objects.equals( user1, pair.user1 ) && Objects.equals( user2, pair.user2 ) ||
             Objects.equals( user1, pair.user2 ) && Objects.equals( user2, pair.user1 ) ||
             Objects.equals( user2, pair.user1 ) && Objects.equals( user1, pair.user2 );
    }
  }
}
