package it.univr.mapred;

import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.LazyOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import java.io.IOException;

import org.apache.commons.logging.Log;

/**
 * MISSING_COMMENT
 *
 * @author Mauro Gambini, Sara Migliorini
 * @version 0.0.0
 */
public class ProvenanceMR {

  // === Attributes ============================================================

  private static final int NUM_ARGS = 2;

  private static final Log logger = LogFactory.getLog( ProvenanceMR.class );

  // === Methods ===============================================================

  /**
   * MISSING_COMMENT
   *
   * @param args
   */
  public static void main( String[] args )
    throws InterruptedException,
    IOException, ClassNotFoundException {

    final long start = System.currentTimeMillis();

    final Path inputPath = new Path( args[0] );
    final Path outputPath = new Path( String.format( "%s%d", args[1], start ));

    final Path userPath = new Path( args[2] );

    // System.out.printf( "START: %d.%n", start );
    logger.info( String.format( "START: %d.%n", start ));

    final Job job = propagationJob( inputPath, outputPath, userPath );
    final int res = job.waitForCompletion( true ) ? 0 : 1;

    final long end = System.currentTimeMillis();
    // System.out.printf( "END: %d.%n", end );
    // System.out.printf( "DURATION: %d.%n", end - start );
    logger.info( String.format( "END: %d.%n", end ) );
    logger.info( String.format("DURATION: %d.%n", end - start) );

    System.exit( res );
  }

  /**
   * MISSING_COMMENT
   *
   *
   * @param inPath
   * @param outPath
   * @param userPath
   * @return
   * @throws IOException
   */

  private static Job propagationJob( Path inPath, Path outPath, Path userPath )
    throws IOException {

    if( inPath == null ) {
      throw new NullPointerException();
    }
    if( outPath == null ) {
      throw new NullPointerException();
    }
    if( userPath == null ) {
      throw new NullPointerException();
    }

    final Configuration conf = new Configuration();

    final Job job = Job.getInstance( conf );
    job.setJarByClass( ProvenanceMR.class );
    job.setJobName( "ProvenancePropagation" );
    logger.info( "ProvenancePropagation started: job created..." );

    // -------------------------------------------------------------------------

    final FileSystem cfs = FileSystem.get( conf );
    job.addCacheFile( cfs.resolvePath( userPath ).toUri() );

    // -------------------------------------------------------------------------


    // set job input and output path
    FileInputFormat.addInputPath( job, inPath );
    FileOutputFormat.setOutputPath( job, outPath );

    // output produced by the map!
    job.setOutputKeyClass( Long.class );
    job.setOutputValueClass( Text.class );

    // set job input format
    job.setInputFormatClass( TextInputFormat.class );

    // set map class and the map output key and value classes
    job.setMapOutputKeyClass( Text.class );
    job.setMapOutputValueClass( Text.class );
    job.setMapperClass( PathConsistencyMapper.class );

    // set reduce classe and the reduce output key and value classes
    job.setReducerClass( PathConsistencyReducer.class );

    // set job output format
    // job.setOutputFormatClass( TextOutputFormat.class );
    // LazyOutputFormat is necessary for MultipleOutputs
    LazyOutputFormat.setOutputFormatClass( job, TextOutputFormat.class );
    FileOutputFormat.setOutputPath( job, outPath );
    job.setOutputKeyClass( Text.class );
    job.setOutputValueClass( Text.class );

    return job;



  }

  // ===========================================================================

  private static void printMenu(){

  }
}
