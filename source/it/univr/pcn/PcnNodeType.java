package it.univr.pcn;

/**
 * MISSING_COMMENT
 *
 * @author Mauro Gambini, Sara Migliorini
 * @version 0.0.0
 */
public enum PcnNodeType {

  TWEET,
  RETWEET,
  QUOTE;

  public static PcnNodeType fromString( String s ){
    if( s == null ){
      return null;
    }

    for( PcnNodeType t : values() ){
      if( t.name().toLowerCase().equals( s.toLowerCase() )){
        return t;
      }
    }

    return null;
  }
}
