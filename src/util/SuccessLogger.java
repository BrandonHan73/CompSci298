package util;

public class SuccessLogger extends Log {
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

