package it.univr;

import it.univr.mapred.PathConsistencyMapper;
import it.univr.pcn.AuthConstraint;
import it.univr.pcn.Direction;
import it.univr.pcn.Pcn;
import it.univr.pcn.PcnEdge;
import it.univr.pcn.PcnNode;
import it.univr.pcn.PcnUser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static it.univr.pcn.Direction.*;
import static it.univr.pcn.PcnEdgeType.*;
import static it.univr.pcn.PcnNodeType.RETWEET;
import static it.univr.pcn.PcnNodeType.TWEET;

/**
 * MISSING_COMMENT
 *
 * @author Mauro Gambini, Sara Migliorini
 * @version 0.0.0
 */
public class MainExample {

  public static final String outFile1 = "Z:\\_datasets_twitter\\zenodo\\mr_part_files\\input\\part-0000";
  public static final String outFile2 = "Z:\\_datasets_twitter\\zenodo\\mr_part_files\\input\\part-0001";
  public static final String userFile = "Z:\\_datasets_twitter\\zenodo\\mr_part_files\\users.csv";


  /**
   * MISSING_COMMENT
   *
   * @param args
   */

  public static void main( String[] args ) {

    // --- subnetwork 1 --------------------------------------------------------
    final Pcn network1 = new Pcn();
    final PcnNode t1 = new PcnNode( 1L, 1L, 90L, null, TWEET );
    final PcnNode rt1 = new PcnNode( 10L, 10L, 110L, 1L, RETWEET );
    final PcnNode rt2 = new PcnNode( 20L, 20L, 120L, 1L, RETWEET );
    final PcnNode rt3 = new PcnNode( 30L, 30L, 100L, 1L, RETWEET );
    final PcnNode rt4 = new PcnNode( 40L, 40L, 95L, 1L, RETWEET );

    network1.addNode( t1 );
    network1.addNode( rt1 );
    network1.addNode( rt2 );
    network1.addNode( rt3 );
    network1.addNode( rt4 );

    network1.addEdge( new PcnEdge( rt2, t1, new AuthConstraint( 1L, 0.18, 1.0 ), RT_EDGE, DIRECT ) );
    network1.addEdge( new PcnEdge( rt1, t1, new AuthConstraint( 1L, 0.33, 1.0 ), RT_EDGE, DIRECT ) );
    network1.addEdge( new PcnEdge( rt2, rt1, new AuthConstraint( 10L, 0.55, 1.0 ), RR_EDGE, DIRECT ) );
    network1.addEdge( new PcnEdge( rt2, rt3, new AuthConstraint( 30L, 0.27, 1.0 ), RR_EDGE, DIRECT ) );
    network1.addEdge( new PcnEdge( rt3, t1, new AuthConstraint( 1L, 0.33, 1.0), RT_EDGE, DIRECT ) );
    network1.addEdge( new PcnEdge( rt1, rt3, new AuthConstraint( 30L, 0.67, 1.0 ), RR_EDGE, DIRECT ) );
    network1.addEdge( new PcnEdge( rt3, rt4, new AuthConstraint( 40L, 0.67, 1.0 ), RR_EDGE, DIRECT ) );
    network1.addEdge( new PcnEdge( rt4, t1, new AuthConstraint( 1L, 1.0, 1.0 ), RT_EDGE, DIRECT ) );

    try( BufferedWriter wr = new BufferedWriter( new FileWriter( outFile1 ) ) ){
      wr.write( network1.toText() );
    } catch( IOException e ) {
      e.printStackTrace();
    }

    //network1.addEdge( new PcnEdge( t2, rt2, new AuthConstraint( 2L, 1.0, 1.0 ), TT_EDGE ) );
    //network1.addEdge( new PcnEdge( rt5, rt3, new AuthConstraint( 3L, 1.0, 1.0 ), TT_EDGE ) );

    // --- subnetwork 2 --------------------------------------------------------

    final Pcn network2 = new Pcn();
    final PcnNode t2 = new PcnNode( 2L, 20L, 150L, null, TWEET );
    final PcnNode rt5 = new PcnNode( 50L, 30L, 170L, 2L, RETWEET );
    final PcnNode rt6 = new PcnNode( 60L, 60L, 155L, 2L, RETWEET );
    network2.addNode( t2 );
    network2.addNode( rt5 );
    network2.addNode( rt6 );
    network2.addEdge( new PcnEdge( rt5, t2, new AuthConstraint( 20L, 0.43, 1.0 ), RT_EDGE, DIRECT ) );
    network2.addEdge( new PcnEdge( rt5, rt6, new AuthConstraint( 60L, 0.57, 1.0 ), RR_EDGE, DIRECT ) );
    network2.addEdge( new PcnEdge( rt6, t2, new AuthConstraint( 20L, 1.0, 1.0 ),  RT_EDGE, DIRECT ) );

    try( BufferedWriter wr = new BufferedWriter( new FileWriter( outFile2 ) ) ){
      wr.write( network2.toText() );
    } catch( IOException e ) {
      e.printStackTrace();
    }

    // --- users ---------------------------------------------------------------

    final PcnUser u1 = new PcnUser( 1L, "u", null, null );
    final PcnUser ur1 = new PcnUser( 10L, "u1", null, Stream.of( 30L ).collect( Collectors.toCollection( HashSet::new) ) );
    final PcnUser ur2 = new PcnUser( 20L, "u2", null, Stream.of( 10L, 30L ).collect( Collectors.toCollection( HashSet::new) ) );
    final PcnUser ur3 = new PcnUser( 30L, "u3", null, Stream.of( 40L, 60L ).collect( Collectors.toCollection( HashSet::new) ) );
    final PcnUser ur4 = new PcnUser( 40L, "u4", null, null );
    final PcnUser ur6 = new PcnUser( 60L, "u6", null, null );

    final PcnUser[] users = new PcnUser[]{
      u1, ur1, ur2, ur3, ur4, ur6
    };

    try( BufferedWriter wr = new BufferedWriter( new FileWriter( userFile ) ) ){
      for( PcnUser u : users ) {
        wr.write( u.toText() );
        wr.write( "\n" );
      }
    } catch( IOException e ) {
      e.printStackTrace();
    }
  }
}
