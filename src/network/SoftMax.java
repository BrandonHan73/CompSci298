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
		int nan_produced = 0;
		int[] nan_coordinates = null;

		for(int row = 0; row < out.getRowDimension(); row++) {
			for(int col = 0; col < out.getColumnDimension(); col++) {
				double curr = Math.exp(out.get(row, col));

				if(Double.isNaN(curr)) {
					nan_produced++;
					nan_coordinates = new int[] {row, col};
				}

				sum += curr;
				if(sum < 0) {
					throw new RuntimeException("Overflow occurred in softmax layer");
				}
				out.set(row, col, curr);
			}
		}

		if(nan_produced > 1) {
			throw new RuntimeException("Softmax produced " + nan_produced + " NaN values");
		}

		for(int row = 0; row < out.getRowDimension(); row++) {
			for(int col = 0; col < out.getColumnDimension(); col++) {
				out.set(
					row, col, 

					nan_produced == 0 ? 
					(out.get(row, col) / sum) :
					( (row == nan_coordinates[0] && col == nan_coordinates[1]) ? 1 : 0 )
				);
			}
		}

		return out;
	}

}

