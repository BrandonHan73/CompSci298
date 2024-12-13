package network;

import util.Utility;

public class SoftplusLayer extends NetworkLayer {

	public SoftplusLayer(int inputs, int outputs) {
		super(inputs, outputs);
	}

	public double[] activation_function(double[] val) {
		double[] out = new double[val.length];

		double exp;
		for(int i = 0; i < out.length; i++) {
			exp = Math.exp(val[i]);
			if(Double.isNaN(exp) || Double.isInfinite(exp)) {
				out[i] = val[i];
			} else {
				out[i] = Math.log(exp + 1);
			}
		}

		return out;
	}

	public double[] calculate_dCdz(double[] x, double[] z, double[] y, double[] dCdy) {
		double[] out = new double[y.length];

		for(int i = 0; i < out.length; i++) {
			out[i] = dCdy[i] * Utility.logistic(z[i]);
		}

		return out;
	}

}

