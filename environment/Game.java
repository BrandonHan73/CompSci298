package environment;

import java.util.Set;

import base.State;

public abstract class Game {

	public abstract Game get_copy(State state);
	public State[] get_possible_states() {
		return null;
	}
	public abstract Set<Integer>[] get_possible_actions();

	public int player_count() {
		return get_possible_actions().length;
	}

	public abstract double[] update(ActionSet as);

	public abstract State get_state();
	public void randomize() {}

}

