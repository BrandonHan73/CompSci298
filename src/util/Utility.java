package util;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.IntToDoubleFunction;
import java.util.function.ToDoubleFunction;

public class Utility {

	public static double logistic(double x) {
		double exp = Math.exp(-x);

		if(Double.isNaN(exp)) {
			return 0;
		}

		double out = 1 / (1 + exp);
		if(0 <= out && out <= 1) {
			return out;
		}

		throw new RuntimeException("Logistic function produced inaccurate result");
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

	public static <T> T[] toArray(List<T> arr, Class<T> c) {
		if(arr == null) return null;
		T[] out = (T[]) Array.newInstance(c, arr.size());

		arr.toArray(out);

		return (T[]) out;
	}

	public static <T> T[] toArray(Set<T> set, Class<T> c) {
		if(set == null) return null;
		List<T> arr = new ArrayList<>();
		arr.addAll(set);
		return toArray(arr, c);
	}

	public static Enum[] copyEnum(Enum[] arr) {
		if(arr == null) return null;
		Enum[] out = new Enum[arr.length];

		for(int i = 0; i < arr.length; i++) {
			out[i] = arr[i];
		}

		return out;
	}

	public static <T> T[] copyObj(T[] arr) {
		if(arr == null) return null;
		Object[] out = new Object[arr.length];

		for(int i = 0; i < arr.length; i++) {
			out[i] = arr[i];
		}

		return (T[]) out;
	}

	public static double[] copyDoubleArr(double[] arr) {
		if(arr == null) return null;
		double[] out = new double[arr.length];

		for(int i = 0; i < arr.length; i++) {
			out[i] = arr[i];
		}

		return out;
	}

	public static int[] copyIntArr(int[] arr) {
		if(arr == null) return null;
		int[] out = new int[arr.length];

		for(int i = 0; i < arr.length; i++) {
			out[i] = arr[i];
		}

		return out;
	}

	public static int[][] copyIntMat(int[][] arr) {
		if(arr == null) return null;
		int[][] out = new int[arr.length][];
		for(int i = 0; i < out.length; i++) {
			out[i] = copyIntArr(arr[i]);
		}
		return out;
	}

	public static <T> Set<T> argmax(T[] options, ToDoubleFunction f) {
		Set<T> choice = new HashSet<>();
		double best;

		Map<T, Double> eval = new HashMap<>();
		for(T i : options) {
			eval.put(i, f.applyAsDouble(i));
		}

		best = -Double.MAX_VALUE;
		double result;
		for(T i : options) {
			result = eval.get(i);
			if(result > best) {
				choice = new HashSet<>();
				choice.add(i);
				best = result;
			} else if(result == best) {
				choice.add(i);
			}
		}

		if(options.length > 0 && choice.size() == 0) {
			throw new RuntimeException("Argmax produced no results");
		}

		return choice;
	}

	public static int[] toPrimitiveArray(Integer[] arr) {
		int[] out = new int[arr.length];
		for(int i = 0; i < arr.length; i++) {
			out[i] = arr[i];
		}
		return out;
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

		return toPrimitiveArray(toArray(choice, Integer.class));
	}

	public static interface EnumArrToVoid {
		public void run(Enum[] arr);
	}

	public static void forEachChoice(Enum[][] choices, EnumArrToVoid action, int... actors_to_skip) {
		int actor_count = choices.length;

		int[] sizes = new int[actor_count];
		for(int actor = 0; actor < actor_count; actor++) {
			sizes[actor] = choices[actor].length;
		}
		for(int actor : actors_to_skip) {
			sizes[actor] = 1;
		}

		int[] indices = new int[actor_count];
		Enum[] arr = new Enum[actor_count];
		while(indices[actor_count - 1] < sizes[actor_count - 1]) {
			for(int actor = 0; actor < actor_count; actor++) {
				arr[actor] = choices[actor][ indices[actor] ];
			}
			action.run(copyEnum(arr));

			indices[0]++;
			for(int actor = 0; actor < actor_count - 1; actor++) {
				if(indices[actor] == sizes[actor]) {
					indices[actor] = 0;
					indices[actor + 1]++;
				}
			}
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

