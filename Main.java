import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		CrashGame game = new CrashGame(5, 5);
		game = new CrashGame(game, 1);

		game.print(System.out);

		CrashGamePolicy pol = new CrashGamePolicy(game);

		pol.train();

		int[] actions;
		double[] rewards;
		while(true) {
			System.out.println();

			actions = pol.evaluate(game.get_state());
			rewards = game.update(actions);

			game.print(System.out);

			System.out.print("Action pair: ");
			Main.print(System.out, actions);

			System.out.print("Reward pair: ");
			Main.print(System.out, rewards);

			TimeUnit.SECONDS.sleep(1);
		}
	}

	public static void print(PrintStream out, int[][] arr) {
		for(int[] row : arr) {
			for(int i = 0; i < row.length - 1; i++) {
				out.print(row[i] + ", ");
			}
			out.println(row[row.length - 1]);
		}
	}

	public static void print(PrintStream out, int[] arr) {
		for(int i = 0; i < arr.length - 1; i++) {
			out.print(arr[i] + ", ");
		}
		out.println(arr[arr.length - 1]);
	}

	public static void print(PrintStream out, double[][] arr) {
		for(double[] row : arr) {
			for(int i = 0; i < row.length - 1; i++) {
				out.print(row[i] + ", ");
			}
			out.println(row[row.length - 1]);
		}
	}

	public static void print(PrintStream out, double[] arr) {
		for(int i = 0; i < arr.length - 1; i++) {
			out.print(arr[i] + ", ");
		}
		out.println(arr[arr.length - 1]);
	}

	public static void print(PrintStream out, Position pos) {
		out.print("(" + pos.row + ", " + pos.col + ")");
	}

}

