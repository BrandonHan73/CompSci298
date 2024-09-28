
public class CycleGame extends Game {

	private int cat_loc, rat_loc;

	public final static int cat_catch_reward = 1;
	public final static int rat_avoid_reward = 1;

	public CycleGame() {
		cat_loc = (int) (2 * Math.random());
		rat_loc = (int) (2 * Math.random());
	}

	public CycleGame(int state) {
		cat_loc = state / 2;
		rat_loc = state % 2;
	}

	public CycleGame(CycleGame game) {
		cat_loc = game.cat_loc;
		rat_loc = game.rat_loc;
	}

	@Override
	public int get_state_count() {
		return 4;
	}

	@Override
	public int get_action_count() {
		return 2;
	}

	@Override
	public Game get_copy(int state) {
		return new CycleGame(get_state());
	}

	@Override
	public double[] update(int p1_action, int p2_action) {
		return null;
	}

	@Override
	public int get_state() {
		return 2 * cat_loc + rat_loc;
	}

}

