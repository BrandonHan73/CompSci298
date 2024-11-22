package policy;

import environment.Game;
import environment.ActionSet;
import base.Config;
import base.State;
import policy.NashSolver;

public abstract class Policy {

	private State[] possible_states;

	private final Game base_game;

	public Policy(Game base) {
		possible_states = null;
		base_game = base;
	}

	public Game get_base_copy(State s) {
		return base_game.get_copy(s);
	}

	public Game get_randomized() {
		base_game.randomize();
		return base_game;
	}

	public void load_possible_states(Game game) {
		possible_states = game.get_possible_states();
	}

	public void load_possible_states() {
		load_possible_states(base_game);
	}

	public State[] get_possible_states() {
		if(possible_states == null) {
			load_possible_states();
		}
		return possible_states;
	}

	public void train() {
		train(Config.Q_iterations);
	}

	public abstract void train(int iterations);
	public abstract ActionDistribution[] get_action_options(State state);

	public ActionSet evaluate(State state) {
		return NashSolver.evaluate_options(get_action_options(state));
	}

}

