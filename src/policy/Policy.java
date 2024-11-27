package policy;

import environment.*;
import base.*;

public abstract class Policy {

	protected final Game base_game;

	public Policy(Game base) {
		base_game = base;
	}

	protected Game get_base_copy(State s) {
		return base_game.get_copy(s);
	}

	protected Game get_randomized() {
		return base_game.get_random_copy();
	}

	public void train() {
		train(Config.Q_iterations);
	}
	public void train(int iterations) {
		for(int i = 0; i < iterations; i++) train_step();
	}
	public abstract void train_step();

	public abstract ActionDistribution[] evaluate(State state);

	public ActionSet poll(State state) {
		return NashSolver.evaluate_options(evaluate(state));
	}

}

