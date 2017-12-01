package edu.stevens.cs549.hadoop.pagerank;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.io.*;

public class DiffRed1 extends Reducer<Text, Text, Text, Text> {

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		/* 
		 * DONE: The list of values should contain two ranks.  Compute and output their difference.
		 */
		List<Double> ranks = new ArrayList<Double>();
		for (Text t : values) {
			ranks.add(Double.parseDouble(t.toString()));
		}
		Double diff = 0d;
		try {
			diff = Math.abs(ranks.get(0) - ranks.get(1));
		} catch (IndexOutOfBoundsException e) {
			//don't worry about it?
		}
		context.write(key, new Text(diff.toString()));
	}
}
