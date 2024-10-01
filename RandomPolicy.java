
public class RandomPolicy extends Policy {

	public RandomPolicy(Game g) {
		super(g);
	}

	@Override
	public double[][] get_action_options(State state) {
		int action_count = state.action_count();

		double[][] out = new double[2][action_count];
		for(int i = 0; i < action_count; i++) {
			out[0][i] = 1.0 / action_count;
			out[1][i] = 1.0 / action_count;
		}

		return out;
	}

	@Override
	public void train(int iterations) {}

}

