package it.univr.json;

import org.json.JSONObject;
import twitter4j.Status;

import java.util.Objects;

import static it.univr.json.JsonUtils.hasPopulatedProperty;
import static java.lang.String.*;

/**
 * MISSING_COMMENT
 *
 * @author Mauro Gambini, Sara Migliorini
 * @version 0.0.0
 */
public class Tweet {

  // === Attributes ============================================================

  public static String createdAtLabel = "created_at";
  public static String idStrLabel = "id_str";
  public static String idLabel = "id";
  public static String textLabel = "text";
  public static String sourceLabel = "source";
  public static String truncatedLabel = "truncated";
  public static String inReplyToStatusIdLabel = "in_reply_to_status_id";
  public static String inReplyToStatusIdStrLabel = "in_reply_to_status_id_str";
  public static String inReplyToUserIdLabel = "in_reply_to_user_id";
  public static String inReplyToUserIdStrLabel = "in_reply_to_user_id_str";
  public static String inReplyToScreenNameLabel = "in_reply_to_screen_name";
  public static String userLabel = "user";
  public static String coordinateLabel = "coordinates";
  public static String placeLabel = "place";
  public static String quoteStatusIdLabel = "quoted_status_id";
  public static String quoteStatusIdStrLabel = "quoted_status_id_str";
  public static String isQuoteStatusLabel = "is_quote_status";
  public static String quotedStatusLabel = "quoted_status";
  public static String retweetedStatusLabel = "retweeted_status";
  public static String quoteCountLabel = "quote_count";
  public static String replyCountLabel = "reply_count";
  public static String retweetCountLabel = "retweet_count";
  public static String favoriteCountLabel = "favorite_count";
  public static String entitiesLabel = "entities";
  public static String extendedEntitiesLabel = "extended_entities";
  public static String favoritedLabel = "favorited";
  public static String retweetedLabel = "retweeted";
  public static String possiblySensitiveLabel = "possibly_sensitive";
  public static String filterLevelLabel = "filter_level";
  public static String langLabel = "lang";
  public static String matchingRulesLabel = "matching_rules";

  // === Properties ============================================================

  // UTC time when this Tweet was created
  private String createdAt;
  // unique identifier for the Tweet
  private Long id;
  // string representation of the Tweet id
  private String idStr;
  // UTF-8 text of the status update
  private String text;
  // utility used to post the Tweet
  private String source;
  // indicates if the text parameter has been truncated
  private Boolean truncated;

  // --- reply tweet -----------------------------------------------------------
  // id of the original Tweet, if the Tweet is a reply to id, or null
  private Long inReplyToStatusId;
  private String inReplyToStatusIdStr;
  // id of the Tweet author, if the Tweet is a reply to a Tweet, or null
  private Long inReplyToUserId;
  private String inReplyToUserIdStr;
  private String inReplyToScreenName;
  // ---------------------------------------------------------------------------

  // the user who posted the tweet
  private TwitterUser twitterUser;
  // geographic location of the tweet
  private Coordinate coordinates;
  private Place place;

  // --- quote tweet -----------------------------------------------------------
  // id of the original Tweet, if the tweet is quote to it, or null
  private Long quoteStatusId;
  private String quoteStatusIdStr;
  // indicates if this is a quoted tweet
  private Boolean isQuoteStatus;
  // representation of the original tweet that has been quoted
  private Tweet quotedStatus;
  // how many times the tweet has been quoted
  private Integer quoteCount;
  // ---------------------------------------------------------------------------

  // number of times the tweet has been replied to
  private Integer replyCount;
  // number of times has been liked by twitter users
  private Integer favoriteCount;
  // indicates where this tweet has been liked by authenticated users
  private Boolean favorited;

  // --- retweet ---------------------------------------------------------------
  // representation of the original tweet that has been retweeted
  private Tweet retweetedStatus;
  // number of times the ORIGINAL tweet has been retweeted (not this tweet)
  private Integer retweetCount;
  // indicates whether this tweet has been retweetd by authenticated users
  private Boolean retweeted;
  // ---------------------------------------------------------------------------

  // entities which have been parsed out of the text of the tweet
  private Entities entities;
  // media metadata for some entities (e.g. photos or videos)
  private ExtendedEntities extendedEntities;

  // true if the tweet contains a link
  private Boolean possiblySensitive;

  // none, low, medium: maximum value of the filter level parameter
  private String filterLevel;

