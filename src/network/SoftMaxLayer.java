package network;

import util.Log;

public class SoftMaxLayer extends NetworkLayer {

	private final static int default_nan_index = -1;
	private final static double preprocess_center, preprocess_tolerance = 1;
	private final static String soft_max_log = "softmax_log";

	private final static String log_name = "softmax_preprocessing";
	static {
		double nan_point, zero_point, exp;

		double max = Double.MAX_VALUE, min = -Double.MAX_VALUE, mid;
		while(max > min + preprocess_tolerance) {
			mid = max / 2 + min / 2;
			exp = Math.exp(mid);
			Log.log(log_name, max + " " + min + " -> " + mid + " -> " + exp);
			if(Double.isNaN(exp) || Double.isInfinite(exp)) {
				max = mid;
			} else {
				min = mid;
			}
		}
		nan_point = min;
		Log.log(log_name, "NaN point is " + nan_point);

		max = Double.MAX_VALUE; 
		min = -Double.MAX_VALUE;
		while(max > min + preprocess_tolerance) {
			mid = max / 2 + min / 2;
			exp = Math.exp(mid);
			Log.log(log_name, max + " " + min + " -> " + mid + " -> " + exp);
			if(exp == 0) {
				min = mid;
			} else {
				max = mid;
			}
		}
		zero_point = max;
		Log.log(log_name, "Zero point is " + zero_point);

		preprocess_center = (nan_point + zero_point) / 2;

		Log.log(log_name, zero_point + " >> " + preprocess_center + " << " + nan_point);
	}

	private static double[] preprocess(double[] val) {
		Log.log(soft_max_log, "Preprocessing start");

		double[] preprocess = new double[val.length];
		double min = val[0], max = val[0], mid;
		for(double d : val) {
			Log.log(soft_max_log, d + "");
			min = Math.min(min, d);
			max = Math.max(max, d);
		}
		mid = (max + min) / 2;
		Log.log(soft_max_log, min + " >> " + mid + " << " + max);

		for(int i = 0; i < preprocess.length; i++) {
			preprocess[i] = val[i] - mid + preprocess_center;
			Log.log(soft_max_log, preprocess[i] + "");
		}

		Log.log(soft_max_log, "Preprocessing end");

		return preprocess;
	}

	public SoftMaxLayer(int inputs, int outputs) {
		super(inputs, outputs);
	}

	public double[] activation_function(double[] val) {
		Log.log(soft_max_log, "Activation function start");
		val = preprocess(val);

		double sum = 0, exp;
		int nan_index = default_nan_index;
		for(int i = 0; i < val.length; i++) {
			exp = Math.exp(val[i]);

			if(Double.isNaN(exp)) {
				if(nan_index != default_nan_index) {
					throw new RuntimeException("Too many NaN values");
				}
				nan_index = i;
			}

			val[i] = exp;
			sum += exp;
		}

		double[] out = new double[val.length];

		if(nan_index == default_nan_index) {
			for(int i = 0; i < out.length; i++) {
				out[i] = val[i] / sum;
			}
		} else {
			for(int i = 0; i < out.length; i++) {
				out[i] = 0;
			}
			out[nan_index] = 1;
		}

		for(double d : out) {
			Log.log(soft_max_log, d + "");
		}
		Log.log(soft_max_log, "Activation function end\n");

		return out;
	}

	public double[] calculate_dCdz(double[] x, double[] z, double[] y, double[] dCdy) {
		double derivative_expectation = 0;
		for(int i = 0; i < output_count; i++) {
			derivative_expectation += y[i] * dCdy[i];
		}

		double[] out = new double[output_count];
		for(int i = 0; i < output_count; i++) {
			out[i] = y[i] * (dCdy[i] - derivative_expectation);
		}

		return out;
	}

}

