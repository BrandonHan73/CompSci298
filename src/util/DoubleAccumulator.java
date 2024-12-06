package util;

public class DoubleAccumulator {

	private double val;

	public DoubleAccumulator() {
		val = 0;
	}

	public void add(double v) {
		val += v;
	}

	public double get() {
		return val;
	}

}

