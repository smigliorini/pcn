package it.univr.pcn;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static it.univr.pcn.PcnEdgeType.*;
import static it.univr.pcn.PcnNodeType.*;

/**
 * MISSING_COMMENT
 *
 * @author Mauro Gambini, Sara Migliorini
 * @version 0.0.0
 */
public class Metrics {

  private static int incidenceThreshold = 5;

  // ===========================================================================

  private Metrics(){
    // nothing here
  }

  // ===========================================================================

  /**
   * Returns the average number of authorship statements contained in the
   * network constraints.
   *
   * @param network
   * @return
   */

  public static double averageConstraintSize( Pcn network ){
    if( network == null ) {
      throw new NullPointerException();
    }

    double result = 0.0;
    int counter = 0;

    if( network.getEdgeSet() != null ){
      for( PcnEdge e : network.getEdgeSet() ){
        if( e.getType() == RT_EDGE ){
          result += e.getConstraint().getStatements().size();
          counter += 1;
        }
      }
    }
    return result / counter;
  }

  /**
   * MISSING_COMMENT
   *
   * @param network
   * @return
   */

  public static double sparseNodeIncidence( Pcn network ){
    if( network == null ) {
      throw new NullPointerException();
    }

    double result = 0.0;
    if( network.getEdgeSet() != null ){
      for( PcnEdge e : network.getEdgeSet() ){
        if( e.getType() == RT_EDGE &&
            e.getConstraint().getStatements().size() == 1 ){
          result += 1;
        }
      }
    }

    int numRetweets = 0;
    if( network.getNodeMap() != null ){
      for( PcnNode n : network.getNodeMap().values() ){
        if( n.getType() == RETWEET ){
          numRetweets += 1;
        }
      }
    }

    return result / numRetweets;
  }

  /**
   * MISSING_COMMENT
   *
   * @param network
   * @return
   */

  public static double retweetSourceIncidence( Pcn network ){
    if( network == null ) {
      throw new NullPointerException();
    }

    double result = 0.0;
    int counter = 0;

    if( network.getEdgeSet() != null ){
      for( PcnEdge e : network.getEdgeSet() ){
        if( e.getType() == RT_EDGE &&
            e.getConstraint().getStatements().size() > 1 ){

          final Long tweetUser = e.getTarget().getUserId();
          Double tweetOwn = null;
          Double otherOwn = Double.MIN_VALUE;

          for( AuthStatement s : e.getConstraint().getStatements() ){
            if( s.getUserId().equals( tweetUser )){
              tweetOwn = s.getMinOwnership();
            } else {
              otherOwn = Math.max( otherOwn, s.getMinOwnership() );
            }
          }

          if( otherOwn * incidenceThreshold >= tweetOwn ){
            result += 1;
          }
          counter += 1;
        }
      }
    }

    return result / counter;
  }
}
