
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.IntToDoubleFunction;

public class Utility {

	private static void print_ob(PrintStream out, int[][] arr) {
		for(int[] row : arr) {
			for(int i = 0; i < row.length - 1; i++) {
				out.print(row[i] + ", ");
			}
			out.print(row[row.length - 1]);
		}
	}

	private static void print_ob(PrintStream out, int[] arr) {
		for(int i = 0; i < arr.length - 1; i++) {
			out.print(arr[i] + ", ");
		}
		out.print(arr[arr.length - 1]);
	}

	private static void print_ob(PrintStream out, double[] arr) {
		for(int i = 0; i < arr.length - 1; i++) {
			print_ob(out, arr[i]);
			out.print(", ");
		}
		print_ob(out, arr[arr.length - 1]);
	}

	private static void print_ob(PrintStream out, double d) {
		out.print(String.format("%.4f", d));
	}

	private static void print_ob(PrintStream out, Position pos) {
		out.print("(" + pos.row + ", " + pos.col + ")");
	}

	private static void print_ob(PrintStream out, Object o) {
		if(o instanceof int[][]) {
			print_ob(out, (int[][]) o);
		} else if(o instanceof int[]) {
			print_ob(out, (int[]) o);
		} else if(o instanceof Double) {
			print_ob(out, (double) o);
		} else if(o instanceof double[]) {
			print_ob(out, (double[]) o);
		} else if(o instanceof Position) {
			print_ob(out, (Position) o);
		} else {
			out.print(o);
		}
	}

	public static void print(PrintStream out, Object... args) {
		for(Object o : args) {
			print_ob(out, o);
		}
	}

	public static void println(PrintStream out, Object... args) {
		print(out, args);
		out.println();
	}

	public static void debug(PrintStream out, Object... args) {
		if(Config.debug) {
			print(out, args);
		}
	}

	public static void debugln(PrintStream out, Object... args) {
		if(Config.debug) {
			println(out, args);
		}
	}

	public static void clearln(PrintStream out) {
		Utility.println(System.out, String.format("                                                                             %c[A", 0x1B));
	}

	public static int[] toArray(ArrayList<Integer> arr) {
		int[] out = new int[arr.size()];
		for(int i = 0; i < out.length; i++) {
			out[i] = arr.get(i);
		}
		return out;
	}

	public static int[][] toMatrix(ArrayList<int[]> arr) {
		int[][] out = new int[arr.size()][];
		for(int i = 0; i < out.length; i++) {
			out[i] = arr.get(i);
		}
		return out;
	}

	public static int[] removeDuplicates(int[] arr) {
		Set<Integer> seen = new HashSet<>();
		ArrayList<Integer> out = new ArrayList<>();

		for(int i : arr) {
			if(!seen.contains(i)) {
				out.add(i);
				seen.add(i);
			}
		}

		return toArray(out);
	}

	public static int[] argmax(int start, int end, IntToDoubleFunction f) {

		ArrayList<Integer> choice = new ArrayList<>();
		double best;

		best = Double.MIN_VALUE;
		for(int i = start; i < end; i++) {
			if(f.applyAsDouble(i) > best) {
				choice = new ArrayList<>();
				choice.add(i);
				best = f.applyAsDouble(i);
			} else if(f.applyAsDouble(i) == best) {
				choice.add(i);
			}
		}

		return Utility.toArray(choice);
	}

	public static boolean contains(int[] arr, int val) {
		for(int i : arr) {
			if(i == val) {
				return true;
			}
		}
		return false;
	}

	public static int pickFrom(double[] distribution) {
		double choice = Math.random();

		for(int i = 0; i < distribution.length; i++) {
			choice -= distribution[i];
			if(choice < 0) {
				return i;
			}
		}

		return -1;
	}

	public static double[] toDistribution(int[] arr) {
		double[] out = new double[arr.length];
		for(int i = 0; i < out.length; i++) {
			out[i] = arr[i];
		}
		return toDistribution(out);
	}

	public static double[] toDistribution(double[] arr) {
		double sum = 0;
		for(double d : arr) {
			sum += d;
		}

		double[] out = new double[arr.length];
		for(int i = 0; i < out.length; i++) {
			out[i] = arr[i] / sum;
		}
		return out;
	}

}

