package util;

import java.awt.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Log {

	private String topic, name;
	public double value;
	public Log(String t, String n) {
		topic = t;
		name = n;
		value = 0;
		loggers.add(this);
	}
	public String getName() {
		return name;
	}
	public String getTopic() {
		return topic;
	}
	@Override
	public String toString() {
		return name + ": " + value;
	}

	private static Map<String, PrintWriter> logs;
	private static ArrayList<Log> loggers;

	public static String focus;

	static {
		focus = "";
		logs = new HashMap<>();
		loggers = new ArrayList<>();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			for(String topic : logs.keySet()) {
				PrintWriter out = logs.get(topic);
				out.println("\n\n\n--------------------------------\n");
			}

			for(Log l : loggers) {
				log(l.topic, l.toString());
			}

			for(String topic : logs.keySet()) {
				logs.get(topic).close();
			}
		}));
	}

	public static void log(String topic, String txt) {
		if(!logs.containsKey(topic)) {
			try {
				String filename = "logs/" + topic + ".log";
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
				logs.put(topic, out);
			} catch(IOException e) {
				throw new RuntimeException("Failed to create new log topic");
			}
		}

		PrintWriter out = logs.get(topic);
		out.println(txt);

		if(focus.equals(topic)) {
			Utility.println(txt);
		}
	}

}

