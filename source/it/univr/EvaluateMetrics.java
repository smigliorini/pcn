package it.univr;

import it.univr.pcn.Metrics;
import it.univr.pcn.Pcn;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * MISSING_COMMENT
 *
 * @author Mauro Gambini, Sara Migliorini
 * @version 0.0.0
 */
public class EvaluateMetrics {

  private static final String[] inputs = new String[]{
    "Z:\\_datasets_twitter\\zenodo\\mr_part_files\\output_01",
    "Z:\\_datasets_twitter\\zenodo\\mr_part_files\\output_02",
    "Z:\\_datasets_twitter\\zenodo\\mr_part_files\\output_03",
    "Z:\\_datasets_twitter\\zenodo\\mr_part_files\\output_04",
    "Z:\\_datasets_twitter\\zenodo\\mr_part_files\\output_05",
    "Z:\\_datasets_twitter\\zenodo\\mr_part_files\\output_06",
    "Z:\\_datasets_twitter\\zenodo\\mr_part_files\\output_07",
    "Z:\\_datasets_twitter\\zenodo\\mr_part_files\\output_08",
    "Z:\\_datasets_twitter\\zenodo\\mr_part_files\\output_09",
    "Z:\\_datasets_twitter\\zenodo\\mr_part_files\\output_10",//*/
    "Z:\\_datasets_twitter\\zenodo\\mr_part_files\\output_11",
  };

  // ===========================================================================

  public static void main( String[] args ) throws IOException {

    final List<String> lines = new ArrayList<>();

    System.out.printf( "Dataset\tACS\tSNI\tRSI\t%%RSD%n" );

    for( String in : inputs ) {
      final File directory = new File( in );
      final List<Double> sizes = new ArrayList<>();

      if( directory.isDirectory() ) {
        final List<Path> paths = walk( directory.getAbsolutePath() );

        for( Path p : paths ) {
          final File f = p.toFile();
          sizes.add( new Double( f.length() ) );

          try( BufferedReader br = new BufferedReader( new FileReader( f ) ) ) {
            String line;
            while( ( line = br.readLine() ) != null ) {
              lines.add( line );
            }
          } catch( FileNotFoundException e ) {
            e.printStackTrace();
          } catch( IOException e ) {
            e.printStackTrace();
          }
        }
      }

      if( !lines.isEmpty() ) {
        final Pcn network = new Pcn();
        network.fromText( lines );

        final double acs = Metrics.averageConstraintSize( network );
        final double sni = Metrics.sparseNodeIncidence( network );
        final double rsi = Metrics.retweetSourceIncidence( network );
        final double rsd = rsd( sizes );

        System.out.printf( "%s\t%.2f\t%.2f\t%.2f\t%.2f%n", in, acs, sni, rsi, rsd );
      }
    }
  }

  // ===========================================================================

  private static List<Path> walk( String directory ) throws IOException {
    if( directory == null ) {
      throw new NullPointerException();
    }

    final List<Path> result = new ArrayList<>();

    Files.walk( Paths.get( directory ) )
         .filter( Files::isRegularFile )
         .forEach( s -> result.add( s ) );

    return result;
  }

  // ===========================================================================

  private static double mean( List<Double> values ){
    if( values == null ) {
      throw new NullPointerException();
    }

    double sum = 0.0;
    for( Double v : values ){
      sum += v;
    }

    return sum / values.size();
  }

  private static double std( List<Double> values, Double mean ){
    if( values == null ) {
      throw new NullPointerException();
    }
    if( mean == null ) {
      throw new NullPointerException();
    }

    double std = 0.0;
    for( Double v : values ){
      std += Math.pow( v - mean, 2 );
    }

    return Math.sqrt( std / values.size() );
  }

  private static double rsd( List<Double> values ){
    if( values == null ) {
      throw new NullPointerException();
    }

    double mean = mean( values );
    double std = std( values, mean );
    return std * 100 / mean;
  }
}
