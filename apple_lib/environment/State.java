package apple_lib.environment;

/**
 * Superclass for defining a game state
 *
 * Required capabilities
 *  - Detect equivalence with other state objects
 *  - Can be used with hash tables
 *
 * Usage
 *  - Override hash and equals methods
 */
public class State implements Cloneable {

	////////////////////////////////// FIELDS //////////////////////////////////

	/* Game that this state object is used in */
	protected Game base_game;

	/////////////////////////////// CONSTRUCTORS ///////////////////////////////

	/**
	 * Initializes the base_game field
	 */
	public State(Game base) {
		base_game = base;
	}

	////////////////////////////////// METHODS /////////////////////////////////

	/**
	 * Determines the player count of the base game
	 */
	public int player_count() {
		return base_game.player_count;
	}

	/**
	 * Polls the base game for the action choices for this state
	 */
	public Enum[] options_for(int player) {
		return base_game.options_for(player);
	}

	/**
	 * Encodes this state object into a double array
	 */
	public double[] parameterize() {
		return new double[] {};
	}

	/**
	 * Returns the length of the parameter representation of this state
	 */
	public int parameter_count() {
		return parameterize().length;
	}

	//////////////////////////////// OVERRIDING ////////////////////////////////

	@Override
	public boolean equals(Object other) {
		if(other instanceof State) {
			State state = (State) other;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		State out = (State) super.clone();

		out.base_game = base_game;

		return out;
	}

}

