package example;

public class SymmetricGame extends CrashGame {

	public SymmetricGame(int rows, int cols) {
		super(rows, cols);
	}

	public SymmetricGame(double[][] rewards) {
		super(rewards);
	}

	@Override
	public double[] update(CrashGameAction truck_action, CrashGameAction car_action) {
		double[] rewards = super.update(truck_action, car_action);
		rewards[0] += rewards[1];
		rewards[1] = rewards[0];

		return rewards;
	}

}

