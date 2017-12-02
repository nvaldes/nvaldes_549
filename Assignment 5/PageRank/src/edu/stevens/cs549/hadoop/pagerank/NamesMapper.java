package edu.stevens.cs549.hadoop.pagerank;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class NamesMapper extends Mapper<LongWritable, Text, Text, Text> {
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException, IllegalArgumentException {
		String[] nodeName = value.toString().split(": ");
		context.write(new Text(nodeName[0]), new Text("_" + nodeName[1]));
	}
}
