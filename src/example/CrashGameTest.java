package example;

import java.util.concurrent.TimeUnit;

import base.*;
import util.*;
import policy.*;
import environment.*;

public class CrashGameTest {

	public static final double[][] reward1 = new double[][] {
			{ -5, -5, 0, -5, 0, -5, -5 },
			{ -5, 0, 1, 0, 1, 0, -5 },
			{ -5, 1, 4, 3, 1, 1, 0 },
			{ -5, 0, 1, 0, 1, 0, -5 },
			{ -5, -5, 0, -5, 0, -5, -5 }
	};

	static {
		for(int r = 0; r < reward1.length; r++) {
			for(int c = 0; c < reward1[r].length; c++) {
				reward1[r][c] += CrashGame.epsilon * (2 * Math.random() - 1);
			}
		}
	}

	public static void symmetric_base() throws InterruptedException {
		// CrashGame game = new CrashGame(5, 7);
		SymmetricGame game = new SymmetricGame(reward1);
		DiscreteGamePolicy pol = new CrashGamePolicy(game);

		pol.train();

		idle(game, pol);
	}

	public static void zero_sum_base() throws InterruptedException {
		// CrashGame game = new CrashGame(5, 7);
		ZeroSumCrashGame game = new ZeroSumCrashGame(reward1);
		DiscreteGamePolicy pol = new CrashGamePolicy(game);

		pol.train();

		idle(game, pol);
	}

	public static void base() throws InterruptedException {
		// CrashGame game = new CrashGame(5, 7);
		CrashGame game = new CrashGame(reward1);
		// DiscreteGamePolicy pol = new CrashGamePolicy(game);
		Policy pol = new A2C_Policy(game);

		pol.train();

		idle(game, pol);
	}

	public static void test_train_cycle() throws InterruptedException {
		CrashGame game = new CrashGame(reward1);
		game = new CrashGame(game, 
			new Position(0, 1), 
			new Position(1, 2)
		);

		// CrashGame game = new CrashGame(5, 7);
		DiscreteGamePolicy pol = new CrashGamePolicy(game);

		pol.train();

		game.print(System.out);

		ActionDistribution[] choices;
		for(int iteration = 0; iteration < 16; iteration++) {
			Utility.println();

			game = new CrashGame(game, 
				new Position(0, 1), 
				new Position(1, 2)
			);

			choices = pol.evaluate(game.get_state());

			Utility.println("For ", game.get_state());
			Utility.print("Truck: ", choices[0]);
			Utility.println();

			Utility.print("  Car: ", choices[1]);
			Utility.println();

			game = new CrashGame(game, 
				new Position(0, 2), 
				new Position(1, 1)
			);

			choices = pol.evaluate(game.get_state());

			Utility.println("For ", game.get_state());
			Utility.print("Truck: ", choices[0]);
			Utility.println();

			Utility.print("  Car: ", choices[1]);
			Utility.println();

			pol.train(1);
		}

	}

	public static void test_fictitious_play() throws InterruptedException {
		CrashGame game = new CrashGame(reward1);

		// CrashGame game = new CrashGame(5, 7);
		DiscreteGamePolicy pol = new CrashGamePolicy(game);

		pol.train();

		ActionDistribution[] choices = pol.evaluate(game.get_state());

		game.print(System.out);

	}

	public static void idle(SymmetricGame game, Policy pol) throws InterruptedException {
		ActionDistribution[] possibilities;
		ActionSet actions;
		double[] rewards;
		char escCode = 0x1B;
		for(int iteration = 1; true; iteration++) {

			Utility.println();
			Utility.println("Iteration ", iteration);

			// Config.debug = false;
			possibilities = pol.evaluate(game.get_state());
			actions = NashSolver.evaluate_options(possibilities);
			rewards = game.update(actions);

			game.print(System.out);

			Utility.clearln();
			Utility.print("Action pair: ");
			print_action_list(actions);
			Utility.println();

			Utility.clearln();
			Utility.println("Truck options: ", possibilities[0]);

			Utility.clearln();
			Utility.println("  Car options: ", possibilities[1]);

			Utility.clearln();
			Utility.println("  Reward pair: ", rewards);

			TimeUnit.SECONDS.sleep(1);

			for(int i = 0; i < 20; i++) {
				Utility.print(String.format("%c[A", escCode));
			}
		}
	}

	public static void idle(CrashGame game, Policy pol) throws InterruptedException {
		ActionDistribution[] possibilities;
		ActionSet actions;
		double[] rewards;
		char escCode = 0x1B;
		for(int iteration = 1; true; iteration++) {

			Utility.println();
			Utility.println("Iteration ", iteration);

			// Config.debug = false;
			possibilities = pol.evaluate(game.get_state());
			actions = NashSolver.evaluate_options(possibilities);
			rewards = game.update(actions);

			game.print(System.out);

			Utility.clearln();
			Utility.print("Action pair: ");
			print_action_list(actions);
			Utility.println();

			Utility.clearln();
			Utility.println("Truck options: ", possibilities[0]);

			Utility.clearln();
			Utility.println("  Car options: ", possibilities[1]);

			Utility.clearln();
			Utility.println("  Reward pair: ", rewards);

			TimeUnit.SECONDS.sleep(1);

			for(int i = 0; i < 20; i++) {
				Utility.print(String.format("%c[A", escCode));
			}
		}
	}

	public static void idle(ZeroSumCrashGame game, Policy pol) throws InterruptedException {
		ActionDistribution[] possibilities;
		ActionSet actions;
		double[] rewards;
		char escCode = 0x1B;
		for(int iteration = 1; true; iteration++) {

			Utility.println();
			Utility.println("Iteration ", iteration);

			// Config.debug = false;
			possibilities = pol.evaluate(game.get_state());
			actions = NashSolver.evaluate_options(possibilities);
			rewards = game.update(actions);

			game.print(System.out);

			Utility.clearln();
			Utility.print("Action pair: ");
			print_action_list(actions);
			Utility.println();

			Utility.clearln();
			Utility.println("Truck options: ", possibilities[0]);

			Utility.clearln();
			Utility.println("  Car options: ", possibilities[1]);

			Utility.clearln();
			Utility.println("  Reward pair: ", rewards);

			TimeUnit.SECONDS.sleep(1);

			for(int i = 0; i < 20; i++) {
				Utility.print(String.format("%c[A", escCode));
			}
		}
	}

	public static void print_action_list(ActionSet as) {
		Utility.println(as.get(0), ", ", as.get(1));
	}

}

