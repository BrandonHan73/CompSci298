package util;

public class FinalMatrix {

	private double[][] mat;
	public final int rows, cols;

	public FinalMatrix(double[][] vals) {
		rows = vals.length;
		cols = vals[0].length;
		mat = new double[rows][cols];

		for(int r = 0; r < vals.length; r++) {
			if(vals[r].length != cols) throw new RuntimeException("Not a rectangular matrix");

			for(int c = 0; c < vals[r].length; c++) {
				mat[r][c] = vals[r][c];
			}
		}
	}

	public FinalMatrix(double[][] vals, double epsilon) {
		this(vals);

		for(int r = 0; r < vals.length; r++) {
			for(int c = 0; c < vals[r].length; c++) {
				mat[r][c] += epsilon * (2 * Math.random() - 1);
			}
		}
	}

	public double get(int row, int col) {
		return mat[row][col];
	}

}

