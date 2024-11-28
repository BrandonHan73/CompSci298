package base;

public class Config {

	public static int fictitious_play_iterations = 128;
	public static int fast_fictitious_play_iterations = 64;

	public static boolean fictitious_play_panic = true;
	public static int fictitious_play_panic_iterations = 16;

	public static int Q_iterations = 128;

	public static int DQN_iterations = 512;
	public static int DQN_simulation_time = 64;
	// Probability of taking a random action for epsilon-greedy
	public static double epsilon = 0.01;

	public static final int placeholder_action = -1;

	public static boolean use_pure_nash_optimization = false;
	public static boolean pick_one_pure_nash = false;
	public static boolean nash_take_highest_sum_of_Q = false;

	// Base learning rate for gradient descent
	public static double alpha = 0.4;

	// Discount for Q learning
	public static double Beta = 0.9;

	// Fictitious play base ratio
	public static double gamma = 0.5;

	public static boolean debug = true;

}

