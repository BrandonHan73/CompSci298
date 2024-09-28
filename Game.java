
public abstract class Game {

	public abstract int get_state_count();
	public abstract int get_action_count();

	public abstract Game get_copy(int state);

	public double[] update(int p1_action, int p2_action) {
		return update(new int[] { p1_action, p2_action });
	}
	public abstract double[] update(int[] actions);

	public abstract int get_state();
	public void randomize() {}

}

