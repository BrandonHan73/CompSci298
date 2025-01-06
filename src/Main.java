import example.*;
import base.Config;
import environment.*;
import util.*;
import policy.*;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		test_games(args);
		CrashGameTest.base();
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
		// evaluate(new CrashGame(CrashGameTest.reward1));

		Utility.println();
		Utility.println("----------------------------------");
		Utility.println("| Crash Game                     |");
		Utility.println("----------------------------------");
		evaluate(new CrashGame(5, 7));

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

	public static void compare(Game game, Policy pol_1, Policy pol_2, int games, int cycles, int test) {
		long time = System.currentTimeMillis();

		double[] gain, reward = new double[] { 0, 0 };
		int expected_match = 0;
		int expectation_count = 0;

		for(int g = 0; g < games; g++) {
			game = game.get_random_copy();

			for(int c = 0; c < cycles; c++) {
				ActionDistribution[] dis_1 = pol_1.evaluate(game.get_state());
				ActionDistribution[] dis_2 = pol_2.evaluate(game.get_state());

				Log.log("distribution_compare", "-------------");
				for(int p = 0; p < game.player_count(); p++) {
					Log.log("distribution_compare", dis_1[p].toString());
					Log.log("distribution_compare", dis_2[p].toString());
					Log.log("distribution_compare", "");

					for(int te = 0; te < test; te++) {
						if(dis_1[p].poll() == dis_2[p].poll()) {
							expected_match++;
						}
						expectation_count++;
					}

				}

				gain = game.update(NashSolver.evaluate_options(Math.random() < 0.5 ? dis_1 : dis_2));
				reward[0] += gain[0];
				reward[1] += gain[1];
			}
		}
		reward[0] /= games;
		reward[1] /= games;

		double elapsed = (System.currentTimeMillis() - time) / 1000.0;

		Utility.println("  Match rate: ", (int) (100.0 * expected_match / expectation_count), "%");
		Utility.println("              ", expected_match , " / ", expectation_count);
		Utility.println("Elapsed time: ", elapsed, "s");
	}

	public static void evaluate(CrashGame game) {

		int games = 256, cycles = 512, test = 128;

		Utility.println();
		Utility.println("Using random actions");
		RandomPolicy random_policy = new RandomPolicy(game);
		Main.test(game, random_policy, games, cycles);

		Utility.println();
		Utility.println("Using discrete policy (fictitious play)");
		DiscreteGamePolicy discrete_policy = new CrashGamePolicy(game);
		Main.test(game, discrete_policy, games, cycles);

		Utility.println();
		Utility.println("Using discrete policy (gradient descent)");
		DiscreteGamePolicy gradient_descent_policy = new CrashGamePolicy(game);
		Config.use_gradient_decent_solver = true;
		Main.test(game, gradient_descent_policy, games, cycles);

		Utility.println();
		Utility.println("Comparing policies (fictitious play vs gradient descent)");
		Main.compare(game, discrete_policy, gradient_descent_policy, games, cycles, test);

		Utility.println();
		Utility.println("Using A2C");
		A2C_Policy a2c_policy = new A2C_Policy(game);
		Main.test(game, a2c_policy, games, cycles);

		Utility.println();
		Utility.println("Comparing policies (fictitious play vs A2C)");
		Main.compare(game, discrete_policy, a2c_policy, games, cycles, test);

		Utility.println();
		Utility.println("Using pretrained policy");
		A2C_Policy pretrained_a2c = new A2C_Policy(game, discrete_policy);
		// Main.test(game, pretrained_a2c, games, cycles);

	}

}

