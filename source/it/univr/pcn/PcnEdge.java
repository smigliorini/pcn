package it.univr.pcn;

import java.util.Objects;

import static it.univr.pcn.Direction.*;

/**
 * MISSING_COMMENT
 *
 * @author Mauro Gambini, Sara Migliorini
 * @version 0.0.0
 */
public class PcnEdge {

  // === Attributes ============================================================

  private static final String separator = ";";

  // === Properties ============================================================

  private PcnNode source;
  private PcnNode target;
  private AuthConstraint constraint;
  private PcnEdgeType type;
  private Direction direction;

  // === Methods ===============================================================

  public PcnEdge() {
    this.source = null;
    this.target = null;
    this.constraint = null;
    this.type = null;
    this.direction = null;
  }

  public PcnEdge
    ( PcnNode source,
      PcnNode target,
      AuthConstraint constraint,
      PcnEdgeType type,
      Direction direction ) {

    this.source = source;
    this.target = target;
    this.constraint = constraint;
    this.type = type;
    this.direction = direction;
  }

  public PcnNode getSource() {
    return source;
  }

  public void setSource( PcnNode source ) {
    this.source = source;
  }

  public PcnNode getTarget() {
    return target;
  }

  public void setTarget( PcnNode target ) {
    this.target = target;
  }

  public AuthConstraint getConstraint() {
    return constraint;
  }

  public void setConstraint( AuthConstraint constraint ) {
    this.constraint = constraint;
  }

  public PcnEdgeType getType() {
    return type;
  }

  public void setType( PcnEdgeType type ) {
    this.type = type;
  }

  public Direction getDirection() {
    return direction;
  }

  public void setDirection( Direction direction ) {
    this.direction = direction;
  }

  // ===========================================================================

  /**
   * MISSING_COMMENT
   *
   * @return
   */
  public String toText(){
    final StringBuilder b = new StringBuilder();
    b.append( source.toText() );
    b.append( separator );
    b.append( target.toText() );
    b.append( separator );
    b.append( constraint.toText() );
    b.append( separator );
    b.append( type.name() );

    return b.toString();
  }

  /**
   * MISSING_COMMENT
   *
   * @param text
   * @return
   */
  public static PcnEdge fromText( String text ){
    if(  text == null ) {
      throw new NullPointerException();
    }

    final PcnEdge e = new PcnEdge();

    final String[] tokens = text.split( separator );
    e.source = PcnNode.fromText( tokens[0] );
    e.target = PcnNode.fromText( tokens[1] );
    e.constraint = AuthConstraint.fromText( tokens[2] );
    e.type = PcnEdgeType.fromString( tokens[3] );
    e.direction = DIRECT;

    return e;
  }

  // ===========================================================================

  @Override
  public boolean equals( Object o ) {
    if( this == o ) return true;
    if( !( o instanceof PcnEdge ) ) return false;
    PcnEdge pcnEdge = (PcnEdge) o;
    return Objects.equals( source, pcnEdge.source ) &&
           Objects.equals( target, pcnEdge.target ) &&
           Objects.equals( constraint, pcnEdge.constraint ) &&
           type == pcnEdge.type;
  }

  @Override
  public int hashCode() {
    return Objects.hash( source, target, constraint, type );
  }
}
