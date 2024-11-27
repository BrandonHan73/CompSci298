package example;

import java.util.TreeSet;
import java.util.Set;

import base.*;
import environment.*;

public class RockPaperScissors extends Game {

	public RockPaperScissors() {
		super(2);
		actions = new Set[2];
		actions[0] = new TreeSet<>(Set.of(0, 1, 2));
		actions[1] = new TreeSet<>(Set.of(0, 1, 2));
		state = new State(this);
	}

	@Override
	public Game get_copy(State state) { return new RockPaperScissors(); }
	@Override
	public Game get_random_copy() { return new RockPaperScissors(); }

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

}

