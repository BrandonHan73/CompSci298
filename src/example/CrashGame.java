package example;

import java.io.PrintStream;
import java.util.Set;

import util.*;
import base.*;
import environment.*;

public class CrashGame extends Game {

	private final double truck_crash_reward = 5.0;
	private final double car_crash_cost = 10.0;

	private final double car_cutoff_reward = 8.0;
	private final double truck_cutoff_cost = 7.0;

	public final int rows, cols;
	private final double[][] rewards;

	public static class CrashGameState extends State {
		Position truck, car;

		CrashGameState(CrashGame game, Position t, Position c) {
			super(game);

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
			CrashGame game = (CrashGame) base;
			int state = truck.row;
			state = state * game.cols + truck.col;
			state = state * game.rows + car.row;
			state = state * game.cols + car.col;
			return state;
		}

		@Override
		public String toString() {
			return truck.toString() + " vs. " + car.toString();
		}
	}

	public CrashGame(int rows_, int cols_) {
		super(2);
		actions = new Set[] {
			Utility.toTreeSet(0, 1, 2, 3),
			Utility.toTreeSet(0, 1, 2, 3)
		};

		rows = rows_;
		cols = cols_;

		rewards = new double[rows][cols];
		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < cols; col++) {
				rewards[row][col] = Math.exp( -(Math.pow(rows / 2 - row, 2) + Math.pow(cols / 2 - col, 2)) / 2 );
			}
		}

		state = new CrashGameState(this, new Position(), new Position());
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

		state = new CrashGameState(this, ((CrashGameState) o.state).truck, ((CrashGameState) o.state).car);
	}

	public CrashGame(CrashGame o, CrashGameState state) {
		this(o);

		state = new CrashGameState(this, ((CrashGameState) state).truck, ((CrashGameState) state).car);
	}

	public CrashGame(CrashGame o, Position t, Position c) {
		this(o);

		state = new CrashGameState(this, t, c);
	}

	@Override
	public Game get_copy(State state) {
		return new CrashGame(this, (CrashGameState) state);
	}

	public void randomize() {
		Position truck = ((CrashGameState) state).truck;
		Position car = ((CrashGameState) state).car;
		truck.randomize(rows, cols);
		do {
			car.randomize(rows, cols);
		} while(truck.equals(car));
	}

	@Override
	public Game get_random_copy() {
		CrashGame out = new CrashGame(this);
		out.randomize();
		return out;
	}

	public double get_reward(Position pos) {
		return rewards[pos.row][pos.col];
	}

	@Override
	public double[] update(ActionSet as) {
		return update(as.get(0), as.get(1));
	}

	public double[] update(int truck_action, int car_action) {
		CrashGameState curr = (CrashGameState) state;
		Position truck_old = new Position(curr.truck);
		Position car_old = new Position(curr.car);

		switch(truck_action % 4) {
			case 0:
			curr.truck.up(rows);
			break;
			case 1:
			curr.truck.right(cols);
			break;
			case 2:
			curr.truck.down(rows);
			break;
			case 3:
			curr.truck.left(cols);
			break;
			default:
		}

		switch(car_action % 4) {
			case 0:
			curr.car.up(rows);
			break;
			case 1:
			curr.car.right(cols);
			break;
			case 2:
			curr.car.down(rows);
			break;
			case 3:
			curr.car.left(cols);
			break;
			default:
		}

		double[] rewards = { get_reward(curr.truck), get_reward(curr.car) };

		if(truck_old.equals(curr.car) && car_old.equals(curr.truck)) {
			curr.truck = truck_old;
			curr.car = car_old;
			rewards[0] += truck_crash_reward;
			rewards[1] -= car_crash_cost;
		}
		if(curr.truck.equals(curr.car)) {
			curr.truck = truck_old;
			rewards[0] -= truck_cutoff_cost;
			rewards[1] += car_cutoff_reward;
		}

		return rewards;
	}

	public void print(PrintStream out) {
		Position truck = ((CrashGameState) state).truck;
		Position car = ((CrashGameState) state).car;
		Print.println(out, "Truck position: ", truck);
		Print.println(out, "Car position: ", car);

		for(int col = 0; col < cols; col++) {
			Print.print(out, "---");
		}
		Print.println(out, "-");
		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < cols; col++) {
				Print.print(out, "|");
				if(truck.row == row && truck.col == col) {
					Print.print(out, "TT");
				} else if(car.row == row && car.col == col) {
					Print.print(out, "cc");
				} else {
					Print.print(out, "  ");
				}
			}
			Print.println(out, "|");
			for(int col = 0; col < cols; col++) {
				Print.print(out, "---");
			}
			Print.println(out, "-");
		}
	}

}

