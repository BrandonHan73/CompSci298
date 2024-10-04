package policy;

import java.util.HashMap;
import java.util.Map;

public class ActionDistribution {

	private Map<Integer, Integer> distribution;
	private int count;

	public ActionDistribution() {
		distribution = new HashMap<>();
		count = 0;
	}

	public void add(int action) {
		count++;
		if(distribution.containsKey(action)) {
			distribution.put(action, distribution.get(action) + 1);
		} else {
			distribution.put(action, 1);
		}
	}

	public double get(int action) {
		return (double) distribution.get(action) / count;
	}

}

