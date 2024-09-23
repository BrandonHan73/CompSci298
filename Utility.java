
import java.io.PrintStream;

public class Utility {

	public static void print(PrintStream out, int[][] arr) {
		for(int[] row : arr) {
			for(int i = 0; i < row.length - 1; i++) {
				out.print(row[i] + ", ");
			}
			out.println(row[row.length - 1]);
		}
	}

	public static void print(PrintStream out, int[] arr) {
		for(int i = 0; i < arr.length - 1; i++) {
			out.print(arr[i] + ", ");
		}
		out.println(arr[arr.length - 1]);
	}

	public static void print(PrintStream out, double[][] arr) {
		for(double[] row : arr) {
			for(int i = 0; i < row.length - 1; i++) {
				out.print(row[i] + ", ");
			}
			out.println(row[row.length - 1]);
		}
	}

	public static void print(PrintStream out, double[] arr) {
		for(int i = 0; i < arr.length - 1; i++) {
			out.print(arr[i] + ", ");
		}
		out.println(arr[arr.length - 1]);
	}

	public static void print(PrintStream out, Position pos) {
		out.print("(" + pos.row + ", " + pos.col + ")");
	}

}

