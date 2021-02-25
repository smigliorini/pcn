package it.univr.pcn;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static it.univr.pcn.TwitterUserTsvProperties.*;
import static it.univr.util.StringUtil.parseLong;
import static it.univr.util.StringUtil.splitString;

/**
 * MISSING_COMMENT
 *
 * @author Mauro Gambini, Sara Migliorini
 * @version 0.0.0
 */
public class PcnUser {

  // === Attributes ============================================================

  private static final String separator = ";";
  private static final String leftDelimiter = "[";
  private static final String rightDelimiter = "]";

  // === Properties ============================================================

  private Long id;
  private String username;
  private Set<Long> followerSet;
  private Set<Long> friendSet;

  // === Methods ===============================================================

  public PcnUser() {
    this.id = null;
    this.username = null;
    this.followerSet = new HashSet<>();
    this.friendSet = new HashSet<>();
  }

  public PcnUser
    ( Long id,
      String username,
      Set<Long> followerSet,
      Set<Long> friendList ) {

    this.id = id;
    this.username = username;
    this.followerSet = followerSet;
    this.friendSet = friendList;
  }

  public Long getId() {
    return id;
  }

  public void setId( Long id ) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername( String username ) {
    this.username = username;
  }

  public Set<Long> getFollowerSet() {
    return followerSet;
  }

  public void setFollowerSet( Set<Long> followerSet ) {
    this.followerSet = followerSet;
  }

  public Set<Long> getFriendSet() {
    return friendSet;
  }

  public void setFriendSet( Set<Long> friendSet ) {
    this.friendSet = friendSet;
  }

  public void addFollower( Long followerId ) {
    if( this.followerSet == null ) {
      this.followerSet = new HashSet<>();
    }
    this.followerSet.add( followerId );
  }

  public void addFriend( Long friendId ) {
    if( this.friendSet == null ) {
      this.friendSet = new HashSet<>();
    }
    this.friendSet.add( friendId );
  }

  // ===========================================================================

  @Override
  public boolean equals( Object o ) {
    if( this == o ) return true;
    if( !( o instanceof PcnUser ) ) return false;
    PcnUser pcnUser = (PcnUser) o;
    return Objects.equals( id, pcnUser.id );
  }

  @Override
  public int hashCode() {
    return Objects.hash( id );
  }

  // ===========================================================================

  public String toText() {
    final StringBuilder b = new StringBuilder();
    b.append( id );
    b.append( separator );
    b.append( username );
    b.append( separator );
    b.append( leftDelimiter );
    if( followerSet != null ) {
      final Iterator<Long> flIter = followerSet.iterator();
      while( flIter.hasNext() ) {
        b.append( flIter.next() );
        if( flIter.hasNext() ) {
          b.append( "-" );
        }
      }
    }
    b.append( rightDelimiter );
    b.append( separator );
    b.append( leftDelimiter );
    if( friendSet != null ) {
      final Iterator<Long> frIter = friendSet.iterator();
      while( frIter.hasNext() ) {
        b.append( frIter.next() );
        if( frIter.hasNext() ) {
          b.append( "-" );
        }
      }
    }
    b.append( rightDelimiter );
    return b.toString();
  }

  public static PcnUser fromText( String text ) {
    if( text == null ) {
      throw new NullPointerException();
    }
    final PcnUser user = new PcnUser();

    final String[] tokens = text.split( separator );
    user.setId( Long.parseLong( tokens[0] ) );
    user.setUsername( tokens[1] );

    String followers = tokens[2];
    followers = followers.replace( leftDelimiter, "" );
    followers = followers.replace( rightDelimiter, "" );
    if( !followers.isEmpty() ) {
      final String[] fls = followers.split( "-" );
      for( String fl : fls ) {
        user.addFollower( Long.parseLong( fl ) );
      }
    }

    String friends = tokens[3];
    friends = friends.replace( leftDelimiter, "" );
    friends = friends.replace( rightDelimiter, "" );
    if( !friends.isEmpty()) {
      final String[] frs = friends.split( "-" );
      for( String fr : frs ) {
        user.addFriend( Long.parseLong( fr ) );
      }
    }

    return user;
  }

  // ===========================================================================

  /**
   * MISSING_COMMENT
   *
   * @param lines
   * @return
   */
  public static Map<Long, PcnUser> buildUsers( List<String> lines ) {
    if( lines == null ) {
      throw new NullPointerException();
    }

    final Map<Long, PcnUser> result = new HashMap<>();

    for( String l : lines ) {
      final PcnUser user = new PcnUser();

      final String[] tokens = splitString( l, "\t" );

      final Long id = parseLong( tokens[ID.ordinal()] );
      user.setId( id );

      if( result.get( user ) == null ) {
        final String followers = tokens[FOLLOWERS.ordinal()]
          .replace( "[", "" )
          .replace( "]", "" );
        final String[] fl = splitString( followers, "," );
        for( String f : fl ) {
          final Long fId = parseLong( f.trim() );
          if( fId != null ) {
            user.addFollower( fId );
          }
        }

        final String friends = tokens[FRIENDS.ordinal()]
          .replace( "[", "" )
          .replace( "]", "" );
        final String[] fr = splitString( friends, "," );
        for( String f : fr ) {
          final Long fId = parseLong( f.trim() );
          if( fId != null ) {
            user.addFriend( fId );
          }
        }

        result.put( id, user );
      }
    }
    return result;
  }
}
