package base;

public class Config {

	public static int fictitious_play_iterations = 512;
	public static int fast_fictitious_play_iterations = 256;
	public static int Q_iterations = 64;

	public static boolean fictitious_play_panic = true;
	public static int fictitious_play_panic_iterations = 4096;

	public static final int placeholder_action = -1;

	public static boolean use_pure_nash_optimization = false;
	public static boolean pick_one_pure_nash = false;

	// Base learning rate for gradient descent
	public static double alpha = 0.4;

	// Discount for Q learning
	public static double Beta = 0.2;

	// Fictitious play base ratio
	public static double gamma = 0.5;

	public static boolean debug = true;

}

