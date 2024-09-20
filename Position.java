
public class Position {

	public int row, col;

	Position() {
		row = 0;
		col = 0;
	}

	Position(int row_, int col_) {
		row = row_;
		col = col_;
	}

	Position(Position o) {
		row = o.row;
		col = o.col;
	}

	void randomize(int row_bound, int col_bound) {
		row = (int) (Math.random() * row_bound);
		col = (int) (Math.random() * col_bound);
	}

	void down(int row_bound) {
		row = (row + row_bound + 1) % row_bound;
	}

	void up(int row_bound) {
		row = (row + row_bound - 1) % row_bound;
	}

	void right(int col_bound) {
		col = (col + col_bound + 1) % col_bound;
	}

	void left(int col_bound) {
		col = (col + col_bound - 1) % col_bound;
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

}

