package environment;

import base.Utility;
import policy.ActionDistribution;
import base.State;

public class ActionSet {

	public final int player_count;
	private int[] actions;
	private final State state;

	public ActionSet(int[] player_actions, State st) {
		player_count = player_actions.length;
		actions = Utility.copy(player_actions);
		state = st;
	}

	public ActionSet(ActionDistribution[] choices, State st) {
		int[] poll = new int[choices.length];

		for(int i = 0; i < choices.length; i++) {
			poll[i] = choices[i].poll();
		}

		player_count = choices.length;
		actions = poll;
		state = st;
	}

	public ActionSet(ActionSet o) {
		this(o.actions, o.state);
	}

	public int get(int player) {
		return actions[player];
	}

	public void set(int player, int action) {
		actions[player] = action;
	}

	@Override
	public boolean equals(Object o) {
		ActionSet as;
		if(o instanceof ActionSet) {
			as = (ActionSet) o;

			for(int i = 0; i < actions.length; ++i) {
				if(actions[i] != as.actions[i]) {
					return false;
				}
			}

			return true;

		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int res = actions[0];

		for(int i = 1; i < actions.length; ++i) {
			res *= state.choices_for(i).size();
			res += actions[i];
		}

		return res;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("(");
		for(int pl = 0; pl < player_count - 1; pl++) {
			sb.append(actions[pl]).append(", ");
		}
		sb.append(actions[player_count - 1]).append(")");

		return sb.toString();
	}

}

