package edu.stevens.cs549.hadoop.pagerank;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.io.*;

public class InitReducer extends Reducer<Text, Text, Text, Text> {

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		/* 
		 * DONE Output key: node+rank, value: adjacency list
		 */
		List<String> outVal = new ArrayList<String>();
		for(Text t: values) {
			outVal.add(t.toString());
		}
		context.write(new Text(key.toString() + " 1"), new Text(String.join(" ", outVal)));
	}
}
