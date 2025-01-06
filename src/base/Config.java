package base;

public class Config {

	public static int fictitious_play_iterations = 1 << 7;
	public static int fast_fictitious_play_iterations = 1 << 6;

	public static int action_distribution_max_count = -1;

	public static boolean fictitious_play_panic = false;
	public static int fictitious_play_panic_iterations = 16;

	public static int Q_iterations = 1 << 5;

	public static int A2C_iterations = 1 << 9;
	public static int a2c_critic_pretrain_iterations = 1 << 8;

	public static int epsilon_greedy_simulation_time = 1 << 8;

	public static int DQN_iterations = 2048;
	public static int DQN_simulation_time = 1 << 17;
	// Probability of taking a random action for epsilon-greedy
	public static double epsilon = 0.1;

	public static boolean use_pure_nash_optimization = false;
	public static boolean pick_one_pure_nash = false;
	public static boolean nash_take_highest_sum_of_Q = false;

	public static double gradient_descent_solver_learning_rate = 0.4;
	public static int gradient_descent_solver_iterations = 1 << 5;
	public static boolean use_gradient_decent_solver = false;

	// Base learning rate for gradient descent
	public static double alpha = 0.1;

	// Discount for Q learning
	public static double Beta = 0.99;

	public static double probability_distribution_tolerance = 0.00000001;

	public static boolean debug = true;

}

