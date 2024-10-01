
public class CycleGameTest {

	public static void base() {

		CycleGame game = new CycleGame();
		Policy pol = new DiscreteGamePolicy(game);

		Config.debug = true;
		pol.train();

	}

}

