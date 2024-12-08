package network;

import util.Utility;

public class HyperbolicTangentLayer extends NetworkLayer {

	public HyperbolicTangentLayer(int inputs, int outputs) {
		super(inputs, outputs);
	}

	public double[] activation_function(double[] val) {
		double[] out = new double[val.length];

		for(int i = 0; i < out.length; i++) {
			out[i] = Math.tanh(val[i]);
		}

		return out;
	}

	public double[] calculate_dCdz(double[] x, double[] z, double[] y, double[] dCdy) {
		double[] out = new double[y.length];

		for(int i = 0; i < out.length; i++) {
			out[i] = dCdy[i] * (1 - y[i] * y[i]);
		}

		return out;
	}

}

