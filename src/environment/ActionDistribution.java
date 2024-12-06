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

	private double epsilon;

	private E[] options;

	public ActionDistribution(E[] values) {
		if(values.length <= 0) {
			throw new RuntimeException("Action distribution requires at least one possible action");
		}
		distribution = new double[values.length];
		count = 0;

		added_values = new LinkedList<>();
		added_actions = new LinkedList<>();

		epsilon = 0;

		options = values;
	}

	public void set_epsilon(double v) {
		epsilon = Math.min(
			1, Math.max(0, v)
		);
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
		if(val >= 0) {
			count += val;
			distribution[action.ordinal()] += val;

			added_actions.add(action);
			added_values.add(val);

			while(count > Config.action_distribution_max_count) {
				double remove = added_values.poll();

				distribution[ added_actions.poll().ordinal() ] -= remove;
				count -= remove;
			}
		} else {
			throw new RuntimeException("Invalid number provided (" + val + ")");
		}
	}

	public double count() {
		return count;
	}

	public void add(E action) {
		add(action, 1);
	}

	public double get(E action) {
		double even = 1.0 / options.length;
		double standard, out;
		if(count == 0) {
			out = even;
		} else {
			standard = distribution[action.ordinal()] / count;
			out = (epsilon * even) + ( (1 - epsilon) * standard );
		}

		if(0 <= out && out <= 1) {
			return out;
		}

		throw new RuntimeException("Action distribution provided improper probability (" + out + ", " + count + ")");
	}

	public E poll() {
		double choice = 0;
		for(E action : options) {
			choice += get(action);
		}
		if(Math.abs(choice - 1.0) > Config.probability_distribution_tolerance) {
			throw new RuntimeException("Probability distribution is not valid (" + choice + ")");
		}

		choice = Math.random();
		for(E action : options) {
			choice -= get(action);

			if(choice < 0) {
				return action;
			}
		}

		throw new RuntimeException("Action distribution polling did not execute correctly (" + choice + ")");
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

