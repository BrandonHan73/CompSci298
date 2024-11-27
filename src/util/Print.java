package util;

import java.io.PrintStream;

import base.Config;

public class Print {

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
		out.print(String.format("%.8f", d));
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
		println(System.out, String.format("                                                                             %c[A", 0x1B));
	}

}

