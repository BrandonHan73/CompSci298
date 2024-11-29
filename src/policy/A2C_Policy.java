package policy;

import environment.*;
import base.*;
import network.LogisticRegression;
import network.SoftMax;

public class A2C_Policy extends EpsilonGreedy {

	LogisticRegression value_network;
	SoftMax[] policy_network;

	public A2C_Policy(Game game) {
		super(game);
	}

	public void step(State curr, ActionSet action, State next, double[] reward, double[] prob) {
		int player_count = base_game.player_count();

		double[] curr_param = curr.to_double_array();
		double[] next_param = next.to_double_array();

		double[] curr_value = value_network.pass(curr_param);
		double[] next_value = value_network.pass(next_param);

		double[] advantage = new double[player_count];
		double[] critic_target = new double[player_count];
		for(int player = 0; player < player_count; player++) {
			critic_target[player] = reward[player] + Config.Beta * next_value[player];
			advantage[player] = critic_target[player] - curr_value[player];
		}

		double[] critic_loss_gradient = new double[player_count];
		for(int player = 0; player < player_count; player++) {
			critic_loss_gradient[player] = critic_target[player] - curr_value[player];
		}
		value_network.backpropogate(curr_param, critic_loss_gradient);
	}

	public ActionDistribution[] evaluate(State state) {
		return null;
	}

}

