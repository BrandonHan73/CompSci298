package policy;

import java.util.HashMap;
import java.util.Map;

import base.Utility;
import game.ActionSet;

public class StateQ {

	private Map<ActionSet, double[]> Q;

	public final int[][] action_choices;

	private final int P1 = 0, P2 = 1;

	public StateQ(State s) {
		action_choices = s.choices();

		Q = new HashMap<>();
		Utility.forEachChoice(action_choices, pick -> {
			Q.put(new ActionSet(pick, action_choices), Utility.createDoubleArray(action_choices.length, 1));
		});
	}

	public double get(ActionSet actions, int player) {
		return Q.get(actions)[player];
	}

	public void set(double val, ActionSet actions, int player) {
		Q.get(actions)[player] = val;
	}

	/**
	 * Takes the Q function for the current state and the current action
	 * choices for both players. The first dimension of the action parameter 
	 * represents the player. 
	 *
	 * @param Q The Q function for the given state.
	 * @param actions The action choice distributions for both players. 
	 */
	public double[] value(double[][] actions) {

		double[] eval = new double[] { 0, 0 };

		double prob;
		for(int p1_action = 0; p1_action < actions[P1].length; p1_action++) {
			for(int p2_action = 0; p2_action < actions[P2].length; p2_action++) {
				prob = actions[P1][p1_action] * actions[P2][p2_action];
				eval[P1] += prob * get(p1_action, p2_action, P1);
				eval[P2] += prob * get(p1_action, p2_action, P2);

			}
		}

		return eval;
	}

	public double[] value(boolean fast) {
		return value(NashSolver.evaluate_state(this, fast));
	}

	public double[] value() {
		return value(false);
	}

}

