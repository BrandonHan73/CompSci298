
public class State {

	private final int value;

	private final int[][] actions;

	public State() {
		this(0);
	}

	public State(int val) {
		this(val, new int[0][0]);
	}

	public State(int[][] p_actions) {
		this(0, p_actions);
	}

	public State(int val, int[][] p_actions) {
		value = val;
		actions = p_actions;
	}

	public int[] choices_for(int player) {
		return actions[player];
	}

	public int[] action_counts() {
		int[] count = new int[actions.length];
		for(int i = 0; i < count.length; i++) {
			count[i] = actions[i].length;
		}

		return count;
	}

	public int action_count() {
		return action_counts()[0];
	}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof State)) return false;

		return ((State) o).value == value;
	}

	@Override
	public int hashCode() {
		return value;
	}

}

