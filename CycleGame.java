
public class CycleGame extends Game {

	public final static double confront = 1, steal = 1, avoid = 0.5, predict = 0.5;

	public CycleGame() {
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
		return new CycleGame();
	}

	@Override
	public double[] update(int p1_action, int p2_action) {
		double[] reward = null;

		if(p1_action == 0 && p2_action == 0) {
			reward = new double[] { -steal, steal }; 
		} else if(p1_action == 0 && p2_action == 1) {
			reward = new double[] { predict, -predict };
		} else if(p1_action == 1 && p2_action == 0) {
			reward = new double[] { confront, -confront };
		} else if(p1_action == 1 && p2_action == 1) {
			reward = new double[] { -avoid, avoid };
		}

		return reward;
	}

	@Override
	public int get_state() {
		return 0;
	}

}

