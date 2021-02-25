package it.univr.mapred;

import org.apache.commons.lang.ObjectUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import java.io.IOException;
import java.util.HashSet;
import java.util.stream.StreamSupport;

/**
 * MISSING_COMMENT
 *
 * @author Mauro Gambini, Sara Migliorini
 * @version 0.0.0
 */
public class PathConsistencyReducer
  extends Reducer<Text, Text, Text, Text> {

  // === Properties ============================================================

  private MultipleOutputs<Writable,Writable> multipleOutputs;

  // === Methods ===============================================================

  @Override
  protected void setup( Context context )
    throws IOException, InterruptedException {
    multipleOutputs = new MultipleOutputs( context );
  }

  @Override
  protected void reduce( Text key, Iterable<Text> values, Context context ) throws IOException, InterruptedException {
    //super.reduce( key, values, context );
    StreamSupport.stream( values.spliterator(), false ).forEach( data -> {
      try{
        multipleOutputs.write( NullWritable.get(), data, key.toString() );
        //foreachOperation( key, data, context.getConfiguration() );
      } catch( IOException | InterruptedException e ){
        e.printStackTrace();
      }
    } );

  }

  @Override
  protected void cleanup( Context context ) throws IOException, InterruptedException {
    multipleOutputs.close();
  }//*/
}
