import java.util.concurrent.TimeUnit;

import Jama.Matrix;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		runCrashGame(args);
	}

	public static void runRockPaperScissors(String[] args) throws InterruptedException {
		RockPaperScissors game = new RockPaperScissors();

		System.out.println("Rock Paper Scissors");

		Policy pol = new Policy(game);
		pol.train();

		System.out.println("-------------------");

		int[][] possibilities;
		int[] actions;
		double[] rewards;
		char escCode = 0x1B;
		for(int iteration = 1; true; iteration++) {

			System.out.println("Cycle " + iteration);

			possibilities = pol.get_action_options(game.get_state());
			actions = FictitiousPlay.evaluate_options(possibilities);
			rewards = game.update(actions);

			System.out.print("Action pair: ");
			Utility.print(System.out, actions);

			System.out.println(String.format("                                                          %c[A", escCode));
			System.out.print("P1 options: ");
			Utility.print(System.out, possibilities[0]);

			System.out.println(String.format("                                                          %c[A", escCode));
			System.out.print("P2 options: ");
			Utility.print(System.out, possibilities[1]);

			System.out.println(String.format("                                                          %c[A", escCode));
			System.out.print("Reward pair: ");
			Utility.print(System.out, rewards);

			TimeUnit.SECONDS.sleep(1);

			for(int i = 0; i < 5; i++) {
				System.out.print(String.format("%c[A", escCode));
			}
		}
	}

	public static void runCrashGame(String[] args) throws InterruptedException {
		CrashGame game = new CrashGame(5, 5);

		Policy pol = new Policy(game);

		pol.train();

		int[][] possibilities;
		int[] actions;
		double[] rewards;
		char escCode = 0x1B;
		while(true) {

			System.out.println();

			possibilities = pol.get_action_options(game.get_state());
			actions = FictitiousPlay.evaluate_options(possibilities);
			rewards = game.update(actions);

			game.print(System.out);

			System.out.print("Action pair: ");
			Utility.print(System.out, actions);

			System.out.println(String.format("                                                          %c[A", escCode));
			System.out.print("Truck options: ");
			Utility.print(System.out, possibilities[0]);

			System.out.println(String.format("                                                          %c[A", escCode));
			System.out.print("Car options: ");
			Utility.print(System.out, possibilities[1]);

			System.out.println(String.format("                                                          %c[A", escCode));
			System.out.print("Reward pair: ");
			Utility.print(System.out, rewards);

			TimeUnit.SECONDS.sleep(1);

			for(int i = 0; i < 18; i++) {
				System.out.print(String.format("%c[A", escCode));
			}
		}
	}
}

