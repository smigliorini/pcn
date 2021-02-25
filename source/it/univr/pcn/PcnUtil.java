package it.univr.pcn;

import java.util.Set;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * MISSING_COMMENT
 *
 * @author Mauro Gambini, Sara Migliorini
 * @version 0.0.0
 */
public class PcnUtil {

  private PcnUtil() {
    // nothing here
  }

  // ===========================================================================

  /**
   * MISSING_COMMENT
   *
   * @param c
   * @return
   */

  public static AuthConstraint inversion( AuthConstraint c ) {
    if( c == null ) {
      throw new NullPointerException();
    }

    final AuthConstraint result = new AuthConstraint();

    for( AuthStatement s : c.getStatements() ) {
      final AuthStatement ns = new AuthStatement();
      ns.setUserId( s.getUserId() );
      ns.setMinOwnership( s.getMaxOwnership() * ( -1 ) );
      ns.setMaxOwnership( s.getMinOwnership() * ( -1 ) );
      result.addStatement( ns );
    }

    return result;
  }


  /**
   * MISSING_COMMENT
   *
   * @param c1
   * @param c2
   * @return
   */

  public static AuthConstraint composition
  ( AuthConstraint c1,
    AuthConstraint c2 ) {

    if( c1 == null ) {
      throw new NullPointerException();
    }
    if( c2 == null ) {
      throw new NullPointerException();
    }

    final AuthConstraint result = new AuthConstraint();

    for( AuthStatement s : c1.getStatements() ) {
        final AuthStatement ns = new AuthStatement();
        ns.setUserId( s.getUserId() );
        ns.setMinOwnership( s.getMinOwnership() );
        ns.setMaxOwnership( s.getMaxOwnership() );
        result.addStatement( ns );
    }

    for( AuthStatement s : c2.getStatements() ) {
      boolean found = false;
      // check if an authorship statement for the same author has been already added
      for( AuthStatement ns : result.getStatements() ) {
        if( ns.getUserId().equals( s.getUserId() ) ) {
          ns.setMinOwnership( ns.getMinOwnership() + s.getMinOwnership() );
          ns.setMaxOwnership( ns.getMaxOwnership() + s.getMaxOwnership() );
          found = true;
        }
      }

      if( !found ) {
        final AuthStatement ns = new AuthStatement();
          ns.setUserId( s.getUserId() );
          ns.setMinOwnership( s.getMinOwnership() );
          ns.setMaxOwnership( s.getMaxOwnership() );
          result.addStatement( ns );
      }
    }

    return result;
  }


  /**
   * MISSING_COMMENT
   *
   * @param c1
   * @param c2
   * @param friendSet
   * @param checkFriends
   * @return
   */

  public static AuthConstraint conjunction
  ( AuthConstraint c1,
    AuthConstraint c2,
    Set<Long> friendSet,
    boolean checkFriends ) {

    if( c1 == null ) {
      throw new NullPointerException();
    }
    if( c2 == null ) {
      throw new NullPointerException();
    }
    if( friendSet == null ) {
      throw new NullPointerException();
    }

    final AuthConstraint result = new AuthConstraint();

    for( AuthStatement s : c1.getStatements() ) {
      if( !checkFriends || friendSet.contains( s.getUserId() ) ) {
        final AuthStatement ns = new AuthStatement();
        ns.setUserId( s.getUserId() );
        ns.setMinOwnership( max( 0, s.getMinOwnership() ) );
        ns.setMaxOwnership( max( 0, s.getMaxOwnership() ) );
        if( ns.getMinOwnership() > 0 && ns.getMaxOwnership() > 0 ) {
          result.addStatement( ns );
        }
      }
    }

    for( AuthStatement s : c2.getStatements() ) {
      boolean found = false;
      // check if an authorship statement for the same author has been already added
      for( AuthStatement ns : result.getStatements() ) {
        if( ns.getUserId().equals( s.getUserId() ) ) {
          // todo fast fix***************************************************************
          final double min = max( ns.getMinOwnership(), s.getMinOwnership() );
          final double max = min( ns.getMaxOwnership(), s.getMaxOwnership() );
          if( min <= max ) {
            ns.setMinOwnership( min );
            ns.setMaxOwnership( max );
          } /*else {
            ns.setMinOwnership( 0.0 );
            ns.setMaxOwnership( 0.0 );
          }//*/
          found = true;
        }
      }

      if( !found ) {
        if( !checkFriends || friendSet.contains( s.getUserId() ) ) {
          final AuthStatement ns = new AuthStatement();
          ns.setUserId( s.getUserId() );
          ns.setMinOwnership( max( 0, s.getMinOwnership() ) );
          ns.setMaxOwnership( max( 0, s.getMaxOwnership() ) );
          if( ns.getMinOwnership() > 0 && ns.getMaxOwnership() > 0 ) {
            result.addStatement( ns );
          }
        }
      }
    }

    return result;
  }


  /**
   * MISSING_COMMENT
   *
   * @param c
   * @return
   */

  public static AuthConstraint normalization( AuthConstraint c ) {
    if( c == null ) {
      throw new NullPointerException();
    }

    final AuthConstraint result = new AuthConstraint();

    double maxValue = 0;
    for( AuthStatement s : c.getStatements() ) {
      maxValue += s.getMaxOwnership();
    }

    for( AuthStatement s : c.getStatements() ) {
      if( s.getMinOwnership() != 0 || s.getMaxOwnership() != 0 ) {
      result.addStatement
        ( new AuthStatement
            ( s.getUserId(),
              s.getMinOwnership() / maxValue,
              s.getMaxOwnership() / maxValue ) );
      }
    }

    return result;
  }


  /**
   * MISSING_COMMENT
   *
   * @param cij
   * @param cik
   * @param ckj
   * @param friendSet
   * @param checkFriends
   * @return
   */

  public static AuthConstraint reduction
  ( AuthConstraint cij,
    AuthConstraint cik,
    AuthConstraint ckj,
    Set<Long> friendSet,
    boolean checkFriends ) {

    if( cij == null ) {
      throw new NullPointerException();
    }
    if( cik == null ) {
      throw new NullPointerException();
    }
    if( ckj == null ) {
      throw new NullPointerException();
    }
    if( friendSet == null ) {
      throw new NullPointerException();
    }

    return normalization(
      conjunction( cij, composition( cik, ckj ), friendSet, checkFriends ) //;
                        );
  }
}
