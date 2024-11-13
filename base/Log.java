package base;

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

	public static class Logger {
		private String topic, name;
		public double value;
		public Logger(String t, String n) {
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
	}

	public static class SuccessLogger extends Logger {
		private long trials, successes;
		public SuccessLogger(String t, String n) {
			super(t, n);
		}
		public void success() {
			trials++;
			successes++;
		}
		public void fail() {
			trials++;
		}
		@Override
		public String toString() {
			double rate = trials == 0 ? 0 : ((double) successes / trials);
			return getName() + ": " + rate;
		}
	}

	private static Map<String, PrintWriter> logs;
	private static ArrayList<Logger> loggers;

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
			
			for(Logger l : loggers) {
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
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("logs/" + topic + ".log")));
				logs.put(topic, out);
			} catch(IOException e) {
				throw new RuntimeException("Failed to create new log topic");
			}
		}

		PrintWriter out = logs.get(topic);
		out.println(txt);

		if(focus.equals(topic)) {
			Utility.println(System.out, txt);
		}
	}

}

