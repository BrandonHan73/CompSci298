
public class FictitiousPlay {

	private static final int P1 = 0, P2 = 1;

	/**
	 * Determines the best response for every action in the current state. Returns an array
	 * of action lists. The first dimension is the player. The second dimension is the action
	 * that the opponent takes. The result is an array containing the best possible actions 
	 * for the current player and opponent's action. 
	 *
	 * @param Q The Q function for the current state.
	 * @param action_count The number of possible actions for each player. 
	 */
	public static int[][][] best_responses(double[][][] Q, int action_count) {
		int[][][] out = new int[2][action_count][];

		for(int p2_action = 0; p2_action < action_count; p2_action++) {
			final int _p2_action = p2_action;
			out[P1][p2_action] = Utility.argmax(0, action_count, i -> Q[i][_p2_action][P1]);
		}

		for(int p1_action = 0; p1_action < action_count; p1_action++) {
			final int _p1_action = p1_action;
			out[P2][p1_action] = Utility.argmax(0, action_count, i -> Q[_p1_action][i][P2]);
		}

		return out;
	}

	public static int[][] pick_most_common(double[][] action_counts) {
		int[][] result = new int[action_counts.length][];

		for(int i = 0; i < action_counts.length; i++) {
			final int _i = i;
			result[i] = Utility.argmax(0, action_counts[i].length, j -> action_counts[_i][j]);
		}

		return result;
	}

	public static int[] evaluate_options(int[][] actions) {
		int[] result = new int[actions.length];

		for(int i = 0; i < result.length; i++) {
			result[i] = actions[i][ (int) (Math.random() * actions[i].length) ];
		}

		return result;
	}

	public static int[][] fictitious_play(double[][][] Q, int action_count) {

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

