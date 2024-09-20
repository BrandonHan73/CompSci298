
public class CrashGame {

	private final int rows, cols;
	private final double[][] rewards;

	private Position truck, car;

	public CrashGame(int rows_, int cols_) {
		rows = rows_;
		cols = cols_;

		rewards = new double[rows][cols];
		for(double[] row : rewards) {
			for(int i = 0; i < cols; i++) {
				row[i] = Math.random();
			}
		}

		truck = new Position();
		car = new Position();
		truck.randomize(rows, cols);
		do {
			car.randomize(rows, cols);
		} while(truck.equals(car));
	}

	public CrashGame(CrashGame o) {
		rows = o.rows;
		cols = o.cols;
		rewards = o.rewards;
	}

	public double get_reward(Position pos) {
		return rewards[pos.row][pos.col];
	}

	public double[] update(int truck_action, int car_action) {
		Position truck_old = new Position(truck);
		Position car_old = new Position(car);

		switch(truck_action % 4) {
			case 0:
			truck.up(rows);
			break;
			case 1:
			truck.right(cols);
			break;
			case 2:
			truck.down(rows);
			break;
			case 3:
			truck.left(cols);
			break;
			default:
		}

		double[] rewards = { get_reward(truck), get_reward(car) };

		return rewards;
	}

}

