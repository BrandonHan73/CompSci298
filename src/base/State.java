package base;

import environment.Game;

public class State {

	private final int state_code;

	protected final Game base;

	public State(Game base) {
		this(0, base);
	}

	public State(int code, Game game) {
		state_code = code;
		base = game;
	}

	public State get_copy() {
		State out = new State(state_code, base);
		return out;
	}

	public Enum[] choices_for(int player) {
		return base.get_possible_actions(player);
	}

	public int player_count() {
		return base.player_count();
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

