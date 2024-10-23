package base;

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

	public int[][] choices() {
		return actions;
	}

	public int player_count() {
		return actions.length;
	}

	public int[] action_counts() {
		int[] count = new int[actions.length];
		for(int i = 0; i < count.length; i++) {
			count[i] = actions[i].length;
		}

		return count;
	}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof State)) return false;

		return ((State) o).value == value;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(value);
	}

}

