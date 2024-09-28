import java.io.PrintStream;

public class CrashGame extends Game {

	private final double truck_crash_reward = 5.0;
	private final double car_crash_cost = 10.0;

	private final double car_cutoff_reward = 8.0;
	private final double truck_cutoff_cost = 7.0;

	public final int rows, cols;
	private final double[][] rewards;

	private Position truck, car;

	public CrashGame(int rows_, int cols_) {
		rows = rows_;
		cols = cols_;

		rewards = new double[rows][cols];
		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < cols; col++) {
				rewards[row][col] = Math.exp( -(Math.pow(rows / 2 - row, 2) + Math.pow(cols / 2 - col, 2)) / 2 );
			}
		}

		truck = new Position();
		car = new Position();
		randomize();
	}

	public CrashGame(double[][] rewards_) {
		this(rewards_.length, rewards_[0].length);

		for(int r = 0; r < rows; r++) {
			for(int c = 0; c < cols; c++) {
				rewards[r][c] = rewards_[r][c];
			}
		}
	}

	public CrashGame(CrashGame o) {
		this(o.rewards);
	}

	public CrashGame(CrashGame o, int state) {
		this(o);

		truck = new Position();
		car = new Position();

		car.col = state % cols;
		state /= cols;

		car.row = state % rows;
		state /= rows;

		truck.col = state % cols;
		state /= cols;

		truck.row = state % rows;
		state /= rows;
	}

	@Override
	public int get_state_count() {
		return (int) Math.pow(rows, 2) * (int) Math.pow(cols, 2);
	}

	@Override
	public int get_action_count() {
		return 4;
	}

	@Override
	public Game get_copy(int state) {
		return new CrashGame(this, state);
	}

	public void randomize() {
		truck.randomize(rows, cols);
		do {
			car.randomize(rows, cols);
		} while(truck.equals(car));
	}

	public double get_reward(Position pos) {
		return rewards[pos.row][pos.col];
	}

	@Override
	public int get_state() {
		int state = truck.row;
		state = state * cols + truck.col;
		state = state * rows + car.row;
		state = state * cols + car.col;
		return state;
	}

	public double[] update(int[] actions) {
		return update(actions[0], actions[1]);
	}

	@Override
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

		switch(car_action % 4) {
			case 0:
			car.up(rows);
			break;
			case 1:
			car.right(cols);
			break;
			case 2:
			car.down(rows);
			break;
			case 3:
			car.left(cols);
			break;
			default:
		}

		double[] rewards = { get_reward(truck), get_reward(car) };

		if(truck_old.equals(car) && car_old.equals(truck)) {
			truck = truck_old;
			car = car_old;
			rewards[0] += truck_crash_reward;
			rewards[1] -= car_crash_cost;
		}
		if(truck.equals(car)) {
			truck = truck_old;
			rewards[0] -= truck_cutoff_cost;
			rewards[1] += car_cutoff_reward;
		}

		return rewards;
	}

	public void print(PrintStream out) {
		Utility.println(out, "Truck position: ", truck);
		Utility.println(out, "Car position: ", car);
		Utility.println(out, "State code: ", get_state());

		for(int col = 0; col < cols; col++) {
			Utility.print(out, "---");
		}
		Utility.println(out, "-");
		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < cols; col++) {
				Utility.print(out, "|");
				if(truck.row == row && truck.col == col) {
					Utility.print(out, "TT");
				} else if(car.row == row && car.col == col) {
					Utility.print(out, "cc");
				} else {
					Utility.print(out, "  ");
				}
			}
			Utility.println(out, "|");
			for(int col = 0; col < cols; col++) {
				Utility.print(out, "---");
			}
			Utility.println(out, "-");
		}
	}

}

