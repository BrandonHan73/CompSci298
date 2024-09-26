
import java.util.ArrayList;

/**
 * Various library functions for Q learning. 
 *
 * @author Brandon Han
 */
public class NashSolver {

	private static final int P1 = 0, P2 = 1;
	
	/**
	 * Takes the Q function for a given state and determines a set of actions
	 * for each player. The output will correspond to the Nash equilibriums of
	 * the given state that results in the highest sum of Q values for both
	 * players. If no Nash equilibriums exist, then the fictitious play algorithm
	 * will be applied instead. 
	 *
	 * @param Q The Q function for the given state.
	 * @param action_count The number of possible actions for each player. 
	 */
	public static int[][] evaluate_state(StateQ Q, int action_count) {

		int[][] all_nash = basic_nash(Q, action_count);
		int[][] best_nash;
		int[][] out = null;

		ArrayList<Integer> choices;

		if(all_nash.length > 0 && Config.use_pure_nash_optimization) {

			best_nash = pick_nash(Q, all_nash);
			out = new int[2][];

			choices = new ArrayList<>();
			for(int[] action_pair : best_nash) {
				choices.add(action_pair[P1]);
			}
			out[P1] = Utility.removeDuplicates(Utility.toArray(choices));

			choices = new ArrayList<>();
			for(int[] action_pair : best_nash) {
				choices.add(action_pair[P2]);
			}
			out[P2] = Utility.removeDuplicates(Utility.toArray(choices));

		} else {
			out = fictitious_play(Q, action_count);
		}

		return out;
	}

	/***
	 * Takes the Q function for the current state and the current action
	 * choices for both players. Assumes a uniform random choice between the
	 * action choices and finds the expected Q value. The first dimension
	 * of the action parameter represents the player. 
	 *
	 * @param Q The Q function for the given state.
	 * @param actions The action choices for both players. 
	 */
	public static double[] Q_expectation(StateQ Q, int[][] actions) {

		double[] eval = new double[] { 0, 0 };
		int possibilities = actions[P1].length * actions[P2].length;

		for(int truck_action : actions[P1]) {
			for(int car_action : actions[P2]) {
				eval[P1] += Q.get(truck_action, car_action, P1);
				eval[P2] += Q.get(truck_action, car_action, P2);

			}
		}

		eval[P1] /= possibilities;
		eval[P2] /= possibilities;

		return eval;
	}

	/**
	 * Finds all pure Nash equilibriums for the current state. Returns an array of
	 * action pairs. 
	 *
	 * @param Q The Q function for the current state.
	 * @param action_count The number of possible actions for each player. 
	 */
	public static int[][] basic_nash(StateQ Q, int action_count) {

		int[][][] response = best_responses(Q, action_count);

		ArrayList<int[]> nash = new ArrayList<>();

		for(int p1_action = 0; p1_action < action_count; p1_action++) {
			for(int p2_action = 0; p2_action < action_count; p2_action++) {
				if(Utility.contains(response[P1][p2_action], p1_action) && Utility.contains(response[P2][p1_action], p2_action)) {
					nash.add(new int[] {p1_action, p2_action});
				}
			}
		}

		return Utility.toMatrix(nash);
	}

	/**
	 * Takes an array of action pairs that lead to Nash equilibriums for the given
	 * Q function. Determines which Nash equilibrium result in the largest sum of
	 * Q values. 
	 *
	 * @param Q The Q function for the current state. 
	 * @param action_pairs The action pairs that correspond to Nash equilibrium.
	 */
	public static int[][] pick_nash(StateQ Q, int[][] action_pairs) {
		int[] choices = Utility.argmax(0, action_pairs.length, i -> {
			int p1_action = action_pairs[i][P1];
			int p2_action = action_pairs[i][P2];
			return Q.get(p1_action, p2_action, P1) + Q.get(p1_action, p2_action, P2);
		});

		int[][] out = new int[choices.length][];
		for(int i = 0; i < choices.length; i++) {
			out[i] = action_pairs[choices[i]];
		}
		return out;
	}

	/**
	 * Determines the best response for every action in the current state. Returns an array
	 * of action lists. The first dimension is the player. The second dimension is the action
	 * that the opponent takes. The result is an array containing the best possible actions 
	 * for the current player and opponent's action. 
	 *
	 * @param Q The Q function for the current state.
	 * @param action_count The number of possible actions for each player. 
	 */
	public static int[][][] best_responses(StateQ Q, int action_count) {
		int[][][] out = new int[2][action_count][];

		for(int p2_action = 0; p2_action < action_count; p2_action++) {
			final int _p2_action = p2_action;
			out[P1][p2_action] = Utility.argmax(0, action_count, i -> Q.get(i, _p2_action, P1));
		}

		for(int p1_action = 0; p1_action < action_count; p1_action++) {
			final int _p1_action = p1_action;
			out[P2][p1_action] = Utility.argmax(0, action_count, i -> Q.get(_p1_action, i, P2));
		}

		return out;
	}

	/**
	 * Takes an array of action frequencies and determines which appeared most often. The
	 * first dimension of the given array represents which player. 
	 *
	 * @param action_counts The frequencies of each action for every player. 
	 */
	public static int[][] pick_most_common(double[][] action_counts) {
		int[][] result = new int[action_counts.length][];

		for(int i = 0; i < action_counts.length; i++) {
			final int _i = i;
			result[i] = Utility.argmax(0, action_counts[i].length, j -> action_counts[_i][j]);
		}

		return result;
	}

	/**
	 * Takes an array of action choices and makes a uniformly random decision for each
	 * player. The input array's first dimension represents which player. The result
	 * of action[ployer] is an array of action choices. 
	 *
	 * @param actions The array of possible actions. 
	 */
	public static int[] evaluate_options(int[][] actions) {
		int[] result = new int[actions.length];

		for(int i = 0; i < result.length; i++) {
			result[i] = actions[i][ (int) (Math.random() * actions[i].length) ];
		}

		return result;
	}

	/**
	 * Runs the fictitious play algorithm for the current state. 
	 *
	 * @param Q The Q function for the current state.
	 * @param action_count The number of possible actions for each player. 
	 */
	public static int[][] fictitious_play(StateQ Q, int action_count) {

		int[][][] response = best_responses(Q, action_count);

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
					reaction[P1][p1_action] += action_counts[P2][p2_action] / moves / response[P1][p2_action].length;
				}
			}

			reaction[P2] = new double[action_count];
			for(int p1_action = 0; p1_action < action_count; p1_action++) {
				for(int p2_action : response[P2][p1_action]) {
					reaction[P2][p2_action] += action_counts[P1][p1_action] / moves / response[P2][p1_action].length;
				}
			}

			for(int action = 0; action < action_count; action++) {
				action_counts[P1][action] += reaction[P1][action];
				action_counts[P2][action] += reaction[P2][action];
			}

		}

		return pick_most_common(action_counts);

	}

}

