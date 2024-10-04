package game;

public class ActionSet {

	public final int player_count;
	private int[] actions;
	private int[][] choices;

	public ActionSet(int[] player_actions, int[][] player_choices) {
		player_count = player_actions.length;
		actions = player_actions;
		choices = player_choices;
	}

	public int get(int player) {
		return actions[player];
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
			res *= choices[i - 1].length;
			res += actions[i];
		}

		return Integer.hashCode(res);
	}

}

