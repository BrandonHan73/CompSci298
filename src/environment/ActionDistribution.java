package environment;

import java.util.EnumMap;
import java.util.Map;

public class ActionDistribution <E extends Enum<E>> {

	private Map<E, Double> distribution;
	private double count;

	private Class<E> key_type;
	private E[] options;

	public ActionDistribution(Class<E> keyType, E[] values) {
		distribution = new EnumMap<>(keyType);
		count = 0;

		key_type = keyType;
		options = values;
	}

	public ActionDistribution<E> copy() {
		ActionDistribution<E> out = new ActionDistribution<>(key_type, options);

		for(E a : options) {
			out.distribution.put( a, distribution.get(a) );
		}
		out.count = count;

		return out;
	}

	public void add(E action, double val) {
		count += val;
		if(distribution.containsKey(action)) {
			distribution.put(action, distribution.get(action) + val);
		} else {
			distribution.put(action, val);
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
		if(!distribution.containsKey(action)) {
			return 0;
		}
		return distribution.get(action) / count;
	}

	public E poll() {
		if(count == 0) {
			return options[ (int) (options.length * Math.random()) ];
		}

		double choice = count * Math.random();

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

