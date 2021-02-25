package it.univr.pcn;

import it.univr.util.DoubleUtil;
import it.univr.util.StringUtil;
import java.util.Objects;

import static it.univr.util.DoubleUtil.*;
import static it.univr.util.StringUtil.*;

/**
 * MISSING_COMMENT
 *
 * @author Mauro Gambini, Sara Migliorini
 * @version 0.0.0
 */
public class AuthStatement {

  // === Attributes ============================================================

  private static final String leftDelimiter = "(";
  private static final String rightDelimiter = ")";
  private static final String separator = ",";
  private static final String ownLeftDelimiter = "[";
  private static final String ownRightDelimiter = "]";

  private static int precision = 4;
  private static double threshold = 1 / Math.pow( 10, precision + 1 );

  // === Properties ============================================================

  private Long userId;
  private Double minOwnership;
  private Double maxOwnership;

  // === Methods ===============================================================

  /**
   * MISSING_COMMENT
   */
  public AuthStatement() {
    this.userId = null;
    this.minOwnership = null;
    this.maxOwnership = null;
  }

  /**
   * MISSING_COMMENT
   *
   * @param userId
   * @param minOwnership
   * @param maxOwnership
   */
  public AuthStatement
  ( Long userId,
    Double minOwnership,
    Double maxOwnership ) {

    this.userId = userId;
    this.minOwnership = truncate( minOwnership, precision );
    this.maxOwnership = truncate( maxOwnership, precision );
  }

  // ===========================================================================

  public Long getUserId() {
    return userId;
  }

  public void setUserId( Long userId ) {
    this.userId = userId;
  }

  public Double getMinOwnership() {
    return minOwnership;
  }

  public void setMinOwnership( Double minOwnership ) {
    this.minOwnership = truncate( minOwnership, precision );
  }

  public Double getMaxOwnership() {
    return maxOwnership;
  }

  public void setMaxOwnership( Double maxOwnership ) {
    this.maxOwnership = truncate( maxOwnership, precision );
  }

  // ===========================================================================

  /**
   * MISSING_COMMENT
   *
   * @return
   */
  public String toText() {
    final StringBuilder b = new StringBuilder();
    b.append( leftDelimiter );
    b.append( userId );
    b.append( separator );
    b.append( ownLeftDelimiter );
    b.append( minOwnership );
    b.append( separator );
    b.append( maxOwnership );
    b.append( ownRightDelimiter );
    b.append( rightDelimiter );
    return b.toString();
  }

  /**
   * MISSING_COMMENT
   *
   * @param text
   * @return
   */
  public static AuthStatement fromText( String text ) {
    if( text == null ) {
      throw new NullPointerException();
    }

    final AuthStatement st = new AuthStatement();

    final String s = text
      .replace( leftDelimiter, "" )
      .replace( rightDelimiter, "" );

    final int userIndex = s.indexOf( separator );
    if( userIndex > 0 ) {
      st.setUserId( parseLong( s.substring( 0, userIndex ) ) );
    }

    final String s1 = s.substring( userIndex + 1 )
                       .replace( ownLeftDelimiter, "" )
                       .replace( ownRightDelimiter, "" );
    final int ownIndex = s1.indexOf( separator );
    if( ownIndex > 0 ){
      st.setMinOwnership( parseDouble( s1.substring( 0, ownIndex ) )  );
      st.setMaxOwnership( parseDouble( s1.substring( ownIndex + 1 ) ) );
    }

    return st;
  }

  // ===========================================================================

  @Override
  public boolean equals( Object o ) {
    if( this == o ) return true;
    if( !( o instanceof AuthStatement ) ) return false;
    AuthStatement that = (AuthStatement) o;
    return Objects.equals( userId, that.userId ) &&
           ( minOwnership - that.minOwnership ) < threshold &&
           ( maxOwnership - that.maxOwnership ) < threshold ;
  }

  @Override
  public int hashCode() {
    return Objects.hash( userId, minOwnership, maxOwnership );
  }
}
