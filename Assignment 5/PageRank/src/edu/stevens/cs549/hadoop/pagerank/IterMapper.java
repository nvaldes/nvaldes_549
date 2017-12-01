package edu.stevens.cs549.hadoop.pagerank;

import java.io.IOException;

import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.io.*;

public class IterMapper extends Mapper<LongWritable, Text, Text, Text> {

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException,
			IllegalArgumentException {
		String line = value.toString(); // Converts Line to a String
		String[] sections = line.split("\t"); // Splits it into two parts. Part 1: node+rank | Part 2: adj list

		if (sections.length > 2) // Checks if the data is in the incorrect format
		{
			throw new IOException("Incorrect data format");
		}
		if (sections.length != 2) {
			return;
		}
		
		/* 
		 * DONE: emit key: adj vertex, value: computed weight.
		 * 
		 * Remember to also emit the input adjacency list for this node!
		 * Put a marker on the string value to indicate it is an adjacency list.
		 */
		String[] nodeRank = sections[0].toString().split(" ");
		context.write(new Text(nodeRank[0]), new Text("+" + sections[1]));
		String[] vertices = sections[1].toString().split(" ");
		for (int i = 0; i < vertices.length; i++) {
			Double weight = Double.parseDouble(nodeRank[1]) / vertices.length;
			context.write(new Text(vertices[i]), new Text(weight.toString()));
		}
	}

}
