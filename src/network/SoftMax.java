package network;

import Jama.Matrix;

public class SoftMax extends LogisticRegression {

	public SoftMax(int in, int hid, int hid_count, int out) {
		super(in, hid, hid_count, out);
	}

	@Override
	protected Matrix[] pass(Matrix in) {
		Matrix[] out = new Matrix[hidden_layers + 2];
		out[0] = in;

		for(int i = 0; i < hidden_layers; i++) {
			out[i + 1] = pass(weights[i], biases[i], out[i]);
		}
		out[hidden_layers + 1] = softmax_pass(weights[hidden_layers], biases[hidden_layers], out[hidden_layers]);

		return out;
	}

	public static Matrix softmax_pass(Matrix weights, Matrix biases, Matrix in) {
		Matrix out = in.times(weights).plus(biases);

		double sum = 0;

		for(int row = 0; row < out.getRowDimension(); row++) {
			for(int col = 0; col < out.getColumnDimension(); col++) {
				double curr = Math.exp(out.get(row, col));
				sum += curr;
				out.set(row, col, curr);
			}
		}

		for(int row = 0; row < out.getRowDimension(); row++) {
			for(int col = 0; col < out.getColumnDimension(); col++) {
				out.set(row, col, out.get(row, col) / sum);
			}
		}

		return out;
	}

}

