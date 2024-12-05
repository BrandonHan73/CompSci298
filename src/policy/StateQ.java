package policy;

import java.util.HashMap;
import java.util.Map;

import base.*;
import util.*;
import environment.*;

public class StateQ {

	private static class Information {
		double[] Q = null;
		StateQ next = null;
		double[] rewards = null;
		int players;

		Information(int player_count) {
			players = player_count;
			Q = new double[players];
		}
	}

	private Map<ActionSet, Information> Q;

	public final State state;

	private Enum[][] choices;
	private int player_count;

	private double value[];
	private boolean fast_value;

	public StateQ(State s) {
		value = null;
		fast_value = true;
		state = s.get_copy();

		player_count = state.player_count();
		choices = new Enum[player_count][];
		for(int i = 0; i < player_count; i++) {
			choices[i] = state.choices_for(i);
		}

		Q = new HashMap<>();
		Utility.forEachChoice(choices, pick -> {
			Q.put(new ActionSet(pick, state), new Information(player_count));
		});
	}

	public double[] get(ActionSet actions) {
		if(Q.keySet().contains(actions)) {
			return Q.get(actions).Q;
		} else {
			StringBuilder sb = new StringBuilder();

			sb.append("Provided action set ");
			sb.append(actions);
			sb.append(" with code ");
			sb.append(actions.hashCode());
			sb.append(" is not one of ");
			sb.append(Q.keySet().size());
			sb.append(" valid options");

			sb.append("\n");
			for(ActionSet as : Q.keySet()) {
				sb.append(as).append(" ").append(as.hashCode());
				sb.append("\n");
			}

			throw new RuntimeException(sb.toString());
		}
	}

	public double get(ActionSet actions, int player) {
		return get(actions)[player];
	}

	public void update(ActionSet actions, double[] rewards, StateQ next) {
		Information info = Q.get(actions);

		info.rewards = Utility.copyDoubleArr(rewards);
		info.next = next;
		update(actions);
	}

	public void update(ActionSet actions) {

	}

	public void set(double val, ActionSet actions, int player) {
		double[] vector = Q.get(actions).Q;
		if(vector[player] != val) {
			vector[player] = val;
			fast_value = true;
			value = null;
		}
	}

	public double[] value(ActionDistribution[] distributions) {

		double[] eval = new double[state.player_count()];

		Utility.forEachChoice(choices, pick -> {
			double prob = 1;
			for(int player = 0; player < pick.length; player++) {
				prob *= distributions[player].get(pick[player]);
			}

			ActionSet as = new ActionSet(pick, state);
			for(int player = 0; player < pick.length; player++) {
				eval[player] += prob * get(as, player);
			}
		});

		return eval;
	}

	public double[] value(boolean fast) {
		if(value == null || (fast_value && !fast)) {
			fast_value = fast;
			value = value(NashSolver.evaluate_state(this, fast));
		}

		return value;
	}

	public double[] value() {
		return value(false);
	}

}

