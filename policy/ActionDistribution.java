package policy;

import java.util.HashMap;
import java.util.Map;

import base.Utility;

public class ActionDistribution {

	private Map<Integer, Double> distribution;
	private double count;

	public ActionDistribution() {
		distribution = new HashMap<>();
		count = 0;
	}

	public void add(int action, double val) {
		count += val;
		if(distribution.containsKey(action)) {
			distribution.put(action, distribution.get(action) + val);
		} else {
			distribution.put(action, val);
		}
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

}

