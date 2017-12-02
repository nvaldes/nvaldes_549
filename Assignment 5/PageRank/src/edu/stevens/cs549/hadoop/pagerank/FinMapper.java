package edu.stevens.cs549.hadoop.pagerank;

import java.io.IOException;

import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.io.*;

public class FinMapper extends Mapper<LongWritable, Text, DoubleWritable, Text> {

	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException, IllegalArgumentException {
		try {
			String line = value.toString(); // Converts Line to a String
			String[] nodeRank = line.split("\t");
			double rank = Double.parseDouble(nodeRank[1]);
			/*
			 * DONE output key:-rank, value: node
			 * See IterMapper for hints on parsing the output of IterReducer.
			 */
			context.write(new DoubleWritable(-rank), new Text(nodeRank[0]));
		} catch (Exception e) {
			return;
		}
	}

}
