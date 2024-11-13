package policy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import base.Config;
import base.Utility;

public class ActionDistribution {

	private Map<Integer, Double> distribution;
	private double count;

	public InitializationOption init_option;
	private boolean used;

	public enum InitializationOption {
		EMPTY, RANDOM, EVEN, SPECIFIC
	}

	public ActionDistribution(InitializationOption init, Set<Integer> choices, int def) {
		distribution = new HashMap<>();
		count = 0;
		used = false;

		double temp;
		switch(init) {
			case EMPTY: break;
			case RANDOM: 

			if(choices == null) {
				throw new RuntimeException("Choices are required to make a random distribution");
			}

			for(int action : choices) {
				temp = Math.random();
				distribution.put(action, temp);
				count += temp;
			}

			break;

			case EVEN:

			if(choices == null) {
				throw new RuntimeException("Choices are required to make an even distribution");
			}

			for(int action : choices) {
				distribution.put(action, 1.0);
			}
			count = choices.size();

			break;

			case SPECIFIC:

			distribution.put(def, 1.0);
			count = 1;

			default:
			throw new RuntimeException("Invalid initialization type has been provided");
		}
	}

	public ActionDistribution() {
		this(InitializationOption.EMPTY, null, Config.placeholder_action);
	}

	public ActionDistribution(Set<Integer> choices, boolean random) {
		this(
			random ? InitializationOption.RANDOM : InitializationOption.EVEN,
			choices,
			Config.placeholder_action
		);
	}

	public ActionDistribution(Set<Integer> choices) {
		this(choices, false);
	}

	public ActionDistribution(int action) {
		this(InitializationOption.SPECIFIC, null, action);
	}

	public ActionDistribution(ActionDistribution o) {
		this();

		count = o.count;
		for(int action : o.distribution.keySet()) {
			distribution.put(action, o.distribution.get(action));
		}
	}

	public ActionDistribution copy() {
		return new ActionDistribution(this);
	}

	public void add(int action, double val) {
		if(!used) {
			used = true;
			distribution = new HashMap<>();
			count = 0;
		}
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

	public void add(int action) {
		add(action, 1);
	}

	public double get(int action) {
		if(count == 0 || !distribution.containsKey(action)) {
			return 0;
		}
		return distribution.get(action) / count;
	}

	public int[] choices() {
		int[] out = new int[distribution.size()];
		int i = 0;
		for(int action : distribution.keySet()) {
			out[i++] = action;
		}
		return out;
	}

	public int poll() {
		double choice = count * Math.random();

		for(int action : distribution.keySet()) {
			choice -= distribution.get(action);

			if(choice < 0) {
				return action;
			}
		}

		return -1;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for(int action : distribution.keySet()) {
			sb.append(String.format("(%d)%.4f ", action, get(action)));
		}

		return sb.toString();
	}

}

