package edu.stevens.cs549.hadoop.pagerank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class JoinReducer extends Reducer<Text, Text, Text, Text> {
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException,
		InterruptedException {
		List<String> vals = new ArrayList<String>();
		for (Text t : values) {
			vals.add(t.toString());
		}
		if (vals.size() == 2) {
			try {
				Double.parseDouble(vals.get(1));
				context.write(new Text(vals.get(0).toString().substring(1)), new Text(vals.get(1)));
			} catch (NumberFormatException e) {
				context.write(new Text(vals.get(1).toString().substring(1)), new Text(vals.get(0)));
			}
		}
	}
}
