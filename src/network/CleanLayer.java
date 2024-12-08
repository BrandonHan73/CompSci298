package network;

import util.Log;

public class CleanLayer extends NetworkLayer {

	public CleanLayer(int inputs, int outputs) {
		super(inputs, outputs);
	}

	private static final String log_name = "clean_layer";

	public double[] activation_function(double[] val) {
		Log.log(log_name, "Activation");
		for(double d : val) {
			Log.log(log_name, d + "");
		}
		
		return val;
	}

	public double[] calculate_dCdz(double[] x, double[] z, double[] y, double[] dCdy) {
		return dCdy;
	}

}

