
import Jama.Matrix;

public class LogisticRegression {

	public final int inputs, hiddens, outputs;
	public final int hidden_layers;

	private Matrix[] weights;
	private Matrix[] biases;

	private int training = 1;

	public LogisticRegression(int in, int hid, int hid_count, int out) {
		inputs = in;
		hiddens = hid;
		hidden_layers = hid_count;
		outputs = out;

		weights = new Matrix[hidden_layers + 1];
		biases = new Matrix[hidden_layers + 1];

		int to, from;
		for(int i = 0; i < hidden_layers + 1; i++) {
			from = i == 0 ? inputs : hiddens;
			to = i == hidden_layers ? outputs : hiddens;

			weights[i] = new Matrix(from, to, -1);
			biases[i] = new Matrix(1, to, -1);

			for(int t = 0; t < to; t++) {
				biases[i].set(0, t, 2 * Math.random() - 1);
				for(int f = 0; f < from; f++) {
					weights[i].set(f, t, 2 * Math.random() - 1);
				}
			}
		}
	}

	private Matrix[] pass(Matrix in) {
		Matrix[] out = new Matrix[hidden_layers + 2];
		out[0] = in;

		for(int i = 0; i < hidden_layers + 1; i++) {
			out[i + 1] = pass(weights[i], biases[i], out[i]);
		}

		return out;
	}

	public double[] pass(double[] in) {
		Matrix m = new Matrix(in, 1);

		Matrix[] out = pass(m);

		return out[hidden_layers + 1].getRowPackedCopy();
	}

	public static Matrix pass(Matrix weights, Matrix biases, Matrix in) {
		Matrix out = in.times(weights).plus(biases);

		for(int row = 0; row < out.getRowDimension(); row++) {
			for(int col = 0; col < out.getColumnDimension(); col++) {
				out.set(row, col, logistic(out.get(row, col)));
			}
		}

		return out;
	}

	public void train(double[] in, double[] out) {
		Matrix i = new Matrix(in, 1);
		Matrix o = new Matrix(out, 1);

		train(i, o);
	}

	public void train(Matrix in, Matrix out) {
		Matrix[] y = pass(in);

		Matrix dCdy = y[hidden_layers + 1].minus(out);

		backpropogate(y, dCdy);
	}

	public void backpropogate(double[] in, double[] dCdy) {
		Matrix i = new Matrix(in, 1);
		Matrix d = new Matrix(dCdy, 1);

		backpropogate(i, d);
	}

	public void backpropogate(Matrix in, Matrix dCdy) {
		Matrix[] out = pass(in);
		backpropogate(out, dCdy);
	}

	private void backpropogate(Matrix[] out, Matrix dCdy) {
		double rate = Config.alpha / (Math.log(training++) + 1);

		for(int i = hidden_layers; i >= 0; i--) {
			dCdy = backpropogate(weights[i], biases[i], out[i], out[i + 1], dCdy, rate);
		}
	}

	public static Matrix backpropogate(Matrix weights, Matrix biases, Matrix in, Matrix out, Matrix dCdy, double alpha) {
		int outputs = weights.getColumnDimension();

		Matrix ones = new Matrix(1, outputs, 1);
		Matrix dCdz = dCdy.arrayTimes(out).arrayTimes(ones.minus(out));
		Matrix dCdw = in.transpose().times(dCdz);

		Matrix dCdx = dCdz.times(weights.transpose());

		weights.minusEquals(dCdw.times(alpha));
		biases.minusEquals(dCdz.times(alpha));

		return dCdx;
	}

	public static double logistic(double x) {
		return 1 / (1 + Math.exp(-x));
	}

}

