package apple_lib.environment;

/**
 * Superclass used for defining a game environment. 
 *
 * Required capabilities:
 *  - Provide a current state
 *  - Define each player's possible actions
 *  - Update the state given the players' action choice
 *
 * Usage:
 *  - Define an enum for player actions and define the options for each player
 *  - Define a state and initialize the state
 *  - Create the clone function
 */
public abstract class Game implements Cloneable {

	////////////////////////////////// FIELDS //////////////////////////////////

	/* Stores the current state of the environment */
	protected State state;

	/* Number of players participating in this game */
	protected int player_count;

	/////////////////////////////// CONSTRUCTORS ///////////////////////////////

	/**
	 * Sets the player_count field. State field is initialized to null
	 */
	public Game(int players) {
		player_count = players;
		state = null;
	}

	///////////////////////////////// ABSTRACT /////////////////////////////////

	/**
	 * Provide all possible actions for a given player
	 */
	public abstract Enum[] options_for(int player);

	/**
	 * Updates the game and provides rewards for each player
	 */
	public abstract double[] update(ActionSet actions);

	////////////////////////////////// METHODS /////////////////////////////////

	/**
	 * Returns a copy of the current state
	 */
	public State get_state() {
		try {
			return (State) state.clone();
		} catch(CloneNotSupportedException cnse) {
			throw new RuntimeException("Could not clone state object");
		}
	}

	/**
	 * Creates an action set associated with this game
	 */
	public ActionSet create_action_set(Enum[] actions) {
		return new ActionSet(this, actions);
	}

	/**
	 * Checks whether the specified player can perform the given action
	 */
	public boolean check_action(int player, Enum action) {
		for(Enum a : options_for(player)) {
			if(a == action) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Provides the player count for this game
	 */
	public int player_count() {
		return player_count;
	}

	//////////////////////////////// OVERRIDING ////////////////////////////////

	@Override
	public Object clone() throws CloneNotSupportedException {
		Game out = (Game) super.clone();

		out.player_count = player_count;
		out.state = (State) state.clone();

		return out;
	}

}

