package example;

import apple_lib.environment.*;

public class CoordinationGame extends Game {

	/////////////////////////////// MINI CLASSES ///////////////////////////////

	public static enum CoordActions {
		UP, DOWN, LEFT, RIGHT, STAY
	}

	public static class CoordState {
		private int truck_row, truck_col;
		private int car_row, car_col;
		private int cargo_row, cargo_col;
		private int food_row, food_col;
		private int thief_row, thief_col;
		public CoordState(Game base) {
			super(base);
		}
		@Override
		public double[] parameterize() {
			return new double[] {
				truck_row, truck_col, car_row, car_col,
				cargo_row, cargo_col, food_row, food_col,
				thief_row, thief_col
			};
		}
		@Override
		public Object clone() throws CloneNotSupportedException {
			CoordState out = (CoordState) super.clone();
			out.truck_row = truck_row;
			out.truck_col = truck_col;
			out.car_row = car_row;
			out.car_col = car_col;
			out.cargo_row = cargo_row;
			out.cargo_col = cargo_col;
			out.food_row = food_row;
			out.food_col = food_col;
			out.thief_row = thief_row;
			out.thief_col = thief_col;
			return out;
		}
	}

	//////////////////////////////// OVERRIDING ////////////////////////////////

	@Override
	public Enum[] options_for(int player) {
		return CoordActions.values();
	}

	@Override
	public double[] update(ActionSet actions) {
	}

}

