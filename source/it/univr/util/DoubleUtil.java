package it.univr.util;

/**
 * MISSING_COMMENT
 *
 * @author Mauro Gambini, Sara Migliorini
 * @version 0.0.0
 */
public class DoubleUtil {

  private DoubleUtil(){
    // nothing here
  }

  public static Double truncate( Double number, int decimalPlaces ){
    if( number == null ) {
      return number;
    }

    int truncatedInt = (int) ( number * Math.pow( 10, decimalPlaces ));
    double truncated = truncatedInt / Math.pow( 10, decimalPlaces );
    return truncated;
  }
}
