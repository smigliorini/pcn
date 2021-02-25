package it.univr.pcn;

import it.univr.util.StringUtil;
import java.util.Objects;

import static it.univr.util.StringUtil.*;

/**
 * MISSING_COMMENT
 *
 * @author Mauro Gambini, Sara Migliorini
 * @version 0.0.0
 */
public class PcnNode {

  // === Attributes ============================================================

  private static String leftDelimiter = "(";
  private static String rightDelimiter = ")";
  private static String separator = ",";

  // === Properties ============================================================

  private Long tweetId;
  private Long userId;
  private Long timestamp;
  private Long sourceTweetId;
  private PcnNodeType type;

  // === Methods ===============================================================

  public PcnNode() {
    this.tweetId = null;
    this.userId = null;
    this.timestamp = null;
    this.sourceTweetId = null;
    this.type = null;
  }

  public PcnNode
    ( Long tweetId,
      Long userId,
      Long timestamp,
      Long sourceTweetId,
      PcnNodeType type ) {

    this.tweetId = tweetId;
    this.userId = userId;
    this.timestamp = timestamp;
    this.sourceTweetId = sourceTweetId;
    this.type = type;
  }

  public Long getTweetId() {
    return tweetId;
  }

  public void setTweetId( Long tweetId ) {
    this.tweetId = tweetId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId( Long userId ) {
    this.userId = userId;
  }

  public Long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp( Long timestamp ) {
    this.timestamp = timestamp;
  }

  public Long getSourceTweetId() {
    return sourceTweetId;
  }

  public void setSourceTweetId( Long sourceTweetId ) {
    this.sourceTweetId = sourceTweetId;
  }

  public PcnNodeType getType() {
    return type;
  }

  public void setType( PcnNodeType type ) {
    this.type = type;
  }

  // ===========================================================================

  /**
   * MISSING_COMMENT
   *
   * @return
   */
  public String toText() {
    final StringBuilder b = new StringBuilder();
    b.append( leftDelimiter );
    b.append( tweetId );
    b.append( separator );
    b.append( userId );
    b.append( separator );
    b.append( timestamp );
    b.append( separator );
    b.append( sourceTweetId != null ? sourceTweetId : "" );
    b.append( separator );
    b.append( type.name() );
    b.append( rightDelimiter );
    return b.toString();
  }

  /**
   * MISSING_COMMENT
   *
   * @param text
   */
  public static PcnNode fromText( String text ) {
    if( text == null ) {
      throw new NullPointerException();
    }

    final PcnNode node = new PcnNode();

    final String t = text
      .replace( leftDelimiter, "" )
      .replace( rightDelimiter, "" );

    final String[] tokens = t.split( separator );
    node.tweetId = parseLong( tokens[0] );
    node.userId = parseLong( tokens[1] );
    node.timestamp = parseLong( tokens[2] );
    node.sourceTweetId = parseLong( tokens[3] );
    node.type = PcnNodeType.fromString( tokens[4] );

    return node;
  }

  // ===========================================================================

  @Override
  public boolean equals( Object o ) {
    if( this == o ) return true;
    if( !( o instanceof PcnNode ) ) return false;
    PcnNode pcnNode = (PcnNode) o;
    return Objects.equals( tweetId, pcnNode.tweetId );
  }

  @Override
  public int hashCode() {
    return Objects.hash( tweetId );
  }
}
