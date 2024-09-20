import java.util.ArrayList;

public class CrashGamePolicy {

	private double[][][][] Q;

	private final int TRUCK = 0, CAR = 1;

	public final int state_count, action_count = 4;

	private final CrashGame base_game;

	private final int fictitious_play_iterations = 256;
	private final int Q_iterations = 1024;
	private final double Beta = 0.6;

	public CrashGamePolicy(CrashGame game) {
		state_count = (int) Math.pow(game.rows, 2) * (int) Math.pow(game.cols, 2);
		base_game = game;

		Q = new double[state_count][action_count][action_count][2];

		for(int state = 0; state < state_count; state++) {
			for(int truck_action = 0; truck_action < action_count; truck_action++) {
				for(int car_action = 0; car_action < action_count; car_action++) {
					Q[state][truck_action][car_action][TRUCK] = 1;
					Q[state][truck_action][car_action][CAR] = 1;
				}
			}
		}

	}

	public int[][] fictitious_play(int state) {

		ArrayList<Integer>[][] response = new ArrayList[2][action_count];
		double best_reward, reward;

		for(int car_action = 0; car_action < action_count; car_action++) {

			best_reward = Double.MIN_VALUE;
			response[TRUCK][car_action] = new ArrayList<>();
			for(int truck_action = 0; truck_action < action_count; truck_action++) {

				reward = Q[state][truck_action][car_action][TRUCK];

				if(reward > best_reward) {
					best_reward = reward;
					response[TRUCK][car_action] = new ArrayList<>();
					response[TRUCK][car_action].add(truck_action);
				} else if(reward == best_reward) {
					response[TRUCK][car_action].add(truck_action);
				}
			}

		}

		for(int truck_action = 0; truck_action < action_count; truck_action++) {

			best_reward = Double.MIN_VALUE;
			response[CAR][truck_action] = new ArrayList<>();
			for(int car_action = 0; car_action < action_count; car_action++) {

				reward = Q[state][truck_action][car_action][CAR];

				if(reward > best_reward) {
					best_reward = reward;
					response[CAR][truck_action] = new ArrayList<>();
					response[CAR][truck_action].add(car_action);
				} else if(reward == best_reward) {
					response[CAR][truck_action].add(car_action);
				}
			}

		}

		double[][] action_counts = new double[2][action_count];
		for(int action = 0; action < action_count; action++) {
			action_counts[TRUCK][action] = 1.0 / action_count;
			action_counts[CAR][action] = 1.0 / action_count;
		}

		double[][] reaction = new double[2][action_count];

		for(int moves = 1; moves < fictitious_play_iterations; moves++) {

			reaction[TRUCK] = new double[4];
			for(int car_action = 0; car_action < 4; car_action++) {
				for(int truck_action : response[TRUCK][car_action]) {
					reaction[TRUCK][truck_action] += action_counts[CAR][car_action] / moves / response[TRUCK][car_action].size();
				}
			}

			reaction[CAR] = new double[4];
			for(int truck_action = 0; truck_action < 4; truck_action++) {
				for(int car_action : response[CAR][truck_action]) {
					reaction[CAR][car_action] += action_counts[TRUCK][truck_action] / moves / response[CAR][truck_action].size();
				}
			}

			for(int action = 0; action < action_count; action++) {
				action_counts[TRUCK][action] += reaction[TRUCK][action];
				action_counts[CAR][action] += reaction[CAR][action];
			}

		}

		ArrayList<Integer> choice = new ArrayList<>();
		int[][] result = new int[2][];
		double best;

		best = Double.MIN_VALUE;
		for(int truck_action = 0; truck_action < action_count; truck_action++) {
			if(action_counts[TRUCK][truck_action] > best) {
				choice = new ArrayList<>();
				choice.add(truck_action);
				best = action_counts[TRUCK][truck_action];
			} else if(action_counts[TRUCK][truck_action] == best) {
				choice.add(truck_action);
			}
		}
		result[TRUCK] = new int[choice.size()];
		for(int action = 0; action < choice.size(); action++) {
			result[TRUCK][action] = choice.get(action);
		}

		best = Double.MIN_VALUE;
		for(int car_action = 0; car_action < action_count; car_action++) {
			if(action_counts[CAR][car_action] > best) {
				choice = new ArrayList<>();
				choice.add(car_action);
				best = action_counts[CAR][car_action];
			} else if(action_counts[CAR][car_action] == best) {
				choice.add(car_action);
			}
		}
		result[CAR] = new int[choice.size()];
		for(int action = 0; action < choice.size(); action++) {
			result[CAR][action] = choice.get(action);
		}

		return result;
	}

	double[] distribution_Q(int state, int[][] actions) {

		double[] eval = new double[] { 0, 0 };
		int possibilities = actions[TRUCK].length + actions[CAR].length;

		for(int truck_action : actions[TRUCK]) {
			for(int car_action : actions[CAR]) {
				eval[TRUCK] += Q[state][truck_action][car_action][TRUCK] / possibilities;
				eval[CAR] += Q[state][truck_action][car_action][CAR] / possibilities;
			}
		}

		return eval;
	}

	int[] evaluate(int[][] actions) {
		int truck_action = actions[TRUCK][ (int) (Math.random() * actions[TRUCK].length) ];
		int car_action = actions[CAR][ (int) (Math.random() * actions[CAR].length) ];

		return new int[] { truck_action, car_action };
	}

	int[] evaluate(int state) {
		return evaluate(fictitious_play(state));
	}

	public void train() {

		CrashGame sim;
		double[] rewards;
		int[][] actions;
		double[] Q_evaluation;
		int new_state;

		double[][][][] Q_update = new double[state_count][action_count][action_count][2];

		String title_text = "Training: ";
		System.out.print(title_text);
		for(int i = title_text.length(); i < 100; i++) {
			System.out.print("-");
		}
		System.out.println("|");
		int progress = 0;

		for(int iteration = 0; iteration < Q_iterations; iteration++) {
			Q_update = new double[state_count][action_count][action_count][2];

			for(int state = 0; state < state_count; state++) {

				for(int truck_action = 0; truck_action < action_count; truck_action++) {
					for(int car_action = 0; car_action < action_count; car_action++) {

						sim = new CrashGame(base_game, state);
						rewards = sim.update(truck_action, car_action);

						new_state = sim.get_state();
						actions = fictitious_play(new_state);
						Q_evaluation = distribution_Q(new_state, actions);

						Q_update[state][truck_action][car_action][TRUCK] = rewards[TRUCK] + Beta * Q_evaluation[TRUCK];
						Q_update[state][truck_action][car_action][CAR] = rewards[CAR] + Beta * Q_evaluation[CAR];

					}
				}

			}

			Q = Q_update;

			while(progress < 100 * (double) iteration / Q_iterations) {
				System.out.print("*");
				progress++;
			}

		}

		while(progress < 100) {
			System.out.print("*");
			progress++;
		}
		System.out.println("|");
		System.out.println("Training complete");
	}

}

