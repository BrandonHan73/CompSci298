import java.io.PrintStream;

public class CrashGame {

	private final double truck_crash_reward = 5.0;
	private final double car_crash_cost = 10.0;

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
		randomize_positions();
	}

	public CrashGame(CrashGame o) {
		rows = o.rows;
		cols = o.cols;
		rewards = o.rewards;

		truck = new Position();
		car = new Position();
		randomize_positions();
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

	public void randomize_positions() {
		truck.randomize(rows, cols);
		do {
			car.randomize(rows, cols);
		} while(truck.equals(car));
	}

	public double get_reward(Position pos) {
		return rewards[pos.row][pos.col];
	}

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

		boolean crash = truck.equals(car);
		if(truck_old.equals(car) && car_old.equals(truck)) {
			crash = true;
			truck = truck_old;
		}
		if(crash) {
			car = car_old;
			rewards[0] += truck_crash_reward;
			rewards[1] -= car_crash_cost;
		}

		return rewards;
	}

	public void print(PrintStream out) {
		out.print("Truck position: ");
		Main.print(out, truck);
		out.println();

		out.print("Car position: ");
		Main.print(out, car);
		out.println();

		for(int col = 0; col < cols; col++) {
			out.print("---");
		}
		out.println("-");
		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < cols; col++) {
				out.print("|");
				if(truck.row == row && truck.col == col) {
					out.print("TT");
				} else if(car.row == row && car.col == col) {
					out.print("cc");
				} else {
					out.print("  ");
				}
			}
			out.println("|");
			for(int col = 0; col < cols; col++) {
				out.print("---");
			}
			out.println("-");
		}
	}

}