  private String lang;

  // === Methods ===============================================================

  public Tweet() {
  }

  public boolean isRetweet() {
    if( retweetedStatus != null ) {
      return true;
    } else {
      return false;
    }
  }

  public boolean isQuoteTw() {
    if( quotedStatus != null ) {
      return true;
    } else {
      return false;
    }
  }

  // ===========================================================================

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt( String createdAt ) {
    this.createdAt = createdAt;
  }

  public Long getId() {
    return id;
  }

  public void setId( Long id ) {
    this.id = id;
  }

  public String getIdStr() {
    return idStr;
  }

  public void setIdStr( String idStr ) {
    this.idStr = idStr;
  }

  public String getText() {
    return text;
  }

  public String getTextSingleLine(){
    String result = text;
    //text.replace( "\n", " " );
    //text.replace( "\r", " " );
    result = result.replaceAll( "\\R+", " " );
    return result;
  }

  public void setText( String text ) {
    this.text = text;
  }

  public String getSource() {
    return source;
  }

  public void setSource( String source ) {
    this.source = source;
  }

  public Boolean getTruncated() {
    return truncated;
  }

  public void setTruncated( Boolean truncated ) {
    this.truncated = truncated;
  }

  public Long getInReplyToStatusId() {
    return inReplyToStatusId;
  }

  public void setInReplyToStatusId( Long inReplyToStatusId ) {
    this.inReplyToStatusId = inReplyToStatusId;
  }

  public String getInReplyToStatusIdStr() {
    return inReplyToStatusIdStr;
  }

  public void setInReplyToStatusIdStr( String inReplyToStatusIdStr ) {
    this.inReplyToStatusIdStr = inReplyToStatusIdStr;
  }

  public Long getInReplyToUserId() {
    return inReplyToUserId;
  }

  public void setInReplyToUserId( Long inReplyToUserId ) {
    this.inReplyToUserId = inReplyToUserId;
  }

  public String getInReplyToUserIdStr() {
    return inReplyToUserIdStr;
  }

  public void setInReplyToUserIdStr( String inReplyToUserIdStr ) {
    this.inReplyToUserIdStr = inReplyToUserIdStr;
  }

  public String getInReplyToScreenName() {
    return inReplyToScreenName;
  }

  public void setInReplyToScreenName( String inReplyToScreenName ) {
    this.inReplyToScreenName = inReplyToScreenName;
  }

  public TwitterUser getTwitterUser() {
    return twitterUser;
  }

  public void setTwitterUser( TwitterUser twitterUser ) {
    this.twitterUser = twitterUser;
  }

  public Coordinate getCoordinates() {
    return coordinates;
  }

  public void setCoordinates( Coordinate coordinates ) {
    this.coordinates = coordinates;
  }

  public Place getPlace() {
    return place;
  }

  public void setPlace( Place place ) {
    this.place = place;
  }

  public Long getQuoteStatusId() {
    return quoteStatusId;
  }

  public void setQuoteStatusId( Long quoteStatusId ) {
    this.quoteStatusId = quoteStatusId;
  }

  public String getQuoteStatusIdStr() {
    return quoteStatusIdStr;
  }

  public void setQuoteStatusIdStr( String quoteStatusIdStr ) {
    this.quoteStatusIdStr = quoteStatusIdStr;
  }

  public Boolean getQuoteStatus() {
    return isQuoteStatus;
  }

  public void setQuoteStatus( Boolean quoteStatus ) {
    isQuoteStatus = quoteStatus;
  }

  public Tweet getQuotedStatus() {
    return quotedStatus;
  }

  public void setQuotedStatus( Tweet quotedStatus ) {
    this.quotedStatus = quotedStatus;
  }

  public Integer getQuoteCount() {
    return quoteCount;
  }

  public void setQuoteCount( Integer quoteCount ) {
    this.quoteCount = quoteCount;
  }

  public Integer getReplyCount() {
    return replyCount;
  }

  public void setReplyCount( Integer replyCount ) {
    this.replyCount = replyCount;
  }

  public Integer getFavoriteCount() {
    return favoriteCount;
  }

  public void setFavoriteCount( Integer favoriteCount ) {
    this.favoriteCount = favoriteCount;
  }

  public Boolean getFavorited() {
    return favorited;
  }

  public void setFavorited( Boolean favorited ) {
    this.favorited = favorited;
  }

