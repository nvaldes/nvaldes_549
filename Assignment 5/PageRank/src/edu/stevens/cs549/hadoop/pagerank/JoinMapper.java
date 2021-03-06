package edu.stevens.cs549.hadoop.pagerank;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class JoinMapper extends Mapper<LongWritable, Text, Text, Text> {
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException, IllegalArgumentException {
//		String[] nodeRank = value.toString().split("\t");
//		context.write(new Text(nodeRank[0]), new Text(nodeRank[1]));
		String line = value.toString(); // Converts Line to a String
		String[] sections = line.split("\t");
		String[] nodeRank = sections[0].split(" ");
		/*
		 * DONE output key:-rank, value: node
		 * See IterMapper for hints on parsing the output of IterReducer.
		 */
		context.write(new Text(nodeRank[0]), new Text(nodeRank[1]));
	}
}
