
public class Policy {

	private double[][][][] Q;

	public final int state_count, action_count;
	private final Game base_game;

	private final int P1 = 0, P2 = 1;

	public Policy(Game game) {
		state_count = game.get_state_count();
		action_count = game.get_action_count();
		base_game = game;

		Q = new double[state_count][action_count][action_count][2];

		for(int state = 0; state < state_count; state++) {
			for(int p1_action = 0; p1_action < action_count; p1_action++) {
				for(int p2_action = 0; p2_action < action_count; p2_action++) {
					Q[state][p1_action][p2_action][P1] = 1;
					Q[state][p1_action][p2_action][P2] = 1;
				}
			}
		}
	}

	double[] Q_expectation(int state, int[][] actions) {

		double[] eval = new double[] { 0, 0 };
		int possibilities = actions[P1].length * actions[P2].length;

		for(int truck_action : actions[P1]) {
			for(int car_action : actions[P2]) {
				eval[P1] += Q[state][truck_action][car_action][P1];
				eval[P2] += Q[state][truck_action][car_action][P2];

			}
		}

		eval[P1] /= possibilities;
		eval[P2] /= possibilities;

		return eval;
	}

	public void train() {

		Game sim;
		double[] rewards;
		int[][] actions;
		double[] Q_evaluation;
		int new_state;

		double[][][][] Q_update = new double[state_count][action_count][action_count][2];

		double max_change;

		for(int iteration = 0; iteration < Config.Q_iterations; iteration++) {
			Q_update = new double[state_count][action_count][action_count][2];

			max_change = Double.MIN_VALUE;

			for(int state = 0; state < state_count; state++) {

				for(int p1_action = 0; p1_action < action_count; p1_action++) {
					for(int p2_action = 0; p2_action < action_count; p2_action++) {

						sim = base_game.get_copy(state);
						rewards = sim.update(p1_action, p2_action);

						new_state = sim.get_state();
						actions = get_action_options(new_state);
						Q_evaluation = Q_expectation(new_state, actions);

						Q_update[state][p1_action][p2_action][P1] = rewards[P1] + Config.Beta * Q_evaluation[P1];
						Q_update[state][p1_action][p2_action][P2] = rewards[P2] + Config.Beta * Q_evaluation[P2];

						max_change = Math.max(
							max_change, 
							Math.abs(Q_update[state][p1_action][p2_action][P1] - Q[state][p1_action][p2_action][P1])
						);
						max_change = Math.max(
							max_change, 
							Math.abs(Q_update[state][p1_action][p2_action][P2] - Q[state][p1_action][p2_action][P2])
						);

					}
				}

			}

			Q = Q_update;
			System.out.println("Largest update: " + max_change);
		}

	}

	public int[][] get_action_options(int state) {
		return FictitiousPlay.evaluate_state(Q[state], action_count);
	}

	public int[] evaluate(int state) {
		return FictitiousPlay.evaluate_options(get_action_options(state));
	}

}

