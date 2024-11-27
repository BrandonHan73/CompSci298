package environment;

import java.util.Set;

import base.State;

public abstract class Game {

	protected State state;
	protected final int player_count;
	protected Set<Integer>[] actions;

	public Game(int players) {
		player_count = players;
	}

	public int player_count() {
		return player_count;
	}

	public Game get_copy() {
		return get_copy(state);
	}

	public Set<Integer>[] get_possible_actions() {
		return actions;
	}

	public State get_state() {
		return state;
	}

	public abstract Game get_copy(State state);
	public abstract Game get_random_copy();

	public abstract double[] update(ActionSet as);

}

