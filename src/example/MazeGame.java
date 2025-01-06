package example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import apple_lib.environment.*;

public class MazeGame extends Game {

	////////////////////////////////// FIELDS //////////////////////////////////

	/* Holds the size of the board */
	private int row_count, col_count;

	/* Store the initial state */
	private int start_ar, start_ac;
	private int start_br, start_bc; 
	private int start_cr, start_cc; 
	private int start_dr, start_dc; 
	private int start_or, start_oc;

	/* Stores string representation of the current game state */
	private String[] lines;

	/////////////////////////////// CONSTRUCTORS ///////////////////////////////

	/**
	 * Basic constructor. Reads maze from a file. 
	 */
	public MazeGame(File input) {
		super(5);

		BufferedReader read = new BufferedReader(new FileReader(input));
		String in = read.readLine();

		ArrayList<String> stack = new ArrayList<>();
		stack.add(in);

		row_count = 0;
		col_count = in.length() / 4;

		while( (in = read.readLine()) != null ) {

			row_count++;
			stack.add(in);
			stack.add(read.readLine());
		}
	}

	//////////////////////////////// OVERRIDING ////////////////////////////////

	public Enum[] options_for(int player) {
		return MazeGameAction.values();
	}

	public double[] update(ActionSet actions) {
		return null;
	}

	public void initialize() {
	}

	/////////////////////////////// MINI CLASSES ///////////////////////////////

	/**
	 * Defines possible actions for all entities
	 */
	public static enum MazeGameAction {
		UP, DOWN, LEFT, RIGHT
	}

	/**
	 * Describes the state of any Maze Game instance
	 */
	public class MazeState extends State {

		//////////////////////////////// FIELDS ////////////////////////////////

		/* Encodes position of each entity */
		protected int ar, ac, br, bc, cr, cc, dr, dc, or, oc;

		////////////////////////////// OVERRIDING //////////////////////////////

		@Override
		public boolean equals(Object other) {
			if(other instanceof MazeState) {
				MazeState state = (MazeState) other;

				return 
					ax == state.ax && ay == state.ay && 
					bx == state.bx && by == state.by && 
					cx == state.cx && cy == state.cy && 
					dx == state.dx && dy == state.dy && 
					ox == state.ox && oy == state.oy;
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			int out = ar;
			out = out * col_count + ac;
			out = out * row_count + br;
			out = out * col_count + bc;
			out = out * row_count + cr;
			out = out * col_count + cc;
			out = out * row_count + dr;
			out = out * col_count + dc;
			out = out * row_count + or;
			out = out * col_count + oc;
			return out;
		}

		@Override
		public Object clone() throws CloneNotSupportedException {
			MazeState out = (MazeState) super.clone();

			out.ar = ar;
			out.ac = ac;
			out.br = br;
			out.bc = bc;
			out.cr = cr;
			out.cc = cc;
			out.dr = dr;
			out.dc = dc;
			out.or = or;
			out.oc = oc;

			return out;
		}

	}

}

