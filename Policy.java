
public abstract class Policy {

	private final Game base_game;

	public Policy(Game base) {
		base_game = base;
	}

	public Game get_base_copy(State s) {
		return base_game.get_copy(s);
	}

	public State[] get_possible_states() {
		return base_game.get_possible_states();
	}

	public void train() {
		train(Config.Q_iterations);
	}

	public abstract void train(int iterations);
	public abstract double[][] get_action_options(State state);

	public int[] evaluate(State state) {
		return NashSolver.evaluate_options(get_action_options(state));
	}

}

