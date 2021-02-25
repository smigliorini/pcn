package it.univr;

import it.univr.pcn.Pcn;
import it.univr.pcn.PcnEdge;
import it.univr.pcn.PcnUser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.String.format;

/**
 * MISSING_COMMENT
 *
 * @author Mauro Gambini, Sara Migliorini
 * @version 0.0.0
 */
public class PcnConstruction {

  private static final String[] sourceFiles = new String[]{
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\full_hydrated_dataset_01.tsv",
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\full_hydrated_dataset_02.tsv",
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\full_hydrated_dataset_03.tsv",
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\full_hydrated_dataset_04.tsv",
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\full_hydrated_dataset_06.tsv",
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\full_hydrated_dataset_0007.tsv",
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\full_hydrated_dataset_0008.tsv",
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\full_hydrated_dataset_0009.tsv",
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\full_hydrated_dataset_0010.tsv",//*/
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\full_hydrated_dataset_0011.tsv",//*/
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\full_hydrated_dataset_0012.tsv",
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\full_hydrated_dataset_0013.tsv",
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\full_hydrated_dataset_0014.tsv",
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\full_hydrated_dataset_0015.tsv",//*/
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\full_hydrated_dataset_0016.tsv",
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\full_hydrated_dataset_0017.tsv",
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\full_hydrated_dataset_0018.tsv",

    //"Z:\\_datasets_twitter\\zenodo\\hydrated_files\\full_hydrated_dataset_0050.tsv",
  };

  private static final String[] userFiles = new String[]{
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\user_dataset_01.tsv",
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\user_dataset_02.tsv",
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\user_dataset_03.tsv",
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\user_dataset_04.tsv",
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\user_dataset_06.tsv",
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\user_dataset_0007.tsv",
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\user_dataset_0008.tsv",
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\user_dataset_0009.tsv",
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\user_dataset_0010.tsv",
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\user_dataset_0011.tsv",
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\user_dataset_0012.tsv",
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\user_dataset_0013.tsv",
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\user_dataset_0014.tsv",
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\user_dataset_0015.tsv",
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\user_dataset_0016.tsv",
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\user_dataset_0017.tsv",
    "Z:\\_datasets_twitter\\zenodo\\hydrated_files\\user_dataset_0018.tsv",

    //"Z:\\_datasets_twitter\\zenodo\\hydrated_files\\user_dataset_0050.tsv",
  };

  private static final String outFileTemplate =
    "Z:\\_datasets_twitter\\zenodo\\mr_part_files\\input\\part-%04d";

  private static final String enhancementFile =
    "Z:\\_datasets_twitter\\zenodo\\mr_part_files\\enhancements.csv";

  private static final String userFile =
    "Z:\\_datasets_twitter\\zenodo\\mr_part_files\\users.csv";

  private static final boolean checkFriends = true;
  private static final boolean writeUserFile = true;

  // === Methods ===============================================================

  /**
   * MISSING_COMMENT
   *
   * @param args
   */
  public static void main( String[] args ) {
    final Pcn network = new Pcn();

    final List<String> lines = new ArrayList<>();
    for( String sourceFile : sourceFiles ) {
      try( BufferedReader br = new BufferedReader( new FileReader( sourceFile ) ) ) {
        String line;
        int index = 0;
        while( ( line = br.readLine() ) != null ) {
          if( index > 0 ) {
            lines.add( line );
          }
          index++;
        }
      } catch( FileNotFoundException e ) {
        e.printStackTrace();
      } catch( IOException e ) {
        e.printStackTrace();
      }
    }

    System.out.printf( "Found: %d lines.%n", lines.size() );

    final List<String> userLines = new ArrayList<>();
    for( String userFile : userFiles ) {
      try( BufferedReader br = new BufferedReader( new FileReader( userFile ) ) ) {
        String line;
        int index = 0;
        while( ( line = br.readLine() ) != null ) {
          if( index > 0 ) {
            userLines.add( line );
          }
          index++;
        }
      } catch( FileNotFoundException e ) {
        e.printStackTrace();
      } catch( IOException e ) {
        e.printStackTrace();
      }
    }

    if( !userLines.isEmpty() && !lines.isEmpty() ) {
      final Map<Long, PcnUser> userMap = PcnUser.buildUsers( userLines );
      System.out.printf( "Found: %d users.%n", userMap.size() );

      // --- write users' file -------------------------------------------------
      if( writeUserFile ) {
        try( BufferedWriter wr = new BufferedWriter( new FileWriter( userFile ) ) ) {
          for( PcnUser u : userMap.values() ) {
            wr.write( u.toText() );
            wr.write( "\n" );
          }
        } catch( IOException e ) {
          e.printStackTrace();
        }
      }//*/
      // -----------------------------------------------------------------------

      network.buildPcn( lines, userMap, checkFriends );
    }

    final List<Pcn> subnetworkList = network.getSubNetworks();
    System.out.printf( "Found: %d subnetworks.%n", subnetworkList.size() );
    int minNodes = Integer.MAX_VALUE;
    int maxNodes = Integer.MIN_VALUE;
    int minEdges = Integer.MAX_VALUE;
    int maxEdges = Integer.MIN_VALUE;
    double avgNodes = 0.0, avgEdges = 0.0;

    long start = Long.MAX_VALUE;
    long end = Long.MIN_VALUE;

    if( subnetworkList.size() > 0 ) {
      for( Pcn subnet : subnetworkList ) {
        minNodes = min( minNodes, subnet.getNodeMap().values().size() );
        maxNodes = max( maxNodes, subnet.getNodeMap().values().size() );
        minEdges = min( minEdges, subnet.getEdgeSet().size() );
        maxEdges = max( maxEdges, subnet.getEdgeSet().size() );
        avgNodes += subnet.getNodeMap().size();
        avgEdges += subnet.getEdgeSet().size();

        for( PcnEdge e : subnet.getEdgeSet() ) {
          start = min( start, e.getTarget().getTimestamp() );
          end = max( end, e.getSource().getTimestamp() );
        }
      }

      System.out.printf
        ( "[NODES]: min = %d, max = %d, avg = %.2f%n",
          minNodes, maxNodes, avgNodes / subnetworkList.size() );
      System.out.printf
        ( "[EDGES]: min = %d, max = %d, avg = %.2f%n",
          minEdges, maxEdges, avgEdges / subnetworkList.size() );

      final SimpleDateFormat f = new SimpleDateFormat( "dd-MM-yyyy HH:mm" );
      System.out.printf( "[TIMESTAMP]: from %s to %s",
                         f.format( new Date( start ) ),
                         f.format( new Date( end ) ) );
    }


    for( int i = 0; i < subnetworkList.size(); i++ ) {
      final Pcn s = subnetworkList.get( i );
      final String fileName = format( outFileTemplate, i );
      try( BufferedWriter br = new BufferedWriter( new FileWriter( fileName ) ) ) {
        br.write( s.toText() );
      } catch( IOException e ) {
        e.printStackTrace();
      }
    }

    /*final Map<Pcn.Pair, Integer> enhancements = network.getLoopEnhancements();
    try( BufferedWriter br = new BufferedWriter( new FileWriter( enhancementFile ) ) ) {
      for( Map.Entry<Pcn.Pair, Integer> e : enhancements.entrySet() ) {
        br.write( format( "%d, ", e.getKey().getUser1() ) );
        br.write( format( "%d, ", e.getKey().getUser2() ) );
        br.write( format( "%d%n", e.getValue() ) );
      }
    } catch( IOException e ) {
      e.printStackTrace();
    }//*/
  }
}
