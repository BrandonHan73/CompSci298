package util;

public class MaxRecord {
	private double value;
	private VoidString log;
	public MaxRecord() {
		value = 0;
		log = null;
	}
	public void reset() {
		value = 0;
		log = null;
	}
	public void record(double v, VoidString vs) {
		if(v > value) {
			value = v;
			log = vs;
		}
	}
	public void record(double v, String debug) {
		record(v, () -> debug);
	}
	public void record(double v) {
		record(v, "");
	}
	public void record(MaxRecord o) {
		record(o.value, o.log);
	}
	public String log() {
		return log.run();
	}
	public double get() {
		return value;
	}
	@Override
	public String toString() {
		return value + " (" + log() + ")";
	}
}

