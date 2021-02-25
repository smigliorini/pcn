package it.univr.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * MISSING_COMMENT
 *
 * @author Mauro Gambini, Sara Migliorini
 * @version 0.0.0
 */
public class StringUtil {

  private StringUtil() {
    // nothing here
  }

  // ===========================================================================

  /**
   * MISSING_COMMENT
   *
   * @param line
   * @param separator
   * @return
   */

  public static String[] splitString( String line, String separator ) {
    if( line == null ) {
      throw new NullPointerException();
    }
    if( separator == null ) {
      throw new IllegalArgumentException();
    }

    // final List<String> tokens = new ArrayList<>();

    final String[] fields = line.split( separator );
    return fields;

    /*final StringTokenizer tk = new StringTokenizer( line, separator );
    while( tk.hasMoreTokens() ) {
      final String s = tk.nextToken();
      if( s == null || s.isEmpty() ){
        tokens.add( "" );
      } else {
        tokens.add( s );
      }
    }

    String[] result = new String[tokens.size()];
    result = tokens.toArray( result );
    return result;//*/
  }


  /**
   * MISSING_COMMENT
   *
   * @param s
   * @return
   */

  public static Integer parseInteger( String s ) {
    if( s == null ) {
      throw new NullPointerException();
    }

    if( s == null || s.isEmpty() ){
      return null;
    }

    try {
      return Integer.parseInt( s );
    } catch( NumberFormatException e ) {
      System.out.printf
        ( "The string \"%s\" does not represent an integer value.%n", s );
      return null;
    }
  }


  /**
   * MISSING_COMMENT
   *
   * @param s
   * @return
   */

  public static Long parseLong( String s ) {
    if( s == null ) {
      throw new NullPointerException();
    }

    if( s == null || s.isEmpty() ){
      return null;
    }

    try {
      return Long.parseLong( s );
    } catch( NumberFormatException e ) {
      System.out.printf
        ( "The string \"%s\" does not represent a long integer value.%n", s );
      return null;
    }
  }


  /**
   * MISSING_COMMENT
   *
   * @param s
   * @return
   */

  public static Double parseDouble( String s ) {
    if( s == null ) {
      throw new NullPointerException();
    }

    if( s == null || s.isEmpty() ){
      return null;
    }

    try {
      return Double.parseDouble( s );
    } catch( NumberFormatException e ) {
      System.out.printf
        ( "The string \"%s\" does not represent an double value.%n", s );
      return null;
    }
  }
}
