
import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		// CrashGameTest.base();
		test_games(args);
	}

	public static void test_games(String[] args) {

		Utility.println(System.out);
		Utility.println(System.out, "----------------------------------");
		Utility.println(System.out, "| Rock Paper Scissors            |");
		Utility.println(System.out, "----------------------------------");
		evaluate(new RockPaperScissors());

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
		Utility.println(System.out, "Using random policy");

		Main.test(game, new RandomPolicy(game), games, cycles);

		Utility.println(System.out);
		Utility.println(System.out, "Using trained policy");

		Main.test(game, new Policy(game), games, cycles);

	}

	public static void runRockPaperScissors(String[] args) throws InterruptedException {
		RockPaperScissors game = new RockPaperScissors();

		Utility.println(System.out, "Rock Paper Scissors");

		Config.debug = false;

		Policy pol = new Policy(game);
		pol.train();

		Config.debug = true;

		Utility.println(System.out, "-------------------");

		double[][] possibilities;
		int[] actions;
		double[] rewards;
		char escCode = 0x1B;
		for(int iteration = 1; true; iteration++) {

			Utility.println(System.out, "Cycle " + iteration);

			possibilities = pol.get_action_options(game.get_state());
			actions = NashSolver.evaluate_options(possibilities);
			rewards = game.update(actions);

			Utility.print(System.out, "Action pair: ");
			Utility.print(System.out, actions);

			Utility.println(System.out, String.format("                                                          %c[A", escCode));
			Utility.print(System.out, "P1 options: ");
			Utility.print(System.out, possibilities[0]);

			Utility.println(System.out, String.format("                                                          %c[A", escCode));
			Utility.print(System.out, "P2 options: ");
			Utility.print(System.out, possibilities[1]);

			Utility.println(System.out, String.format("                                                          %c[A", escCode));
			Utility.print(System.out, "Reward pair: ");
			Utility.print(System.out, rewards);

			TimeUnit.SECONDS.sleep(1);

			for(int i = 0; i < 5; i++) {
				Utility.print(System.out, String.format("%c[A", escCode));
			}
		}
	}

}

