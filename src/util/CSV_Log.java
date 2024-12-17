package util;

public class CSV_Log extends Log {

	private StringBuilder old, output;
	private boolean done;
	private String[] init;

	public CSV_Log(String topic, String name, String... titles) {
		super(topic, name);

		init = titles;
		old = null;
		done = false;
		output = new StringBuilder();
		add(titles);
	}

	public void add(Object[] vals) {
		for(Object val : vals) {
			output.append(val).append(",");
		}
		output.deleteCharAt(output.length() - 1);
		output.append("\n");
	}

	public void complete() {
		done = true;
	}

	public void clear() {
		old = output;
		done = false;
		output = new StringBuilder();
		add(init);
	}

	@Override
	public String toString() {
		if(old == null || done) {
			return output.toString();
		} else {
			return old.toString();
		}
	}

}