  public Tweet getRetweetedStatus() {
    return retweetedStatus;
  }

  public void setRetweetedStatus( Tweet retweetedStatus ) {
    this.retweetedStatus = retweetedStatus;
  }

  public Integer getRetweetCount() {
    return retweetCount;
  }

  public void setRetweetCount( Integer retweetCount ) {
    this.retweetCount = retweetCount;
  }

  public Boolean getRetweeted() {
    return retweeted;
  }

  public void setRetweeted( Boolean retweeted ) {
    this.retweeted = retweeted;
  }

  public Entities getEntities() {
    return entities;
  }

  public void setEntities( Entities entities ) {
    this.entities = entities;
  }

  public ExtendedEntities getExtendedEntities() {
    return extendedEntities;
  }

  public void setExtendedEntities( ExtendedEntities extendedEntities ) {
    this.extendedEntities = extendedEntities;
  }

  public Boolean getPossiblySensitive() {
    return possiblySensitive;
  }

  public void setPossiblySensitive( Boolean possiblySensitive ) {
    this.possiblySensitive = possiblySensitive;
  }

  public String getFilterLevel() {
    return filterLevel;
  }

  public void setFilterLevel( String filterLevel ) {
    this.filterLevel = filterLevel;
  }

  public String getLang() {
    return lang;
  }

  public void setLang( String lang ) {
    this.lang = lang;
  }

  // ===========================================================================

  /**
   * MISSING_COMMENT
   *
   * @param o
   * @return
   */
  public static Tweet parseTweetObject( JSONObject o ) {
    final Tweet t = new Tweet();
    t.setCreatedAt( hasPopulatedProperty( o, createdAtLabel ) ? o.getString( createdAtLabel ) : null );
    t.setId( hasPopulatedProperty( o, idLabel ) ? o.getLong( idLabel ) : null );
    t.setIdStr( hasPopulatedProperty( o, idStrLabel ) ? o.getString( idStrLabel ) : null );
    t.setText( hasPopulatedProperty( o, textLabel ) ? o.getString( textLabel ) : null );
    t.setSource( hasPopulatedProperty( o, sourceLabel ) ? o.getString( sourceLabel ) : null );
    t.setTruncated( hasPopulatedProperty( o, truncatedLabel ) ? o.getBoolean( truncatedLabel ) : null );
    t.setInReplyToStatusId( hasPopulatedProperty( o, inReplyToStatusIdLabel ) ? o.getLong( inReplyToStatusIdLabel ) : null );
    t.setInReplyToStatusIdStr( hasPopulatedProperty( o, inReplyToStatusIdStrLabel ) ? o.getString( inReplyToStatusIdStrLabel ) : null );
    t.setInReplyToUserId( hasPopulatedProperty( o, inReplyToUserIdLabel ) ? o.getLong( inReplyToUserIdLabel ) : null );
    t.setInReplyToUserIdStr( hasPopulatedProperty( o, inReplyToUserIdStrLabel ) ? o.getString( inReplyToUserIdStrLabel ) : null );
    t.setInReplyToScreenName( hasPopulatedProperty( o, inReplyToScreenNameLabel ) ? o.getString( inReplyToScreenNameLabel ) : null );

    // USER

    // COORDINATE
    if( hasPopulatedProperty( o, coordinateLabel ) ){
      System.out.printf( "[%s] COORDINATES.%n", t.getIdStr() );
    }

    // PLACE
    if( hasPopulatedProperty( o, placeLabel )){
      System.out.printf( "[%s] PLACE: %s.%n", t.getIdStr(), o.getJSONObject( placeLabel ).getString( "name" ) );
    }

    t.setQuoteStatusId( hasPopulatedProperty( o, quoteStatusIdLabel ) ? o.getLong( quoteStatusIdLabel ) : null );
    t.setQuoteStatusIdStr( hasPopulatedProperty( o, quoteStatusIdStrLabel ) ? o.getString( quoteStatusIdStrLabel ) : null );
    t.setQuoteStatus( hasPopulatedProperty( o, isQuoteStatusLabel  ) ? o.getBoolean( isQuoteStatusLabel ) : null );
    t.setQuotedStatus( hasPopulatedProperty( o, quotedStatusLabel ) ? parseTweetObject( o.getJSONObject( quotedStatusLabel ) ) : null );

    t.setQuoteCount( hasPopulatedProperty( o, quoteCountLabel ) ? o.getInt( quoteCountLabel ) : null );
    t.setReplyCount( hasPopulatedProperty( o, replyCountLabel ) ? o.getInt( replyCountLabel ) : null );
    t.setFavoriteCount( hasPopulatedProperty( o, favoriteCountLabel ) ? o.getInt( favoriteCountLabel ) : null );
    t.setFavorited( hasPopulatedProperty( o, favoritedLabel ) ? o.getBoolean( favoritedLabel ) : null );
    t.setRetweetedStatus( hasPopulatedProperty( o, retweetedStatusLabel ) ? parseTweetObject( o.getJSONObject( retweetedStatusLabel ) ) : null );
    t.setRetweetCount( hasPopulatedProperty( o, retweetCountLabel ) ? o.getInt( retweetCountLabel ) : null );
    t.setRetweeted( hasPopulatedProperty( o, retweetedLabel ) ? o.getBoolean( retweetedLabel ) : null );

    // ENTITIES

    // EXTENDED ENTITIES

    t.setPossiblySensitive( hasPopulatedProperty( o, possiblySensitiveLabel ) ? o.getBoolean( possiblySensitiveLabel ) : null );
    t.setFilterLevel( hasPopulatedProperty( o, filterLevelLabel ) ? o.getString( filterLevelLabel ) : null );
    t.setLang( hasPopulatedProperty( o, langLabel ) ? o.getString( langLabel ) : null );

    return t;
  }

