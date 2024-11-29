package util;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.IntToDoubleFunction;

import environment.*;
import base.*;

public class Utility {

	public static double logistic(double x) {
		return 1 / (1 + Math.exp(-x));
	}

	public static void clearln() {
		Print.clearln(System.out);
	}

	public static void debugln(Object... args) {
		Print.debugln(System.out, args);
	}

	public static void debug(Object... args) {
		Print.debug(System.out, args);
	}

	public static void println(Object... args) {
		Print.println(System.out, args);
	}

	public static void print(Object... args) {
		Print.print(System.out, args);
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

	public static ActionSet[] toArray(Set<ActionSet> set) {
		ActionSet[] out = new ActionSet[set.size()];
		int i = 0;
		for(ActionSet as : set) {
			out[i++] = as;
		}
		return out;
	}

	public static int[] copy(int[] arr) {
		if(arr == null) return null;
		int[] out = new int[arr.length];
		for(int i = 0; i < out.length; i++) {
			out[i] = arr[i];
		}
		return out;
	}

	public static <T extends Enum<T>> Enum<T>[] copy(Enum<T>[] arr) {
		if(arr == null) return null;
		Enum<T>[] out = new Enum[arr.length];
		for(int i = 0; i < arr.length; i++) {
			out[i] = arr[i];
		}
		return out;
	}

	public static Set<Integer> toTreeSet(int... vals) {
		Set<Integer> out = new TreeSet<>();
		for(int v : vals) {
			out.add(v);
		}
		return out;
	}

	public static int[][] copy(int[][] arr) {
		if(arr == null) return null;
		int[][] out = new int[arr.length][];
		for(int i = 0; i < out.length; i++) {
			out[i] = copy(arr[i]);
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

	public static Set<Integer> argmax(Set<Integer> options, IntToDoubleFunction f) {
		Set<Integer> choice = new HashSet<>();
		double best;

		Map<Integer, Double> eval = new HashMap<>();
		for(int i : options) {
			eval.put(i, f.applyAsDouble(i));
		}

		best = -Double.MAX_VALUE;
		double result;
		for(int i : options) {
			result = eval.get(i);
			if(result > best) {
				choice = new HashSet<>();
				choice.add(i);
				best = result;
			} else if(result == best) {
				choice.add(i);
			}
		}

		if(options.size() > 0 && choice.size() == 0) {
			throw new RuntimeException("Argmax produced no results");
		}

		return choice;
	}

	public static int[] argmax(int start, int end, IntToDoubleFunction f) {
		if(start == end) {
			return new int[] {};
		}

		int temp = start;
		start = Math.min(temp, end);
		end = Math.max(temp, end);

		ArrayList<Integer> choice = new ArrayList<>();
		double best;

		double[] eval = new double[end - start];
		for(int i = start; i < end; i++) {
			eval[i - start] = f.applyAsDouble(i);
		}

		best = eval[0];
		choice.add(0);

		for(int i = start + 1; i < end; i++) {
			if(eval[i - start] > best) {
				choice = new ArrayList<>();
				choice.add(i);
				best = eval[i - start];
			} else if(eval[i - start] == best) {
				choice.add(i);
			}
		}

		if(choice.size() == 0) {
			throw new RuntimeException("Argmax produced no results");
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

	public static interface IntArrToVoid {
		public void run(int[] arr);
	}

	public static void forEachChoice(Set<Integer>[] choices, IntArrToVoid action, int... skip) {
		int[] current = new int[choices.length];
		Set<Integer> skip_set = new HashSet<>();

		for(int i : skip) {
			skip_set.add(i);
		}

		forEachChoice(choices, action, 0, current, skip_set);
	}

	private static void forEachChoice(Set<Integer>[] choices, IntArrToVoid action, int start_index, int[] current, Set<Integer> skip) {
		if(start_index > choices.length) {
			throw new RuntimeException("Something went wrong with forEachChoice...");
		}

		if(skip.contains(start_index)) {
			current[start_index] = Config.placeholder_action;
			forEachChoice(choices, action, start_index + 1, current, skip);
			return;
		}

		if(start_index == choices.length) {
			action.run(current);
			return;
		}

		for(int i : choices[start_index]) {
			current[start_index] = i;
			forEachChoice(choices, action, start_index + 1, current, skip);
		}
	}

	public static void forEachChoice(int[][] choices, IntArrToVoid action) {
		int count = choices.length;
		int choice_count, index;

		int[] indices = new int[count];

		int[] pick = new int[count];
		while(true) {

			for(int i = 0; i < count; i++) {
				pick[i] = choices[i][indices[i]];
			}

			action.run(pick);

			index = 0;
			do {
				choice_count = choices[index].length;
				indices[index]++;

				if(indices[index] < choice_count) {
					break;
				} else {
					indices[index] = 0;
					index++;
					if(index < count) {
						continue;
					} else {
						return;
					}
				}
			} while(true);
		}
	}

	public static double[] createDoubleArray(int n, double val) {
		double[] out = new double[n];
		for(int i = 0; i < n; i++) {
			out[i] = val;
		}
		return out;
	}

}

