package apple_lib.environment;

/**
 * Action set representing the action choices of a group of players
 */
public class ActionSet implements Cloneable {

	////////////////////////////////// FIELDS //////////////////////////////////

	/* Game that this action set object is used in */
	protected Game base_game;

	/* Action choices */
	private Enum[] action_choices;

	/////////////////////////////// CONSTRUCTORS ///////////////////////////////

	/**
	 * Initializes the base_game field and sets the actions
	 */
	protected ActionSet(Game base, Enum[] actions) {
		base_game = base;

		if(actions.length != player_count()) {
			throw new RuntimeException(player_count() + " actions expected, " + actions.length + " actions provided");
		}

		action_choices = new Enum[actions.length];
		for(int a = 0; a < actions.length; a++) {
			if(base_game.check_action(a, actions[a]) == false) {
				throw new RuntimeException("Invalid action " + actions[a] + " for player " + a);
			}
			action_choices[a] = actions[a];
		}
	}

	////////////////////////////////// METHODS /////////////////////////////////

	/**
	 * Determines the player count of the base game
	 */
	public int player_count() {
		return base_game.player_count;
	}

	/**
	 * Polls the base game for the action choices for each player
	 */
	public Enum[] options_for(int player) {
		return base_game.options_for(player);
	}

	/**
	 * Encodes this action set object into an integer array
	 */
	public int[] parameterize() {
		int[] out = new int[action_choices.length];
		for(int a = 0; a < action_choices.length; a++) {
			out[a] = action_choices[a].ordinal();
		}
		return out;
	}

	/**
	 * Returns the length of the parameter representation of this state
	 */
	public int parameter_count() {
		return parameterize().length;
	}

	/**
	 * Returns the current action chosen by a given player
	 */
	public Enum get(int player) {
		return action_choices[player];
	}

	/**
	 * Creates a copy of this action set but changes a specified action choice
	 */
	public ActionSet modify(int player, Enum action) {
		Enum[] new_actions = new Enum[player_count()];
		for(int p = 0; p < player_count(); p++) {
			new_actions[p] = action_choices[p];
		}

		new_actions[player] = action;
		return base_game.create_action_set(new_actions);
	}

	//////////////////////////////// OVERRIDING ////////////////////////////////

	@Override
	public boolean equals(Object other) {
		if(other instanceof ActionSet) {
			ActionSet action_set = (ActionSet) other;

			if(player_count() != action_set.player_count()) {
				return false;
			}

			for(int a = 0; a < action_choices.length; a++) {
				if(action_choices[a] != action_set.action_choices[a]) {
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
		int out = 0;
		for(int a = 0; a < player_count(); a++) {
			out *= options_for(a).length;
			out += action_choices[a].ordinal();
		}
		return out;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		ActionSet out = (ActionSet) super.clone();

		out.base_game = base_game;
		out.action_choices = new Enum[action_choices.length];
		for(int a = 0; a < action_choices.length; a++) {
			out.action_choices[a] = action_choices[a];
		}

		return out;
	}

}