  /**
   * MISSING_COMMENT
   *
   * @param s
   * @return
   */
  public static Tweet parseTweetObject( Status s ) {
    final Tweet t = new Tweet();
    t.setCreatedAt( new Long( s.getCreatedAt().getTime() ).toString() );
    t.setId( s.getId() );
    t.setIdStr( format( "%d", s.getId() ));
    t.setText( s.getText() );
    t.setSource( s.getSource() );
    t.setTruncated( s.isTruncated() );
    t.setInReplyToStatusId( s.getInReplyToStatusId() );
    t.setInReplyToStatusIdStr( format( "%d", s.getInReplyToStatusId() ));
    t.setInReplyToUserId( s.getInReplyToUserId() );
    t.setInReplyToUserIdStr( format( "%d", s.getInReplyToUserId() ));
    t.setInReplyToScreenName( s.getInReplyToScreenName() );

    // USER
    t.setTwitterUser( TwitterUser.parseUserObject( s.getUser() ) );

    // COORDINATE
    /*if( hasPopulatedProperty( o, coordinateLabel ) ){
      System.out.printf( "[%s] COORDINATES.%n", t.getIdStr() );
    }//*/

    // PLACE
    /*if( hasPopulatedProperty( o, placeLabel )){
      System.out.printf( "[%s] PLACE: %s.%n", t.getIdStr(), o.getJSONObject( placeLabel ).getString( "name" ) );
    }//*/

    t.setQuoteStatusId( s.getQuotedStatusId() );
    t.setQuoteStatusIdStr( format( "%d", s.getQuotedStatusId() ) );
    // t.setQuoteStatus( hasPopulatedProperty( o, isQuoteStatusLabel  ) ? o.getBoolean( isQuoteStatusLabel ) : null );
    t.setQuotedStatus( s.getQuotedStatus() != null ? parseTweetObject( s.getQuotedStatus() ) : null );

    // t.setQuoteCount( );
    // t.setReplyCount(  );
    t.setFavoriteCount( s.getFavoriteCount() );
    t.setFavorited( s.isFavorited() );
    t.setRetweetedStatus( s.getRetweetedStatus() != null ? parseTweetObject( s.getRetweetedStatus() ) : null );
    t.setRetweetCount( s.getRetweetCount() );
    t.setRetweeted( s.isRetweeted() );

    // ENTITIES

    // EXTENDED ENTITIES

    t.setPossiblySensitive( s.isPossiblySensitive() );
    // t.setFilterLevel(  );
    t.setLang( s.getLang() );

    return t;
  }

  // ===========================================================================


  @Override
  public boolean equals( Object o ) {
    if( this == o ) return true;
    if( !( o instanceof Tweet ) ) return false;
    Tweet tweet = (Tweet) o;
    return id.equals( tweet.id );
  }

  @Override
  public int hashCode() {
    return Objects.hash( id );
  }
}
