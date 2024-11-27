package util;

public class Position {

	public int row, col;

	public Position() {
		row = 0;
		col = 0;
	}

	public Position(int row_, int col_) {
		row = row_;
		col = col_;
	}

	public Position(Position o) {
		row = o.row;
		col = o.col;
	}

	public void randomize(int row_bound, int col_bound) {
		row = (int) (Math.random() * row_bound);
		col = (int) (Math.random() * col_bound);
	}

	public void down(int row_bound) {
		row = (row + row_bound + 1) % row_bound;
	}

	public void up(int row_bound) {
		row = (row + row_bound - 1) % row_bound;
	}

	public void right(int col_bound) {
		col = (col + col_bound + 1) % col_bound;
	}

	public void left(int col_bound) {
		col = (col + col_bound - 1) % col_bound;
	}

	public void down(int row_min, int row_max) {
		row = Math.max(row_min, Math.min(row + 1, row_max - 1));
	}

	public void up(int row_min, int row_max) {
		row = Math.max(row_min, Math.min(row - 1, row_max - 1));
	}

	public void right(int col_min, int col_max) {
		col = Math.max(col_min, Math.min(col + 1, col_max - 1));
	}

	public void left(int col_min, int col_max) {
		col = Math.max(col_min, Math.min(col - 1, col_max - 1));
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof Position) {
			Position pos = (Position) o;
			return row == pos.row && col == pos.col;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "(" + row + "," + col + ")";
	}

}

