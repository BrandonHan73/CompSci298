
import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		// CrashGameTest.base();
		// CrashGameTest.test_train_cycle();
		CrashGameTest.test_fictitious_play();
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

