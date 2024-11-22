package environment;

import java.util.TreeSet;
import java.util.Set;

import base.State;

public class RockPaperScissors extends Game {

	private State state;
	private Set<Integer>[] actions;

	public RockPaperScissors() {
		actions = new Set[2];
		actions[0] = new TreeSet<>(Set.of(0, 1, 2));
		actions[1] = new TreeSet<>(Set.of(0, 1, 2));
		state = new State(this);
	}

	@Override
	public State[] get_possible_states() {
		return new State[] { state };
	}

	@Override
	public Set<Integer>[] get_possible_actions() {
		return actions;
	}

	public Game get_copy(State state) { return new RockPaperScissors(); }

	public double[] update(int p1_action, int p2_action) {

		double[] rewards = new double[] { 0, 0 };

		int diff = (p1_action - p2_action + 3) % 3;

		switch(diff) {
			case 1:
			rewards[1] += 1;
			break;
			case 2:
			rewards[0] += 1;
			break;
			default:
		}

		return rewards;

	}

	@Override
	public double[] update(ActionSet as) {
		return update(as.get(0), as.get(1));
	}

	public State get_state() { return state; }

}

