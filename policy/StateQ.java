package policy;

import java.util.HashMap;
import java.util.Map;

import base.State;
import base.Utility;
import game.ActionSet;

public class StateQ {

	private Map<ActionSet, double[]> Q;

	public final int[][] action_choices;
	public final int player_count;

	public StateQ(State s) {
		action_choices = s.choices();
		player_count = action_choices.length;

		Q = new HashMap<>();
		Utility.forEachChoice(action_choices, pick -> {
			Q.put(new ActionSet(pick, action_choices), Utility.createDoubleArray(action_choices.length, 1));
		});
	}

	public double[] get(ActionSet actions) {
		return Q.get(actions);
	}

	public double get(ActionSet actions, int player) {
		return Q.get(actions)[player];
	}

	public void set(double val, ActionSet actions, int player) {
		Q.get(actions)[player] = val;
	}

	public double[] value(ActionDistribution[] distributions) {

		double[] eval = new double[action_choices.length];

		Utility.forEachChoice(action_choices, pick -> {
			double prob = 1;
			for(int player = 0; player < pick.length; player++) {
				prob *= distributions[player].get(pick[player]);
			}

			ActionSet as = new ActionSet(pick, action_choices);
			for(int player = 0; player < pick.length; player++) {
				eval[player] += prob * get(as, player);
			}
		});

		return eval;
	}

	public double[] value(boolean fast) {
		return value(NashSolver.evaluate_state(this, fast));
	}

	public double[] value() {
		return value(false);
	}

}

