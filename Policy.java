
public class Policy {

	private StateQ[] Q;

	public final int state_count, action_count;
	private final Game base_game;

	private final int P1 = 0, P2 = 1;

	public Policy(Game game) {
		state_count = game.get_state_count();
		action_count = game.get_action_count();
		base_game = game;

		Q = new StateQ[state_count];

		for(int state = 0; state < state_count; state++) {
			Q[state] = new StateQ(action_count);
		}
	}

	double[] Q_expectation(int state, int[][] actions) {

		double[] eval = new double[] { 0, 0 };
		int possibilities = actions[P1].length * actions[P2].length;

		for(int truck_action : actions[P1]) {
			for(int car_action : actions[P2]) {
				eval[P1] += Q[state].get(truck_action, car_action, P1);
				eval[P2] += Q[state].get(truck_action, car_action, P2);

			}
		}

		eval[P1] /= possibilities;
		eval[P2] /= possibilities;

		return eval;
	}

	public void train() {
		train(Config.Q_iterations);
	}

	public void train(int iterations) {

		Game sim;
		double[] rewards;
		int[][] actions;
		double[] Q_evaluation;
		int new_state;

		StateQ[] Q_update;

		double max_change;

		for(int iteration = 0; iteration < iterations; iteration++) {
			Q_update = new StateQ[state_count];

			max_change = Double.MIN_VALUE;

			for(int state = 0; state < state_count; state++) {
				Q_update[state] = new StateQ(action_count);

				for(int p1_action = 0; p1_action < action_count; p1_action++) {
					for(int p2_action = 0; p2_action < action_count; p2_action++) {

						sim = base_game.get_copy(state);
						rewards = sim.update(p1_action, p2_action);

						new_state = sim.get_state();
						actions = get_action_options(new_state);
						Q_evaluation = Q_expectation(new_state, actions);

						Q_update[state].set(rewards[P1] + Config.Beta * Q_evaluation[P1], p1_action, p2_action, P1);
						Q_update[state].set(rewards[P2] + Config.Beta * Q_evaluation[P2], p1_action, p2_action, P2);

						max_change = Math.max(
							max_change, 
							Math.abs(Q_update[state].get(p1_action, p2_action, P1) - Q[state].get(p1_action, p2_action, P1))
						);
						max_change = Math.max(
							max_change, 
							Math.abs(Q_update[state].get(p1_action, p2_action, P2) - Q[state].get(p1_action, p2_action, P2))
						);

					}
				}

			}

			Q = Q_update;
			Utility.println(System.out, "Largest update: " + max_change);
		}

	}

	public int[][] get_action_options(int state) {
		return NashSolver.evaluate_state(Q[state], action_count);
	}

	public int[] evaluate(int state) {
		return NashSolver.evaluate_options(get_action_options(state));
	}

}

