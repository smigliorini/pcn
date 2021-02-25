package it.univr.json;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.lang.Math.max;

/**
 * MISSING_COMMENT
 *
 * @author Mauro Gambini, Sara Migliorini
 * @version 0.0.0
 */
public class JsonUtils {

  /**
   * MISSING_COMMENT
   *
   * @param inFile
   */

  public static void readLine( File inFile ) {

    final Map<Tweet,Integer> refs = new HashMap<>();

    int count = 0;

    try( BufferedReader br = new BufferedReader( new FileReader( inFile ) ) ) {
      String line;

      while( ( line = br.readLine() ) != null ) {
        count = count + 1;
        final Tweet t = Tweet.parseTweetObject( new JSONObject( line ) );
        if( t.isRetweet() || t.isQuoteTw() ) {
          Integer r = refs.get( t.getRetweetedStatus() );
          if( r == null ){
            r = 0;
          }
          refs.put( t.getRetweetedStatus(), r+1 );
        }
      }

    } catch( FileNotFoundException e ) {
      System.out.printf( "File \"%s\" not found.%n", inFile );
    } catch( IOException e ) {
      System.out.printf( "Unable to read file \"%s\".%n", inFile );
    }

    Tweet mostPopularTweet = null;
    Integer maxReference = 0;
    for( Map.Entry<Tweet, Integer> e : refs.entrySet() ){
      maxReference = Math.max( maxReference, e.getValue() );
      if( maxReference.equals( e.getValue() )){
        mostPopularTweet = e.getKey();
      }
    }

    if( mostPopularTweet != null ) {
      System.out.printf
        ( "MPT: %s [count: %d <-> %d].%n",
          mostPopularTweet.getIdStr(),
          maxReference,
          ( mostPopularTweet.getRetweetCount() != null ? mostPopularTweet.getRetweetCount() : 0 ) +
          ( mostPopularTweet.getQuoteCount() != null ? mostPopularTweet.getQuoteCount() : 0 ) );
    } else {
      System.out.printf( "MPT: Not found!%n" );
    }

    // -------------------------------------------------------------------------

    try( BufferedReader br = new BufferedReader( new FileReader( inFile ) ) ) {
      String line;
      int twCount = 0, rtCount = 0, qtCount = 0;
      final Set<Long> refTwIds = new HashSet<>();
      final Set<Long> twIds = new HashSet<>();

      while( ( line = br.readLine() ) != null ) {
        final Tweet t = Tweet.parseTweetObject( new JSONObject( line ) );

        if( t.isRetweet() && t.getRetweetedStatus().equals( mostPopularTweet )) {
          /*System.out.printf
            ( "[RT][%s] %s <-> [%s] %s <-> Num. retweets %d <-> %d. %n",
              t.getIdStr(),
              t.getCreatedAt(),
              t.getRetweetedStatus().getIdStr(),
              t.getRetweetedStatus().getCreatedAt(),
              t.getRetweetCount(),
              t.getRetweetedStatus().getRetweetCount() );//*/
          rtCount += 1;
          refTwIds.add( t.getRetweetedStatus().getId() );
        }

        if( t.isQuoteTw() ) {
          /*System.out.printf
            ( "[QT][%s] %s <> [%s] %s %n",
              t.getIdStr(),
              t.getCreatedAt(),
              t.getQuotedStatus().getIdStr(),
              t.getQuotedStatus().getCreatedAt() );//*/
          qtCount += 1;
          refTwIds.add( t.getQuotedStatus().getId() );
        }

        twCount += 1;
        twIds.add( t.getId() );
      }//*/

      System.out.printf( "MPT text: %s.%n", mostPopularTweet.getText() );

      System.out.printf( "%s: %d.%n", inFile.getName(), count );

      /*System.out.printf( "#TW = %d, #RT = %d, #QT = %d.%n", twCount, rtCount, qtCount );
      System.out.printf( "# referenced tweets: %d", refTwIds.size() );
      refTwIds.retainAll( twIds );
      System.out.printf( "--> # processed tweets: %d%n", refTwIds.size() );//*/



    } catch( FileNotFoundException e ) {
      System.out.printf( "File \"%s\" not found.%n", inFile );
    } catch( IOException e ) {
      System.out.printf( "Unable to read file \"%s\".%n", inFile );
    }
  }

  public static boolean hasPopulatedProperty( JSONObject o, String propertyLabel ) {
    if( o.has( propertyLabel ) && !o.isNull( propertyLabel ) ) {
      return true;
    } else {
      return false;
    }
  }

}
