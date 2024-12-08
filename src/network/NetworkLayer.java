package network;

import base.Config;

import Jama.Matrix;

public abstract class NetworkLayer {

	public final int input_count, output_count;

	private Matrix weights, biases;
	private Matrix last_x, last_z, last_y;

	public NetworkLayer(int inputs, int outputs) {
		input_count = inputs;
		output_count = outputs;

		weights = new Matrix(inputs, outputs);
		biases = new Matrix(1, outputs);

		for(int out = 0; out < outputs; out++) {
			for(int in = 0; in < inputs; in++) {
				weights.set(in, out, 2 * Math.random() - 1);
			}
			biases.set(0, out, 2 * Math.random() - 1);
		}

		last_x = null;
		last_z = null;
		last_y = null;
	}

	public abstract double[] activation_function(double[] val);
	public abstract double[] calculate_dCdz(double[] x, double[] z, double[] y, double[] dCdy);

	public double[] pass(double[] input) {
		if(input.length != input_count) throw new RuntimeException("Improper input length");

		last_x = new Matrix(input, /* rows */ 1);
		last_z = last_x.times(weights).plus(biases);

		double[] activation_input = last_z.getRowPackedCopy();
		double[] output = activation_function(activation_input);
		last_y = new Matrix(output, /* rows */ 1);

		return output;
	}

	public double[] backpropogate(double[] dCdy) {
		if(dCdy.length != output_count) {
			throw new RuntimeException("Improper input length");
		}

		if(last_x == null) throw new RuntimeException("Must pass forward first");

		double[] x = last_x.getRowPackedCopy();
		double[] z = last_z.getRowPackedCopy();
		double[] y = last_y.getRowPackedCopy();

		double[] dCdz = calculate_dCdz(x, z, y, dCdy);
		Matrix dCdz_mat = new Matrix(dCdz, /* rows */ 1);

		Matrix dCdw = last_x.transpose().times(dCdz_mat);
		Matrix dCdb = dCdz_mat;
		Matrix dCdx = dCdz_mat.times(weights.transpose());

		weights.minusEquals(dCdw.times(Config.alpha));
		biases.minusEquals(dCdb.times(Config.alpha));

		return dCdx.getRowPackedCopy();
	}

}

