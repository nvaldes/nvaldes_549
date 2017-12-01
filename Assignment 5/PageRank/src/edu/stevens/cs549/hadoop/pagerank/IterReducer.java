package edu.stevens.cs549.hadoop.pagerank;

import java.io.*;
import java.util.*;

import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.io.*;

public class IterReducer extends Reducer<Text, Text, Text, Text> {
	
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		double d = PageRankDriver.DECAY; // Decay factor
		/* 
		 * DONE: emit key:node+rank, value: adjacency list
		 * Use PageRank algorithm to compute rank from weights contributed by incoming edges.
		 * Remember that one of the values will be marked as the adjacency list for the node.
		 */
		Text adjList = new Text("");
		double sum = 0;
		for (Text t : values) {
			if (t.find("+") == -1) {
				sum += Double.parseDouble(t.toString());
			} else {
				adjList = new Text(t.toString().substring(1));
			}
		}
		Double rank = (1 - d) + (d * sum);
		context.write(new Text(key.toString() + " " + rank.toString()), adjList);
	}
}
