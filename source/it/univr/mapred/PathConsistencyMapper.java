package it.univr.mapred;

import it.univr.pcn.AuthConstraint;
import it.univr.pcn.Direction;
import it.univr.pcn.PcnEdge;
import it.univr.pcn.PcnEdgeType;
import it.univr.pcn.PcnNode;
import it.univr.pcn.PcnUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import static it.univr.pcn.PcnNodeType.TWEET;
import static it.univr.pcn.PcnUtil.*;

/**
 * MISSING_COMMENT
 *
 * @author Mauro Gambini, Sara Migliorini
 * @version 0.0.0
 */
public class PathConsistencyMapper
  extends Mapper<LongWritable, Text, Text, Text> {

  // === Properties ============================================================

  private static final Log log = LogFactory.getLog( PathConsistencyMapper.class );
  private static final boolean checkFriends = true;

  private long start, end;

  // source  -> target -> edge
  private Map<PcnNode, Map<PcnNode, PcnEdge>> edgeMap;
  private List<PcnEdge> edgeList;
  private Map<Long, PcnUser> userMap;

  private FileSystem hdfs;

  // === Methods ===============================================================


  @Override
  protected void setup( Context context )
    throws IOException, InterruptedException {
    edgeMap = new HashMap<>();
    edgeList = new ArrayList<>();
    userMap = new HashMap<>();

    start = System.currentTimeMillis();

    final Configuration conf = context.getConfiguration();
    hdfs = FileSystem.get( conf );
    final URI[] cachedFiles = context.getCacheFiles();
    if( cachedFiles != null && cachedFiles.length > 0 ) {
      final URI userUri = cachedFiles[0];
      try( BufferedReader br = new BufferedReader
        ( new InputStreamReader( hdfs.open( new Path( userUri ) ) ) ) ) {
        String line;
        while( ( line = br.readLine() ) != null ) {
          final PcnUser user = PcnUser.fromText( line );
          userMap.put( user.getId(), user );
        }
      }
    }
  }

  @Override
  protected void map( LongWritable key, Text value, Context context )
    throws IOException, InterruptedException {

    final PcnEdge e = PcnEdge.fromText( value.toString() );
    edgeList.add( e );

    Map<PcnNode,PcnEdge> nodeEdgeMap = edgeMap.get( e.getSource() );
    if( nodeEdgeMap == null ) {
      nodeEdgeMap = new HashMap<>();
    }
    nodeEdgeMap.put( e.getTarget(), e );
    edgeMap.put( e.getSource(), nodeEdgeMap );

    Map<PcnNode, PcnEdge> inverseMap = edgeMap.get( e.getTarget() );
    if( inverseMap == null ) {
      inverseMap = new HashMap<>();
    }
    inverseMap.put
      ( e.getSource(),
        new PcnEdge
          ( e.getTarget(),
            e.getSource(),
            inversion( e.getConstraint() ),
            e.getType() ,
            Direction.INVERSE ));
    edgeMap.put( e.getTarget(), inverseMap );//*/
  }

  @Override
  protected void cleanup( Context context )
    throws IOException, InterruptedException {

    final String key = ( (FileSplit) context.getInputSplit() ).getPath().getName();

    List<PcnNode[]> triangleList = getTriangles();
    boolean finished = triangleList.size() == 0;

    int i = 0;
    while( !finished ) {
      final List<PcnNode[]> tmpTriangleList = new ArrayList<>();

      for( PcnNode[] triangle : triangleList ) {
        final AuthConstraint cij =
          ( ( edgeMap.get( triangle[0] ) ).get( triangle[2] ) ).getConstraint();
        final AuthConstraint cik =
          ( ( edgeMap.get( triangle[0] ) ).get( triangle[1] ) ).getConstraint();
        final AuthConstraint ckj =
          ( ( edgeMap.get( triangle[1] ) ).get( triangle[2] ) ).getConstraint();

        final Long user = triangle[0].getUserId();
        final Set<Long> friendSet = userMap.get( user ).getFriendSet();
        if( triangle[0].getType() == TWEET )
          friendSet.add( triangle[0].getUserId() );
        if( triangle[1].getType() == TWEET )
          friendSet.add( triangle[1].getUserId() );
        if( triangle[2].getType() == TWEET )
          friendSet.add( triangle[2].getUserId() );

        final AuthConstraint ncij = reduction( cij, cik, ckj, friendSet, checkFriends );
        if( !cij.equals( ncij ) ) {
          tmpTriangleList.add( triangle );

          // update edgeMap
          final PcnEdge e = ( edgeMap.get( triangle[0] ) ).get( triangle[2] );
          e.setConstraint( ncij );
          final PcnEdge e2 = ( edgeMap.get( triangle[2] )).get( triangle[0] );
          e2.setConstraint( inversion( ncij ) );
        }
      }

      triangleList = tmpTriangleList;
      if( triangleList.size() == 0 ) {
          finished = true;
      }
      i += 1;
    }

    edgeList.clear();
    for( PcnNode target : edgeMap.keySet() ) {
      final Map<PcnNode,PcnEdge> sourceEdgeMap = edgeMap.get( target );
      for( PcnNode source : sourceEdgeMap.keySet() ){
        final PcnEdge edge = sourceEdgeMap.get( source );
        if( edge.getDirection() == Direction.DIRECT ){
          final AuthConstraint c = normalization( edge.getConstraint() );
          edge.setConstraint( c );
          // edgeList.add( edge );
          context.write( new Text( key ), new Text( edge.toText() ) );
        }
      }
    }

    end = System.currentTimeMillis();
    log.info( String.format( "[PCN] Mapper running time: %d", end - start ));
    log.info( String.format( "[PCN] Number of iterations: %d", i ) );
  }

  // ===========================================================================

  /**
   * MISSING_COMMENT
   */

  private List<PcnNode[]> getTriangles() {
    final List<PcnNode[]> triangleList = new ArrayList<>();
    if( edgeList != null && !edgeList.isEmpty() ) {
      for( PcnEdge edge : edgeList ) {
        // todo fast fix
        if( //edge.getType() == PcnEdgeType.RT_EDGE &&
            edge.getDirection() == Direction.DIRECT ) {
          final PcnNode xi = edge.getSource();
          final PcnNode xj = edge.getTarget();

          final Set<PcnNode> intermediates = edgeMap.get( xi ).keySet();
          if( intermediates != null && !intermediates.isEmpty() ) {
            for( PcnNode xk : intermediates ) {
              if( edgeMap.get( xk ).keySet().contains( xj ) ) {
                triangleList.add( new PcnNode[]{xi, xk, xj} );
              }
            }
          }
        }
      }
    }

    return triangleList;
  }
}
