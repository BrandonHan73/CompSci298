package environment;

import base.State;

public abstract class Game {

	protected State state;
	protected final int player_count;
	protected Enum[][] actions;

	public Game(int players, Enum[]... action_choices) {
		player_count = players;

		actions = new Enum[player_count][];
		for(int player = 0; player < player_count; player++) {
			actions[player] = action_choices[player];
		}
	}

	public int player_count() {
		return player_count;
	}

	public Game get_copy() {
		return get_copy(state);
	}

	public Enum[] get_possible_actions(int player) {
		return actions[player];
	}

	public State get_state() {
		return state;
	}

	public abstract Game get_copy(State state);
	public abstract Game get_random_copy();

	public abstract double[] update(ActionSet as);

}

