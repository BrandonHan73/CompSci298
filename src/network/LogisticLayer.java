package network;

import util.Utility;

public class LogisticLayer extends NetworkLayer {

	public LogisticLayer(int inputs, int outputs) {
		super(inputs, outputs);
	}

	public double[] activation_function(double[] val) {
		double[] out = new double[val.length];

		for(int i = 0; i < out.length; i++) {
			out[i] = Utility.logistic(val[i]);
		}

		return out;
	}

	public double[] calculate_dCdz(double[] x, double[] z, double[] y, double[] dCdy) {
		double[] out = new double[y.length];

		for(int i = 0; i < out.length; i++) {
			out[i] = dCdy[i] * y[i] * (1 - y[i]);
		}

		return out;
	}

}

