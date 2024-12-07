package base;

public class Config {

	public static int fictitious_play_iterations = 1 << 10;
	public static int fast_fictitious_play_iterations = 1 << 8;

	public static int action_distribution_max_count = 1 << 5;

	public static boolean fictitious_play_panic = false;
	public static int fictitious_play_panic_iterations = 16;

	public static int Q_iterations = 1 << 7;

	public static int A2C_iterations = 1 << 6;

	public static int DQN_iterations = 2048;
	public static int DQN_simulation_time = 1 << 18;
	// Probability of taking a random action for epsilon-greedy
	public static double epsilon = 0.01;

	public static boolean use_pure_nash_optimization = false;
	public static boolean pick_one_pure_nash = false;
	public static boolean nash_take_highest_sum_of_Q = false;

	// Base learning rate for gradient descent
	public static double alpha = 0.4;

	// Discount for Q learning
	public static double Beta = 0.4;

	public static double probability_distribution_tolerance = 0.00000001;

	public static boolean debug = true;

}

