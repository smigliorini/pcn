package it.univr.json;

import twitter4j.User;

import java.util.List;

import static java.lang.String.*;

/**
 * MISSING_COMMENT
 *
 * @author Mauro Gambini, Sara Migliorini
 * @version 0.0.0
 */
public class TwitterUser {

  // === Attributes ============================================================

  public static String idLabel = "id";
  public static String idStrLabel = "id_str";
  public static String nameLabel = "name";
  public static String screenNameLabel = "screen_name";
  public static String locationLabel = "location";
  public static String urlLabel = "url";
  public static String descriptionLabel = "description";
  public static String protectedLabel = "protected";
  public static String verifiedLabel = "verified";
  public static String followersCountLabel = "followers_count";
  public static String friendsCountLabel = "friends_count";
  public static String listedCountLabel = "listed_count";
  public static String favouritesCountLabel = "favourites_count";
  public static String statusesCountLabel = "statuses_count";
  public static String createdAtLabel = "statuses_count";

  // === Properties ============================================================

  // unique identifier for the user
  private Long id;
  // string representation of the user id
  private String idStr;
  // the name of the user, as they have defined it
  private String name;
  // screen name or alias
  private String screenName;
  // user-defined location
  private String location;
  // a URL provided by the user in association to their profile
  private String url;
  // user-defined UTF-8 description of the account
  private String description;
  // true if the user has chosen to protect their tweet
  private Boolean protectedFlag;
  // true if the account is verified
  private Boolean verified;
  // number of followers
  private Integer followerCount;
  // number of friends
  private Integer friendCount;
  // number of public lists that this user is a member of
  private Integer listedCount;
  // number of tweets this user has like in the account's lifetime
  private Integer favouritesCount;
  //  number of tweets (included retweets) issued by the user
  private Integer statusesCount;
  // the UTC datetime that the user account was created
  private String createdAt;

  private List<Long> followers;
  private List<Long> friends;

  // === Methods ===============================================================

  public TwitterUser() {
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

  public String getName() {
    return name;
  }

  public void setName( String name ) {
    this.name = name;
  }

  public String getScreenName() {
    return screenName;
  }

  public void setScreenName( String screenName ) {
    this.screenName = screenName;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation( String location ) {
    this.location = location;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl( String url ) {
    this.url = url;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription( String description ) {
    this.description = description;
  }

  public Boolean getProtectedFlag() {
    return protectedFlag;
  }

  public void setProtectedFlag( Boolean protectedFlag ) {
    this.protectedFlag = protectedFlag;
  }

  public Boolean getVerified() {
    return verified;
  }

  public void setVerified( Boolean verified ) {
    this.verified = verified;
  }

  public Integer getFollowerCount() {
    return followerCount;
  }

  public void setFollowerCount( Integer followerCount ) {
    this.followerCount = followerCount;
  }

  public Integer getFriendCount() {
    return friendCount;
  }

  public void setFriendCount( Integer friendCount ) {
    this.friendCount = friendCount;
  }

  public Integer getListedCount() {
    return listedCount;
  }

  public void setListedCount( Integer listedCount ) {
    this.listedCount = listedCount;
  }

  public Integer getFavouritesCount() {
    return favouritesCount;
  }

  public void setFavouritesCount( Integer favouritesCount ) {
    this.favouritesCount = favouritesCount;
  }

  public Integer getStatusesCount() {
    return statusesCount;
  }

  public void setStatusesCount( Integer statusesCount ) {
    this.statusesCount = statusesCount;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt( String createdAt ) {
    this.createdAt = createdAt;
  }

  public List<Long> getFollowers() {
    return followers;
  }

  public void setFollowers( List<Long> followers ) {
    this.followers = followers;
  }

  public List<Long> getFriends() {
    return friends;
  }

  public void setFriends( List<Long> friends ) {
    this.friends = friends;
  }

  // ===========================================================================

  public static TwitterUser parseUserObject( User u ){
    final TwitterUser tu = new TwitterUser();

    tu.setId( u.getId() );
    tu.setIdStr( format( "%d", u.getId() ) );
    tu.setName( u.getName() );
    tu.setScreenName( u.getScreenName() );
    tu.setLocation( u.getLocation() );
    tu.setUrl( u.getURL() );
    tu.setDescription( u.getDescription() );
    // tu.setProtectedFlag(  );
    tu.setVerified( u.isVerified() );
    tu.setFollowerCount( u.getFollowersCount() );
    tu.setFriendCount( u.getFriendsCount() );
    tu.setListedCount( u.getListedCount() );
    tu.setFavouritesCount( u.getFavouritesCount() );
    tu.setStatusesCount( u.getStatusesCount() );
    tu.setCreatedAt( new Long( u.getCreatedAt().getTime()).toString() );

    return tu;
  }
}
