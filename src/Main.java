import example.*;
import environment.*;
import util.*;
import policy.*;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		// CrashGameTest.base();
		test_games(args);
	}

	public static void test_games(String[] args) {

		// Utility.println();
		// Utility.println("----------------------------------");
		// Utility.println("| Rock Paper Scissors            |");
		// Utility.println("----------------------------------");
		// evaluate(new RockPaperScissors());

		Utility.println();
		Utility.println("----------------------------------");
		Utility.println("| Crash Game with custom rewards |");
		Utility.println("----------------------------------");
		evaluate(new CrashGame(CrashGameTest.reward1));

		Utility.println();
		Utility.println("----------------------------------");
		Utility.println("| Crash Game                     |");
		Utility.println("----------------------------------");
		// evaluate(new CrashGame(5, 7));

		Utility.println();
		Utility.println("----------------------------------");
		Utility.println("| Zero Sum Crash Game            |");
		Utility.println("----------------------------------");
		// evaluate(new ZeroSumCrashGame(CrashGameTest.reward1));
	}

	public static void test(Game game, Policy pol, int games, int cycles) {
		long time = System.currentTimeMillis();

		pol.train();

		double[] gain, reward = new double[] { 0, 0 };

		for(int g = 0; g < games; g++) {
			game = game.get_random_copy();

			for(int c = 0; c < cycles; c++) {
				gain = game.update(pol.poll(game.get_state()));
				reward[0] += gain[0];
				reward[1] += gain[1];
			}
		}
		reward[0] /= games;
		reward[1] /= games;

		double elapsed = (System.currentTimeMillis() - time) / 1000.0;

		Utility.println("     Rewards: ", reward);
		Utility.println("       Total: ", reward[0] + reward[1]);
		Utility.println("Elapsed time: ", elapsed, "s");
	}

	public static void evaluate(CrashGame game) {

		int games = 256, cycles = 512;

		Utility.println();
		Utility.println("Using random actions");
		RandomPolicy random_policy = new RandomPolicy(game);
		Main.test(game, random_policy, games, cycles);

		Utility.println();
		Utility.println("Using A2C");
		A2C_Policy a2c_policy = new A2C_Policy(game);
		// Main.test(game, a2c_policy, games, cycles);

		Utility.println();
		Utility.println("Using discrete policy");
		DiscreteGamePolicy discrete_policy = new CrashGamePolicy(game);
		Main.test(game, discrete_policy, games, cycles);

		Utility.println();
		Utility.println("Using pretrained policy");
		A2C_Policy pretrained_a2c = new A2C_Policy(game, discrete_policy);
		Main.test(game, pretrained_a2c, games, cycles);

	}

}

