package game;

import base.Config;
import policy.Policy;
import policy.DiscreteGamePolicy;

public class CycleGameTest {

	public static void base() {

		CycleGame game = new CycleGame();
		Policy pol = new DiscreteGamePolicy(game);

		Config.debug = true;
		pol.train();

	}

}

