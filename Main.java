import java.util.concurrent.TimeUnit;

import Jama.Matrix;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		LogisticRegression lr = new LogisticRegression(2, 5, 1, 3);

		for(int i = 0; i < 4096; i++) {
			lr.train(new double[] {0, 0}, new double[] {0, 0, 0});
			lr.train(new double[] {0, 1}, new double[] {0, 1, 1});
			lr.train(new double[] {1, 0}, new double[] {0, 1, 1});
			lr.train(new double[] {1, 1}, new double[] {1, 1, 0});
		}

		Utility.print(System.out, lr.pass(new double[] {0, 0}));
		Utility.print(System.out, lr.pass(new double[] {0, 1}));
		Utility.print(System.out, lr.pass(new double[] {1, 0}));
		Utility.print(System.out, lr.pass(new double[] {1, 1}));
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

			possibilities = pol.fictitious_play(game.get_state());
			actions = pol.evaluate(possibilities);
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

			possibilities = pol.fictitious_play(game.get_state());
			actions = pol.evaluate(possibilities);
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

