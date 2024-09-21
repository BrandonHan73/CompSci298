
public abstract class Game {

	public abstract int get_state_count();
	public abstract int get_action_count();

	public abstract Game get_copy(int state);

	public abstract double[] update(int p1_action, int p2_action);

	public abstract int get_state();

}

