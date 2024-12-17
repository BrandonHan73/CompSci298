package policy;

import base.*;
import environment.*;

public abstract class AllStatesDefinedPolicy extends Q_Policy {

	protected State[] possible_states;

	public AllStatesDefinedPolicy(Game base) {
		super(base);
	}

	public abstract void load_possible_states();

	public State[] get_possible_states() {
		if(possible_states == null) {
			load_possible_states();
		}
		return possible_states;
	}

	@Override
	public void train_step() {
		State[] possible_states = get_possible_states();
		int[] indices = new int[possible_states.length];
		for(int i = 0; i < indices.length; i++) {
			indices[i] = i;
		}
		for(int i = indices.length - 1; i > 0; i--) {
			int swap = (int) (i * Math.random());
			int temp = indices[swap];
			indices[swap] = indices[i];
			indices[i] = temp;
		}

		for(int index : indices) {
			State state = possible_states[index];
			train_with_state(state);
		}
	}

	public abstract void train_with_state(State state);

}

