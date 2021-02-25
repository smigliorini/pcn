package it.univr;

import it.univr.json.Tweet;
import twitter4j.Status;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static it.univr.json.Tweet.parseTweetObject;
import static it.univr.service.TwitterService.*;
import static it.univr.util.FileUtil.getTweetIdInRow;
import static java.lang.String.format;

/**
 * MISSING_COMMENT
 *
 * @author Mauro Gambini, Sara Migliorini
 * @version 0.0.0
 */
public class TweetHydration {

  // === Attributes ============================================================

  private static final String mainInputDir = "parts";
  private static final String stwFile = "full_dataset_part_%04d.tsv";

  private static final String mainOutputDir = "hydrated_files";
  private static final String twFile = "full_hydrated_dataset_%04d.tsv";

  private static final String userOutputFile = "user_dataset_%04d.tsv";

  private static final int min = 50;
  private static final int max = 1784;

  // === Methods ===============================================================

  public static void main( String[] args ) {

    for( int i = min; i < max; i++ ) {
      process
        ( new File( mainInputDir, format( stwFile, i ) ),
          new File( mainOutputDir, format( twFile, i ) ),
          new File( mainOutputDir, format( userOutputFile, i ) ),
          true, 0 );
      System.out.printf( "Processed file %d/%d.%n",
                         i+1 , max -  min );
    }
  }


  // ===========================================================================

  /**
   * MISSING_COMMENT
   *
   * @param inFile
   * @param twFile
   * @param header
   * @param columnIndex
   */
  private static void process
  ( File inFile,
    File twFile,
    File userFile,
    boolean header,
    int columnIndex ) {

    if( inFile == null ) {
      throw new NullPointerException();
    }
    if( twFile == null ) {
      throw new NullPointerException();
    }
    if( userFile == null ) {
      throw new NullPointerException();
    }


    try( BufferedReader br = new BufferedReader( new FileReader( inFile ) ) ) {
      try( BufferedWriter wr = new BufferedWriter( new FileWriter( twFile ) ) ) {
        try( BufferedWriter uwr = new BufferedWriter( new FileWriter( userFile ) ) ) {

          wr.write( "id\t"
                    + "user_id\t"
                    + "timestamp\t"
                    + "is_retweet\t"
                    + "retweeted_status_id\t"
                    + "is_quoted\t"
                    + "quoted_status_id\t"
                    + "lang\t"
                    + "text\n"
                  );

          uwr.write( "id\t"
                     + "followers\t"
                     + "friends\n" );

          String line;
          int count = 0;

          System.out.printf( "Start tweets processing....%n" );
          while( ( line = br.readLine() ) != null ) {
            // check if the row has to be processed
            if( header == false || ( header == true && count > 0 ) ) {
              final Long id = getTweetIdInRow( line, columnIndex );

              final Status s = retrieveTweet( id );
              if( s != null ) {
                final Tweet t = parseTweetObject( s );

                wr.write( format( "%s\t"
                                  + "%s\t"
                                  + "%s\t"
                                  + "%s\t"
                                  + "%s\t"
                                  + "%s\t"
                                  + "%s\t"
                                  + "%s\t"
                                  + "%s\n",
                                  t.getId(),
                                  t.getTwitterUser().getId(),
                                  t.getCreatedAt(),
                                  t.isRetweet(),
                                  t.isRetweet() ? t.getRetweetedStatus().getId() : "",
                                  t.isQuoteTw(),
                                  t.isQuoteTw() ? t.getQuotedStatus().getId() : "",
                                  t.getLang(),
                                  t.getTextSingleLine()
                                ) );

                final List<Long> followers = retrieveFollowers( t.getTwitterUser().getId() );
                final StringBuilder folStr = new StringBuilder();
                for( int i = 0; i < followers.size(); i++ ) {
                  folStr.append( followers.get( i ) );
                  if( i < followers.size() - 1 ) {
                    folStr.append( ", " );
                  }
                }

                final List<Long> friends = retrieveFriends( t.getTwitterUser().getId() );
                final StringBuilder friStr = new StringBuilder();
                for( int i = 0; i < friends.size(); i++ ) {
                  friStr.append( friends.get( i ) );
                  if( i < friends.size() - 1 ) {
                    friStr.append( ", " );
                  }
                }

                uwr.write( format( "%s\t"
                                   + "[%s]\t"
                                   + "[%s]\n",
                                   t.getTwitterUser().getId(),
                                   folStr.toString(),
                                   friStr.toString() ) );
              }
            } // end if --> process line condition

            if( ( count > 0 ) && ( count % 100 == 0 ) ) {
              System.out.printf( "%d - %d%n", count - 100, count );
            }
            count += 1;
          }
          System.out.printf( "End tweets processing: %d lines%n", count );
        }
      }
    } catch( FileNotFoundException e ) {
      System.out.printf( "File not found: %s.%n", e.getMessage() );
    } catch( IOException e ) {
      System.out.printf( "Unable to read file: %s.%n", e.getMessage() );
    }
  }


}
