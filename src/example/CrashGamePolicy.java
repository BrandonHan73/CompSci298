package example;

import policy.*;
import util.Position;
import base.*;

public class CrashGamePolicy extends DiscreteGamePolicy {

	public CrashGamePolicy(CrashGame game) {
		super(game);
	}

	public void load_possible_states() {
		CrashGame game = (CrashGame) base_game;

		int total = (game.rows * game.rows * game.cols * game.cols) - (game.rows * game.cols);
		possible_states = new State[total];

		int index = 0;

		for(int tr = 0; tr < game.rows; tr++) {
			for(int tc = 0; tc < game.cols; tc++) {
				for(int cr = 0; cr < game.rows; cr++) {
					for(int cc = 0; cc < game.cols; cc++) {
						if(tr == cr && tc == cc) continue;
						possible_states[index++] = new CrashGame.CrashGameState(
							game, 
							new Position(tr, tc), 
							new Position(cr, cc)
						);
					}
				}
			}
		}
	}

}

