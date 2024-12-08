package network;

import util.Log;

public class TanhLayer extends NetworkLayer {

	public TanhLayer(int inputs, int outputs) {
		super(inputs, outputs);
	}

	private static final String log_name = "tanh_activation";

	public double[] activation_function(double[] val) {
		double[] out = new double[val.length];

		for(int i = 0; i < out.length; i++) {
			out[i] = Math.tanh(val[i]);

			Log.log(log_name, "Activate: " + val[i] + " -> " + out[i]);
		}

		return out;
	}

	public double[] calculate_dCdz(double[] x, double[] z, double[] y, double[] dCdy) {
		double[] out = new double[y.length];

		for(int i = 0; i < out.length; i++) {
			out[i] = dCdy[i] * (1 - y[i] * y[i]);

			Log.log(log_name, "Backpropogate: " + dCdy[i] + ", " + y[i] + " -> " + out[i]);
		}

		return out;
	}

}

