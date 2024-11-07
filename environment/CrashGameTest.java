package environment;

import java.util.concurrent.TimeUnit;

import base.Config;
import base.Position;
import base.Utility;
import policy.ActionDistribution;
import policy.DiscreteGamePolicy;
import policy.Policy;
import policy.NashSolver;

public class CrashGameTest {

	public static final double[][] reward1 = new double[][] {
			{ 0, 0, 1, 0, 1, 0, 0 },
			{ 0, 1, 2, 1, 2, 1, 0 },
			{ 0, 2, 4, 3, 2, 2, 1 },
			{ 0, 1, 2, 1, 2, 1, 0 },
			{ 0, 0, 1, 0, 1, 0, 0 }
	};

	public static void zero_sum_base() throws InterruptedException {
		// CrashGame game = new CrashGame(5, 7);
		ZeroSumCrashGame game = new ZeroSumCrashGame(reward1);
		DiscreteGamePolicy pol = new DiscreteGamePolicy(game);

		pol.train();

		idle(game, pol);
	}

	public static void base() throws InterruptedException {
		// CrashGame game = new CrashGame(5, 7);
		CrashGame game = new CrashGame(reward1);
		DiscreteGamePolicy pol = new DiscreteGamePolicy(game);

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
		DiscreteGamePolicy pol = new DiscreteGamePolicy(game);

		pol.train();

		game.print(System.out);

		ActionDistribution[] choices;
		for(int iteration = 0; iteration < 16; iteration++) {
			Utility.println(System.out);

			game = new CrashGame(game, 
				new Position(0, 1), 
				new Position(1, 2)
			);

			choices = pol.get_action_options(game.get_state());

			Utility.println(System.out, "For ", game.get_state());
			Utility.print(System.out, "Truck: ", choices[0]);
			Utility.println(System.out);

			Utility.print(System.out, "  Car: ", choices[1]);
			Utility.println(System.out);

			game = new CrashGame(game, 
				new Position(0, 2), 
				new Position(1, 1)
			);

			choices = pol.get_action_options(game.get_state());

			Utility.println(System.out, "For ", game.get_state());
			Utility.print(System.out, "Truck: ", choices[0]);
			Utility.println(System.out);

			Utility.print(System.out, "  Car: ", choices[1]);
			Utility.println(System.out);

			pol.train(1);
		}

	}

	public static void test_fictitious_play() throws InterruptedException {
		CrashGame game = new CrashGame(reward1);

		// CrashGame game = new CrashGame(5, 7);
		DiscreteGamePolicy pol = new DiscreteGamePolicy(game);

		pol.train();

		ActionDistribution[] choices = pol.get_action_options(game.get_state());

		game.print(System.out);

	}

	public static void idle(CrashGame game, Policy pol) throws InterruptedException {
		ActionDistribution[] possibilities;
		ActionSet actions;
		double[] rewards;
		char escCode = 0x1B;
		for(int iteration = 1; true; iteration++) {

			Utility.println(System.out);
			Utility.println(System.out, "Iteration ", iteration);

			// Config.debug = false;
			possibilities = pol.get_action_options(game.get_state());
			actions = NashSolver.evaluate_options(possibilities);
			rewards = game.update(actions);

			game.print(System.out);

			Utility.clearln(System.out);
			Utility.print(System.out, "Action pair: ");
			print_action_list(actions);
			Utility.println(System.out);

			Utility.clearln(System.out);
			Utility.println(System.out, "Truck options: ", possibilities[0]);

			Utility.clearln(System.out);
			Utility.println(System.out, "  Car options: ", possibilities[1]);

			Utility.clearln(System.out);
			Utility.println(System.out, "  Reward pair: ", rewards);

			TimeUnit.SECONDS.sleep(1);

			for(int i = 0; i < 20; i++) {
				Utility.print(System.out, String.format("%c[A", escCode));
			}
		}
	}

	public static void idle(ZeroSumCrashGame game, Policy pol) throws InterruptedException {
		ActionDistribution[] possibilities;
		ActionSet actions;
		double[] rewards;
		char escCode = 0x1B;
		for(int iteration = 1; true; iteration++) {

			Utility.println(System.out);
			Utility.println(System.out, "Iteration ", iteration);

			// Config.debug = false;
			possibilities = pol.get_action_options(game.get_state());
			actions = NashSolver.evaluate_options(possibilities);
			rewards = game.update(actions);

			game.print(System.out);

			Utility.clearln(System.out);
			Utility.print(System.out, "Action pair: ");
			print_action_list(actions);
			Utility.println(System.out);

			Utility.clearln(System.out);
			Utility.println(System.out, "Truck options: ", possibilities[0]);

			Utility.clearln(System.out);
			Utility.println(System.out, "  Car options: ", possibilities[1]);

			Utility.clearln(System.out);
			Utility.println(System.out, "  Reward pair: ", rewards);

			TimeUnit.SECONDS.sleep(1);

			for(int i = 0; i < 20; i++) {
				Utility.print(System.out, String.format("%c[A", escCode));
			}
		}
	}

	public static void print_action_list(ActionSet as) {
		Utility.println(System.out,
			action_string(as.get(0)),
			", ", 
			action_string(as.get(1))
		);
	}

	public static String action_string(int action) {
		switch(action) {
			case 0: return "Up";
			case 1: return "Right";
			case 2: return "Down";
			case 3: return "Left";
			default: return "Invalid";
		}
	}

}

