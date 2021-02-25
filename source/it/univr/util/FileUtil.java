package it.univr.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * MISSING_COMMENT
 *
 * @author Mauro Gambini, Sara Migliorini
 * @version 0.0.0
 */
public class FileUtil {

  /**
   * MISSING_COMMENT
   *
   * @param f
   * @return
   */
  public static List<Long> getTweetIds( File f, int columnIndex, boolean header ) {
    if( f == null ) {
      throw new NullPointerException();
    }

    final List<Long> ids = new ArrayList<>();

    try( BufferedReader br = new BufferedReader( new FileReader( f ) ) ) {
      String line;
      int count = 0;

      while( ( line = br.readLine() ) != null ) {
        // check if the row has to be processed
        if( header == false || ( header == true && count > 0 ) ) {
          ids.add( getTweetIdInRow( line, columnIndex ) );
          count += 1;
        }
      }
    } catch( FileNotFoundException e ) {
      System.out.printf( "File \"%s\" not found.%n", f );
    } catch( IOException e ) {
      System.out.printf( "Unable to read file \"%s\".%n", f );
    }
    return ids;
  }

  /**
   * MISSING_COMMENT
   *
   * @param line
   * @param index
   * @return
   */

  public static Long getTweetIdInRow( String line, int index ){
      final StringTokenizer tk = new StringTokenizer( line, "\t" );
      int tindex = 0;
      while( tk.hasMoreTokens() ) {
        final String token = tk.nextToken();
        // check if the column is the desired one
        if( tindex == index ) {
          try {
            return Long.parseLong( token );
          } catch( NumberFormatException e ) {
            // nothing here
          }
        }
        tindex += 1;
      }
    return null;
  }


  /**
   * MISSING_COMMENT
   *
   * @param f
   * @return
   */

  private static int countLines( File f ) {
    if( f == null ) {
      throw new NullPointerException();
    }

    int count = 0;

    try( BufferedReader br = new BufferedReader( new FileReader( f ) ) ) {
      while( br.readLine() != null ) {
        count = count + 1;
      }

    } catch( FileNotFoundException e ) {
      System.out.printf( "File \"%s\" not found.%n", f );
    } catch( IOException e ) {
      System.out.printf( "Unable to read file \"%s\".%n", f );
    }

    return count;
  }
}
