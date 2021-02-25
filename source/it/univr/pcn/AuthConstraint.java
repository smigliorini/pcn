package it.univr.pcn;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * MISSING_COMMENT
 *
 * @author Mauro Gambini, Sara Migliorini
 * @version 0.0.0
 */
public class AuthConstraint {

  // === Attributes ============================================================

  private static String leftDelimiter = "[";
  private static String rightDelimiter = "]";
  private static String separator = "/";

  // === Properties ============================================================

  private Set<AuthStatement> statements;

  // === Methods ===============================================================

  public AuthConstraint() {
    this.statements = new HashSet<>();
  }

  public AuthConstraint( Set<AuthStatement> statements ) {
    this.statements = statements;
  }

  public AuthConstraint
    ( Long userId,
      Double minOwnership,
      Double maxOwnership ) {
    this.statements = new HashSet<>();
    this.statements.add
      ( new AuthStatement
          ( userId, minOwnership, maxOwnership ) );
  }

  // ===========================================================================

  public Set<AuthStatement> getStatements() {
    return statements;
  }

  public void setStatements( Set<AuthStatement> statements ) {
    this.statements = statements;
  }

  public void addStatement( AuthStatement s ) {
    if( s == null ) {
      throw new NullPointerException();
    }

    if( statements == null ) {
      statements = new HashSet<>();
    }
    statements.add( s );
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
    if( statements != null ) {
      final Iterator<AuthStatement> it = statements.iterator();
      while( it.hasNext() ) {
        final AuthStatement statement = it.next();
        b.append( statement.toText() );
        if( it.hasNext() ) {
          b.append( separator );
        }
      }
    }
    b.append( rightDelimiter );
    return b.toString();
  }

  /**
   * MISSING_COMMENT
   *
   * @param text
   * @return
   */
  public static AuthConstraint fromText( String text ) {
    if( text == null ) {
      throw new NullPointerException();
    }

    final AuthConstraint constraint = new AuthConstraint();

    final String s = text
      .replace( leftDelimiter, "" )
      .replace( rightDelimiter, "" );
    final String[] statements = s.split( separator );
    for( String st : statements ) {
      final AuthStatement ast = AuthStatement.fromText( st );
      constraint.addStatement( ast );
    }

    return constraint;
  }//*/

  // ===========================================================================

  @Override
  public boolean equals( Object o ) {
    if( this == o ) return true;
    if( !( o instanceof AuthConstraint ) ) return false;
    AuthConstraint that = (AuthConstraint) o;
    final Set<AuthStatement> sSet1 = this.statements;
    final Set<AuthStatement> sSet2 = that.statements;

    if( sSet1.size() != sSet2.size() ){
      return false;
    }

    for( AuthStatement s : sSet1 ){
      if( !sSet2.contains( s )){
        return false;
      }
    }
    for( AuthStatement s : sSet2 ){
      if( !sSet1.contains( s )){
        return false;
      }
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hash( statements );
  }

}
