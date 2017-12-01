package edu.stevens.cs549.hadoop.pagerank;

import java.io.IOException;

import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.io.*;

public class InitMapper extends Mapper<LongWritable, Text, Text, Text> {

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException,
			IllegalArgumentException {
		String[] line = value.toString().split(": "); // Converts Line to a String
		/* 
		 * DONE Just echo the input, since it is already in adjacency list format.
		 */
		if (line.length == 1) {
			line = new String[] {line[0], ""};
		}
		Text outKey = new Text(line[0]);
		Text outVal =  new Text(line[1]);
		context.write(outKey, outVal);
		// return;

	}

}
