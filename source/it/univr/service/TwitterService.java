package it.univr.service;

import twitter4j.IDs;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.*;

/**
 * MISSING_COMMENT
 *
 * @author Mauro Gambini, Sara Migliorini
 * @version 0.0.0
 */
public class TwitterService {

  // ===========================================================================

  // put your data!!!
  private static String consumerKey = "***********";
  private static String consumerSecret = "******************";
  private static String accessToken = "****************";
  private static String accessTokenSecret = "********************";

  // singleton static instance
  private static Twitter twitter;
  static{
    final ConfigurationBuilder builder = new ConfigurationBuilder();
    builder.setOAuthConsumerKey( consumerKey );
    builder.setOAuthConsumerSecret( consumerSecret );
    builder.setOAuthAccessToken( accessToken );
    builder.setOAuthAccessTokenSecret( accessTokenSecret );

    final TwitterFactory factory = new TwitterFactory( builder.build() );
    twitter = factory.getInstance();
  }

  // ===========================================================================

  private TwitterService() {
    // nothing here
  }

  // ===========================================================================

  /**
   * @param tweetId
   * @return
   */
  public static Status retrieveTweet( Long tweetId ) {
    if( tweetId == null ) {
      throw new NullPointerException();
    }

    try {
      final Status status = twitter.showStatus( tweetId );
      return status;
    } catch( TwitterException e ) {
      // System.err.printf( "Failed to retrieve tweet with id: %d.%n", tweetId );
      return null;
    }
  }

  /**
   * MISSING_COMMENT
   *
   * @param userId
   * @return
   */
  public static List<Long> retrieveFollowers( Long userId ) {
    if( userId == null ) {
      throw new NullPointerException();
    }

    final List<Long> followers = new ArrayList<>();

    try {
      long cursor = -1L;
      IDs ids;
      do {
        ids = twitter.getFollowersIDs( userId, cursor );
        for( long id : ids.getIDs() ) {
          followers.add( id );
        }
      } while( ( cursor = ids.getNextCursor() ) != 0 );

      sort( followers );
      return followers;
    } catch( TwitterException e ) {
      // System.err.printf( "Failed to retrieve followers for user with id: %d.%n", userId );
      return Collections.emptyList();
    }
  }

  /**
   * MISSING_COMMENT
   *
   * @param userId
   * @return
   */
  public static List<Long> retrieveFriends( Long userId ) {
    if( userId == null ) {
      throw new NullPointerException();
    }

    final List<Long> friends = new ArrayList<>();

    try {
      long cursor = -1L;
      IDs ids;
      do {
        ids = twitter.getFriendsIDs( userId, cursor );
        for( long id : ids.getIDs() ) {
          friends.add( id );
        }
      } while( ( cursor = ids.getNextCursor() ) != 0 );

      sort( friends );
      return friends;
    } catch( TwitterException e ) {
      // System.err.printf( "Failed to retrieve friends for user with id: %d.%n", userId );
      return Collections.emptyList();
    }
  }
}
