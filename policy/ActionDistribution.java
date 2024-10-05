package policy;

import java.util.HashMap;
import java.util.Map;

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
		if(count == 0) {

		}
		return (double) distribution.get(action) / count;
	}

}

