
public class RockPaperScissors extends Game {

	private State state;

	public RockPaperScissors() {
		state = new State(new int[][] {
			new int[] { 0, 1, 2 },
			new int[] { 0, 1, 2 }
		});
	}

	@Override
	public State[] get_possible_states() {
		return new State[] { state };
	}

	public Game get_copy(State state) { return new RockPaperScissors(); }

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

	public State get_state() { return state; }

}

