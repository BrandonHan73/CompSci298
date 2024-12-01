package example;

public class ZeroSumCrashGame extends CrashGame {

	public ZeroSumCrashGame(int rows, int cols) {
		super(rows, cols);
	}

	public ZeroSumCrashGame(double[][] rewards) {
		super(rewards);
	}

	@Override
	public double[] update(CrashGameAction truck_action, CrashGameAction car_action) {
		double[] rewards = super.update(truck_action, car_action);
		rewards[0] = -rewards[1];

		return rewards;
	}

}

