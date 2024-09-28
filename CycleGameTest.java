
public class CycleGameTest {

	public static void base() {

		CycleGame game = new CycleGame();
		Policy pol = new Policy(game);

		Config.debug = true;
		pol.train();

	}

}

