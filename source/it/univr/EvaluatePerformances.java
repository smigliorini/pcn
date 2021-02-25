package it.univr;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * MISSING_COMMENT
 *
 * @author Mauro Gambini, Sara Migliorini
 * @version 0.0.0
 */
public class EvaluatePerformances {

  private static final String[] inputs = new String[]{
    "Z:\\_datasets_twitter\\zenodo\\mr_part_files\\mr_log_01.txt",
    "Z:\\_datasets_twitter\\zenodo\\mr_part_files\\mr_log_02.txt",
    "Z:\\_datasets_twitter\\zenodo\\mr_part_files\\mr_log_03.txt",
    "Z:\\_datasets_twitter\\zenodo\\mr_part_files\\mr_log_04.txt",
    "Z:\\_datasets_twitter\\zenodo\\mr_part_files\\mr_log_05.txt",
    "Z:\\_datasets_twitter\\zenodo\\mr_part_files\\mr_log_06.txt",
    "Z:\\_datasets_twitter\\zenodo\\mr_part_files\\mr_log_07.txt",
    "Z:\\_datasets_twitter\\zenodo\\mr_part_files\\mr_log_08.txt",
    "Z:\\_datasets_twitter\\zenodo\\mr_part_files\\mr_log_09.txt",
    "Z:\\_datasets_twitter\\zenodo\\mr_part_files\\mr_log_10.txt",
    "Z:\\_datasets_twitter\\zenodo\\mr_part_files\\mr_log_11.txt",
  };

  private static String mapRunningTime = "[PCN] Mapper running time:";
  private static String numIters = "[PCN] Number of iterations:";
  private static String duration = "DURATION:";

  /**
   * MISSING_COMMENT
   *
   * @param args
   */
  public static void main( String[] args ) {

    System.out.printf( "Dataset\t"
                       + "Total time\t"
                       + "Min Map Time\t"
                       + "Max Map Time\t"
                       + "Avg Map Time\t"
                       + "Min Iters\t"
                       + "Max Iters\t"
                       + "Avg Iters%n");

    for( String in : inputs ) {
      Long minMapRunningTime = Long.MAX_VALUE;
      Long maxMapRunningTime = Long.MIN_VALUE;
      Long avgMapRunningTime = 0L;
      double numMaps = 0.0;

      Long minNumIters = Long.MAX_VALUE;
      Long maxNumIters = Long.MIN_VALUE;
      Long avgNumIters = 0L;

      Long totalTime = 0L;

      try( BufferedReader br = new BufferedReader( new FileReader( in ) ) ) {
        String line;
        while( ( line = br.readLine() ) != null ) {
          if( line.contains( mapRunningTime ) ) {
            final String s = line
              .substring( line.indexOf( mapRunningTime ) + mapRunningTime.length() )
              .trim();
            final Long l = Long.parseLong( s );
            minMapRunningTime = Math.min( minMapRunningTime, l );
            maxMapRunningTime = Math.max( maxMapRunningTime, l );
            avgMapRunningTime += l;
            numMaps += 1;

          } else if( line.contains( numIters ) ) {
            final String s = line
              .substring( line.indexOf( numIters ) + numIters.length() )
              .trim();
            final Long l = Long.parseLong( s );
            minNumIters = Math.min( minNumIters, l );
            maxNumIters = Math.max( maxNumIters, l );
            avgNumIters += l;

          } else if( line.contains( duration ) ) {
            final String s = line
              .substring( line.indexOf( duration ) + duration.length() )
              .replace( ".", "" )
              .trim();
            final Long l = Long.parseLong( s );
            totalTime = l;
          }
        }
      } catch( FileNotFoundException e ) {
        e.printStackTrace();
      } catch( IOException e ) {
        e.printStackTrace();
      }

      System.out.printf( "%s\t"
                         + "%d\t"
                         + "%d\t%d\t%.2f\t"
                         + "%d\t%d\t%.2f%n",
                         in, totalTime,
                         minMapRunningTime, maxMapRunningTime, avgMapRunningTime / numMaps,
                         minNumIters, maxNumIters, avgNumIters / numMaps );
    }
    System.out.printf( "%n%n" );
  }
}
