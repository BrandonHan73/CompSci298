package environment;

import java.util.EnumMap;
import java.util.Map;

public class ActionDistribution <E extends Enum<E>> {

	private double[] distribution;
	private double count;

	private E[] options;

	public ActionDistribution(E[] values) {
		if(values.length <= 0) {
			throw new RuntimeException("Action distribution requires at least one possible action");
		}
		distribution = new double[values.length];
		count = 0;

		options = values;
	}

	public ActionDistribution<E> copy() {
		ActionDistribution<E> out = new ActionDistribution<>(options);

		for(E a : options) {
			out.distribution[a.ordinal()] = distribution[a.ordinal()];
		}
		out.count = count;

		return out;
	}

	public void add(E action, double val) {
		count += val;
		distribution[action.ordinal()] += val;
	}

	public double count() {
		return count;
	}

	public void add(E action) {
		add(action, 1);
	}

	public double get(E action) {
		if(count == 0) {
			return 1.0 / options.length;
		}
		return distribution[action.ordinal()] / count;
	}

	public E poll() {
		if(count == 0) {
			return options[ (int) (options.length * Math.random()) ];
		}

		double choice = Math.random();

		for(E action : options) {
			choice -= get(action);

			if(choice < 0) {
				return action;
			}
		}

		throw new RuntimeException("Action distribution polling did not execute correctly");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for(E action : options) {
			sb.append(String.format("(" + action + ")%.4f ", get(action)));
		}

		return sb.toString();
	}

}

