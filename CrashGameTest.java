
import java.util.concurrent.TimeUnit;

public class CrashGameTest {

	public static final double[][] reward1 = new double[][] {
			{ 0, 0, 1, 0, 1, 0, 0 },
			{ 0, 1, 2, 1, 2, 1, 0 },
			{ 0, 2, 4, 3, 2, 2, 1 },
			{ 0, 1, 2, 1, 2, 1, 0 },
			{ 0, 0, 1, 0, 1, 0, 0 }
	};

	public static void base() throws InterruptedException {
		// CrashGame game = new CrashGame(5, 7);
		CrashGame game = new CrashGame(reward1);
		DiscreteGamePolicy pol = new DiscreteGamePolicy(game);

		Config.debug = false;

		pol.train();

		idle(game, pol);
	}

	public static void test_train_cycle() throws InterruptedException {
		CrashGame game = new CrashGame(reward1);

		// CrashGame game = new CrashGame(5, 7);
		DiscreteGamePolicy pol = new DiscreteGamePolicy(game);

		Config.debug = false;

		pol.train();

		Config.debug = true;

		game.print(System.out);

		double[][] choices;
		for(int iteration = 0; iteration < 16; iteration++) {
			Utility.println(System.out);

			Config.debug = false;
			choices = pol.get_action_options(game.get_state());
			Config.debug = true;

			Utility.print(System.out, "Truck: ", choices[0]);
			Utility.println(System.out);

			Utility.print(System.out, "  Car: ", choices[1]);
			Utility.println(System.out);

			Config.debug = false;
			pol.train(1);
			Config.debug = true;
		}

	}

	public static void test_fictitious_play() throws InterruptedException {
		CrashGame game = new CrashGame(reward1);

		// CrashGame game = new CrashGame(5, 7);
		DiscreteGamePolicy pol = new DiscreteGamePolicy(game);

		Config.debug = false;

		pol.train();

		Config.debug = true;

		double[][] choices = pol.get_action_options(game.get_state());

		game.print(System.out);

	}

	public static void idle(CrashGame game, Policy pol) throws InterruptedException {
		double[][] possibilities;
		int[] actions;
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

			for(int i = 0; i < 19; i++) {
				Utility.print(System.out, String.format("%c[A", escCode));
			}
		}
	}

	public static void print_action_list(int[] actions) {
		for(int i = 0; i < actions.length - 1; i++) {
			Utility.print(System.out, action_string(actions[i]), ", ");
		}
		Utility.print(System.out, action_string(actions[actions.length - 1]));
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

