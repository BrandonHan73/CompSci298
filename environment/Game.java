package environment;

import base.State;

public abstract class Game {

	public abstract Game get_copy(State state);
	public State[] get_possible_states() {
		return null;
	}

	public abstract double[] update(ActionSet as);

	public abstract State get_state();
	public void randomize() {}

}

