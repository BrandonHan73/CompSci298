
public class RockPaperScissors extends Game {

	public int get_state_count() { return 1; }
	public int get_action_count() { return 3; }

	public Game get_copy(int state) { return new RockPaperScissors(); }

	public double[] update(int p1_action, int p2_action) {

		double[] rewards = new double[] { 0, 0 };

		int diff = (p1_action - p2_action + 3) % 3;

		switch(diff) {
			case 1:
			rewards[1] += 1;
			break;
			case 2:
			rewards[0] += 1;
			break;
			default:
		}

		return rewards;

	}

	public double[] update(int[] actions) {
		return update(actions[0], actions[1]);
	}

	public int get_state() { return 0; }

}

