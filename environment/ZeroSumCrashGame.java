package environment;

import java.io.PrintStream;

import base.Utility;
import base.Position;
import base.State;

public class ZeroSumCrashGame extends Game {

	private final double truck_crash_reward = 10.0;

	public final int rows, cols;
	private final double[][] rewards;

	private Position truck, car;

	private CrashGameState[] possible_states;

	private class CrashGameState extends State {
		Position truck, car;

		CrashGameState(Position t, Position c) {
			super(new int[][] {
				new int[] { 0, 1, 2, 3 },
				new int[] { 0, 1, 2, 3 }
			});

			truck = new Position(t);
			car = new Position(c);
		}

		@Override
		public boolean equals(Object o) {
			CrashGameState s;
			if(o instanceof CrashGameState) {
				s = (CrashGameState) o;

				return truck.equals(s.truck) && car.equals(s.car);
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			int state = truck.row;
			state = state * cols + truck.col;
			state = state * rows + car.row;
			state = state * cols + car.col;
			return state;
		}

		@Override
		public String toString() {
			return truck.toString() + " vs. " + car.toString();
		}
	}

	public ZeroSumCrashGame(int rows_, int cols_) {
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

		if(possible_states == null) {
			possible_states = new CrashGameState[rows * cols * (rows * cols - 1)];
			int state_ = 0;
			for(int tr = 0; tr < rows; tr++) {
				for(int tc = 0; tc < cols; tc++) {
					for(int cr = 0; cr < rows; cr++) {
						for(int cc = 0; cc < cols; cc++) {
							if(!(tr == cr && tc == cc)) {
								possible_states[state_++] = new CrashGameState(
									new Position(tr, tc), 
									new Position(cr, cc)
								);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public State[] get_possible_states() {
		return possible_states;
	}

	public ZeroSumCrashGame(double[][] rewards_) {
		this(rewards_.length, rewards_[0].length);

		for(int r = 0; r < rows; r++) {
			for(int c = 0; c < cols; c++) {
				rewards[r][c] = rewards_[r][c];
			}
		}
	}

	public ZeroSumCrashGame(ZeroSumCrashGame o) {
		rows = o.rows;
		cols = o.cols;
		rewards = o.rewards;

		truck = new Position(o.truck);
		car = new Position(o.car);

		possible_states = o.possible_states;
	}

	public ZeroSumCrashGame(ZeroSumCrashGame o, CrashGameState state) {
		this(o);

		truck = new Position(state.truck);
		car = new Position(state.car);
	}

	public ZeroSumCrashGame(ZeroSumCrashGame o, Position t, Position c) {
		this(o);

		truck = new Position(t);
		car = new Position(c);
	}

	@Override
	public Game get_copy(State state) {
		return new ZeroSumCrashGame(this, (CrashGameState) state);
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
	public State get_state() {
		return new CrashGameState(truck, car);
	}

	@Override
	public double[] update(ActionSet as) {
		return update(as.get(0), as.get(1));
	}

	public double[] update(int truck_action, int car_action) {
		Position truck_old = new Position(truck);
		Position car_old = new Position(car);

		switch(truck_action % 4) {
			case 0:
			truck.up(0, rows);
			break;
			case 1:
			truck.right(0, cols);
			break;
			case 2:
			truck.down(0, rows);
			break;
			case 3:
			truck.left(0, cols);
			break;
			default:
		}

		switch(car_action % 4) {
			case 0:
			car.up(0, rows);
			break;
			case 1:
			car.right(0, cols);
			break;
			case 2:
			car.down(0, rows);
			break;
			case 3:
			car.left(0, cols);
			break;
			default:
		}

		double[] rewards = { 0, get_reward(car) };

		if(truck_old.equals(car) && car_old.equals(truck)) {
			truck = truck_old;
			car = car_old;
			rewards[1] = -truck_crash_reward;
		}
		if(truck.equals(car)) {
			if(car.equals(car_old)) {
				truck = truck_old;
			} else {
				car = car_old;
			}
			rewards[1] = -truck_crash_reward;
		}
		rewards[0] = -rewards[1];

		return rewards;
	}

	public void print(PrintStream out) {
		Utility.println(out, "Truck position: ", truck);
		Utility.println(out, "Car position: ", car);

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

