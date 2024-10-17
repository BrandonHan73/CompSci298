package base;

public class Config {

	public static int fictitious_play_iterations = 512;
	public static int fast_fictitious_play_iterations = 128;
	public static int Q_iterations = 128;

	public static boolean use_pure_nash_optimization = true;
	public static boolean use_action_distribution = true;

	// Base learning rate for gradient descent
	public static double alpha = 0.4;

	// Discount for Q learning
	public static double Beta = 0.4;

	// Fictitious play base ratio
	public static double gamma = 0.5;

	public static boolean debug = true;

}

