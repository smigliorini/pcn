package it.univr.pcn;

/**
 * MISSING_COMMENT
 *
 * @author Mauro Gambini, Sara Migliorini
 * @version 0.0.0
 */
public enum PcnEdgeType {

  RT_EDGE,
  RR_EDGE,
  TT_EDGE;

  public static PcnEdgeType fromString( String s ){
    if( s == null ){
      return null;
    }

    for( PcnEdgeType t : values() ){
      if( t.name().toLowerCase().equals( s.toLowerCase() )){
        return t;
      }
    }

    return null;
  }
}
