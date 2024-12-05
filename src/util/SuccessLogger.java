package util;

public class SuccessLogger extends Log {
	private long trials, successes;
	public SuccessLogger(String t, String n) {
		super(t, n);
	}
	public void reset() {
		trials = 0;
		successes = 0;
	}
	public void success() {
		trials++;
		successes++;
	}
	public void fail() {
		trials++;
	}
	public double get() {
		if(trials == 0) return 0;
		return (double) successes / trials;
	}
	@Override
	public String toString() {
		double rate = trials == 0 ? 0 : ((double) successes / trials);
		return getName() + ": " + rate + " (" + successes + "/" + trials + ")";
	}
}

