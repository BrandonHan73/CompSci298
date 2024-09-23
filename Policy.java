
import java.util.ArrayList;

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

	private ArrayList<Integer>[][] best_response(int state) {
		ArrayList<Integer>[][] response = new ArrayList[2][action_count];
		double best_reward, reward;

		for(int p2_action = 0; p2_action < action_count; p2_action++) {

			best_reward = Double.MIN_VALUE;
			response[P1][p2_action] = new ArrayList<>();
			for(int p1_action = 0; p1_action < action_count; p1_action++) {

				reward = Q[state][p1_action][p2_action][P1];

				if(reward > best_reward) {
					best_reward = reward;
					response[P1][p2_action] = new ArrayList<>();
					response[P1][p2_action].add(p1_action);
				} else if(reward == best_reward) {
					response[P1][p2_action].add(p1_action);
				}
			}

		}

		for(int p1_action = 0; p1_action < action_count; p1_action++) {

			best_reward = Double.MIN_VALUE;
			response[P2][p1_action] = new ArrayList<>();
			for(int p2_action = 0; p2_action < action_count; p2_action++) {

				reward = Q[state][p1_action][p2_action][P2];

				if(reward > best_reward) {
					best_reward = reward;
					response[P2][p1_action] = new ArrayList<>();
					response[P2][p1_action].add(p2_action);
				} else if(reward == best_reward) {
					response[P2][p1_action].add(p2_action);
				}
			}

		}

		return response;
	}

	private int[][] pick_most_common(double[][] action_counts) {

		ArrayList<Integer> choice = new ArrayList<>();
		int[][] result = new int[2][];
		double best;

		best = Double.MIN_VALUE;
		for(int p1_action = 0; p1_action < action_count; p1_action++) {
			if(action_counts[P1][p1_action] > best) {
				choice = new ArrayList<>();
				choice.add(p1_action);
				best = action_counts[P1][p1_action];
			} else if(action_counts[P1][p1_action] == best) {
				choice.add(p1_action);
			}
		}
		result[P1] = new int[choice.size()];
		for(int action = 0; action < choice.size(); action++) {
			result[P1][action] = choice.get(action);
		}

		best = Double.MIN_VALUE;
		for(int p2_action = 0; p2_action < action_count; p2_action++) {
			if(action_counts[P2][p2_action] > best) {
				choice = new ArrayList<>();
				choice.add(p2_action);
				best = action_counts[P2][p2_action];
			} else if(action_counts[P2][p2_action] == best) {
				choice.add(p2_action);
			}
		}
		result[P2] = new int[choice.size()];
		for(int action = 0; action < choice.size(); action++) {
			result[P2][action] = choice.get(action);
		}

		return result;
	}

	public int[][] fictitious_play(int state) {

		ArrayList<Integer>[][] response = best_response(state);

		double[][] action_counts = new double[2][action_count];
		for(int action = 0; action < action_count; action++) {
			action_counts[P1][action] = 1.0 / action_count;
			action_counts[P2][action] = 1.0 / action_count;
		}

		double[][] reaction = new double[2][];

		for(int moves = 1; moves < Config.fictitious_play_iterations; moves++) {

			reaction[P1] = new double[action_count];
			for(int p2_action = 0; p2_action < action_count; p2_action++) {
				for(int p1_action : response[P1][p2_action]) {
					reaction[P1][p1_action] += action_counts[P2][p2_action] / moves / response[P1][p2_action].size();
				}
			}

			reaction[P2] = new double[action_count];
			for(int p1_action = 0; p1_action < action_count; p1_action++) {
				for(int p2_action : response[P2][p1_action]) {
					reaction[P2][p2_action] += action_counts[P1][p1_action] / moves / response[P2][p1_action].size();
				}
			}

			for(int action = 0; action < action_count; action++) {
				action_counts[P1][action] += reaction[P1][action];
				action_counts[P2][action] += reaction[P2][action];
			}

		}

		return pick_most_common(action_counts);

	}

	double[] Q_expectation(int state, int[][] actions) {

		double[] eval = new double[] { 0, 0 };
		int possibilities = actions[P1].length * actions[P2].length;

		for(int truck_action : actions[P1]) {
			for(int car_action : actions[P2]) {
				eval[P1] += Q[state][truck_action][car_action][P1] / possibilities;
				eval[P2] += Q[state][truck_action][car_action][P2] / possibilities;
			}
		}

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
						actions = fictitious_play(new_state);
						Q_evaluation = Q_expectation(new_state, actions);

						Q_update[state][p1_action][p2_action][P1] = rewards[P1] + Config.Beta * Q_evaluation[P1];
						Q_update[state][p1_action][p2_action][P2] = rewards[P2] + Config.Beta * Q_evaluation[P2];

						max_change = Math.max(max_change, Math.abs(Q_update[state][p1_action][p2_action][P1] - Q[state][p1_action][p2_action][P1]));
						max_change = Math.max(max_change, Math.abs(Q_update[state][p2_action][p2_action][P2] - Q[state][p2_action][p2_action][P2]));

					}
				}

			}

			Q = Q_update;
			System.out.println("Largest update: " + max_change);
		}

	}

	int[] evaluate(int[][] actions) {
		int p1_action = actions[P1][ (int) (Math.random() * actions[P1].length) ];
		int p2_action = actions[P2][ (int) (Math.random() * actions[P2].length) ];

		return new int[] { p1_action, p2_action };
	}

	int[] evaluate(int state) {
		return evaluate(fictitious_play(state));
	}

}

