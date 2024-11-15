package base;

import java.util.Set;
import java.util.TreeSet;

public class State {

	private final int state_code;

	private final Set<Integer>[] action_choices;

	public State() {
		this(0);
	}

	public State(int code) {
		this(code, null);
	}

	public State(int[][] p_actions) {
		this(0, new Set[p_actions.length]);

		for(int player = 0; player < p_actions.length; player++) {
			action_choices[player] = new TreeSet<>();
			for(int action : p_actions[player]) {
				action_choices[player].add(action);
			}
		}
	}

	public State(Set<Integer>[] p_actions) {
		this(0, p_actions);
	}

	public State(int code, Set<Integer>[] p_actions) {
		state_code = code;
		action_choices = p_actions;
	}

	public Set<Integer> choices_for(int player) {
		ensure_action_choices_provided();
		return action_choices[player];
	}

	public Set<Integer>[] choices() {
		ensure_action_choices_provided();
		return action_choices;
	}

	public int player_count() {
		ensure_action_choices_provided();
		return action_choices.length;
	}

	public void ensure_action_choices_provided() {
		if(action_choices == null) {
			throw new RuntimeException("Player action choices not specified. ");
		}
	}

	public double[] to_double_array() {
		return new double[] { state_code };
	}
	public int parameter_count() {
		return 1;
	}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof State)) return false;

		return ((State) o).state_code == state_code;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(state_code);
	}

}

