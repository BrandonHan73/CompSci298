package environment;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import base.Config;

public class ActionDistribution <E extends Enum<E>> {

	private double[] distribution;
	private double count;

	private Queue<Double> added_values;
	private Queue<Enum> added_actions;

	private E[] options;

	public ActionDistribution(E[] values) {
		if(values.length <= 0) {
			throw new RuntimeException("Action distribution requires at least one possible action");
		}
		distribution = new double[values.length];
		count = 0;

		added_values = new LinkedList<>();
		added_actions = new LinkedList<>();

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

		added_actions.add(action);
		added_values.add(val);

		while(count > Config.action_distribution_max_count) {
			double remove = added_values.poll();

			distribution[ added_actions.poll().ordinal() ] -= remove;
			count -= remove;
		}
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

