package base;

import environment.RockPaperScissors;
import environment.CrashGame;
import environment.CrashGameTest;
import environment.Game;

import policy.Policy;
import policy.RandomPolicy;
import policy.DiscreteGamePolicy;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		CrashGameTest.base();
		//test_games(args);
	}

	public static void test_games(String[] args) {

		// Utility.println(System.out);
		// Utility.println(System.out, "----------------------------------");
		// Utility.println(System.out, "| Rock Paper Scissors            |");
		// Utility.println(System.out, "----------------------------------");
		// evaluate(new RockPaperScissors());

		Utility.println(System.out);
		Utility.println(System.out, "----------------------------------");
		Utility.println(System.out, "| Crash Game                     |");
		Utility.println(System.out, "----------------------------------");
		evaluate(new CrashGame(5, 7));

		Utility.println(System.out);
		Utility.println(System.out, "----------------------------------");
		Utility.println(System.out, "| Crash Game with custom rewards |");
		Utility.println(System.out, "----------------------------------");
		evaluate(new CrashGame(CrashGameTest.reward1));

	}

	public static void test(Game game, Policy pol, int games, int cycles) {
		long time = System.currentTimeMillis();

		pol.train();

		double[] gain, reward = new double[] { 0, 0 };

		for(int g = 0; g < games; g++) {
			game.randomize();

			for(int c = 0; c < cycles; c++) {
				gain = game.update(pol.evaluate(game.get_state()));
				reward[0] += gain[0];
				reward[1] += gain[1];
			}
		}
		reward[0] /= games;
		reward[1] /= games;

		double elapsed = (System.currentTimeMillis() - time) / 1000.0;

		Utility.println(System.out, "     Rewards: ", reward);
		Utility.println(System.out, "       Total: ", reward[0] + reward[1]);
		Utility.println(System.out, "Elapsed time: ", elapsed, "s");
	}

	public static void evaluate(Game game) {

		int games = 256, cycles = 512;

		Utility.println(System.out);
		Utility.println(System.out, "Using random actions");

		Main.test(game, new RandomPolicy(game), games, cycles);

		Utility.println(System.out);
		Utility.println(System.out, "Using discrete policy");

		Main.test(game, new DiscreteGamePolicy(game), games, cycles);

	}

}

