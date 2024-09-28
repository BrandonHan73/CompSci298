
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
		CrashGame game = new CrashGame(5, 7);
		// CrashGame game = new CrashGame(reward1);
		Policy pol = new Policy(game);

		Config.debug = false;

		pol.train();

		Config.debug = true;

		idle(game, pol);
	}

	public static void test_train_cycle() throws InterruptedException {
		CrashGame game = new CrashGame(reward1);
		game = (CrashGame) game.get_copy(613);

		// CrashGame game = new CrashGame(5, 7);
		Policy pol = new Policy(game);

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
		game = (CrashGame) game.get_copy(613);

		// CrashGame game = new CrashGame(5, 7);
		Policy pol = new Policy(game);

		Config.debug = false;

		pol.train();

		Config.debug = true;

		double[][] choices = pol.get_action_options(game.get_state());

		game.print(System.out);

	}

	public static void evaluate() {

		Config.debug = false;

		CrashGame game = new CrashGame(reward1);
		Policy pol;

		int games = 256, cycles = 512;

		double[] reward, gain;

		Config.use_pure_nash_optimization = true;

		Utility.println(System.out, "Using pure Nash optimization");
		pol = new Policy(game);
		pol.train();

		reward = new double[] { 0, 0 };

		for(int g = 0; g < games; g++) {
			game.randomize_positions();

			for(int c = 0; c < cycles; c++) {
				gain = game.update(pol.evaluate(game.get_state()));
				reward[0] += gain[0];
				reward[1] += gain[1];
			}
		}
		reward[0] /= games;
		reward[1] /= games;

		Utility.println(System.out, reward);

		Config.use_pure_nash_optimization = false;

		Utility.println(System.out, "Using only fictitious play");
		pol = new Policy(game);
		pol.train();

		reward = new double[] { 0, 0 };

		for(int g = 0; g < games; g++) {
			game.randomize_positions();

			for(int c = 0; c < cycles; c++) {
				gain = game.update(pol.evaluate(game.get_state()));
				reward[0] += gain[0];
				reward[1] += gain[1];
			}
		}
		reward[0] /= games;
		reward[1] /= games;

		Utility.println(System.out, reward);

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
			Config.debug = true;

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

			for(int i = 0; i < 23; i++) {
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

