package edu.stevens.cs549.hadoop.pagerank;

import java.io.*;

import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.io.*;

public class DiffRed2 extends Reducer<Text, Text, Text, Text> {

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		Double diff_max = 0.0; // sets diff_max to a default value
		/* 
		 * DONE: Compute and emit the maximum of the differences
		 */
		for (Text t : values) {
			double curr = Double.parseDouble(t.toString());
			if (curr > diff_max) {
				diff_max = curr;
			}
		}
		context.write(new Text(diff_max.toString()), new Text());
	}
}
