package policy;

public class StateQ {

	private double[][][] Q;

	public final int action_count;

	private final int P1 = 0, P2 = 1;

	public StateQ(int actions) {
		action_count = actions;

		Q = new double[action_count][action_count][2];
		for(int p1_action = 0; p1_action < action_count; p1_action++) {
			for(int p2_action = 0; p2_action < action_count; p2_action++) {
				Q[p1_action][p2_action][P1] = 1;
				Q[p1_action][p2_action][P2] = 1;
			}
		}
	}

	public double get(int p1_action, int p2_action, int player) {
		return Q[p1_action][p2_action][player];
	}

	public void set(double val, int p1_action, int p2_action, int player) {
		Q[p1_action][p2_action][player] = val;
	}

}

