import java.util.Random;

public class RandomPolicy extends Policy {

	public RandomPolicy(Game g) {
		super(g);
	}

	@Override
	public int[] evaluate(State state) {
		int action_count = state.action_count();
		return new int[] {
			(int) (action_count * Math.random()),
			(int) (action_count * Math.random())
		};
	}

	@Override
	public void train() {}

}

