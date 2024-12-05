package policy;

import environment.*;

import base.*;
import util.*;
import network.LogisticRegression;
import network.SoftMax;

public class A2C_Policy extends EpsilonGreedy {

	LogisticRegression value_network;
	SoftMax[] policy_network;

	public A2C_Policy(Game game) {
		super(game);

		int player_count = game.player_count();
		int param_count = game.get_state().parameter_count();

		value_network = new LogisticRegression(param_count, 5, 3, player_count);

		policy_network = new SoftMax[player_count];
		for(int player = 0; player < player_count; player++) {
			policy_network[player] = new SoftMax(param_count, 5, 3, game.get_possible_actions(player).length);
		}
	}

	public void step(State curr, ActionSet action, State next, double[] reward, double[] prob) {
		int player_count = base_game.player_count();
		Log.log(A2C_log_name, curr.toString() + " to " + next.toString() + " using " + action.toString());
		Log.log(A2C_log_name, "Rewards: " + reward[0] + " " + reward[1]);
		Log.log(A2C_log_name, "Action probabilities: "+ prob[0] + " " + prob[1]);

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
		Log.log(A2C_log_name, "Advantage: " + advantage[0] + " " + advantage[1]);

		double[] critic_loss_gradient = new double[player_count];
		for(int player = 0; player < player_count; player++) {
			critic_loss_gradient[player] = critic_target[player] - curr_value[player];
		}
		Log.log(A2C_log_name, "Critic gradient: " + critic_loss_gradient[0] + " " + critic_loss_gradient[1]);
		value_network.backpropogate(curr_param, critic_loss_gradient);

		for(int player = 0; player < player_count; player++) {
			Enum[] choices = base_game.get_possible_actions(player);
			double[] gradient = new double[choices.length];
			gradient[ action.get(player).ordinal() ] = -advantage[player] / prob[player];
			policy_network[player].backpropogate(curr_param, gradient);
		}

		Log.log(A2C_log_name, "");
	}
	private static final String A2C_log_name = "a2c_training";

	public ActionDistribution[] evaluate(State state) {
		double[] parameters = state.to_double_array();

		ActionDistribution[] out = new ActionDistribution[state.player_count()];

		for(int player = 0; player < state.player_count(); player++) {
			Enum[] options = state.choices_for(player);
			out[player] = new ActionDistribution<>(options);

			double[] eval = policy_network[player].pass(parameters);

			for(int i = 0; i < options.length; i++) {
				out[player].add(options[i], eval[i]);
			}
		}

		return out;
	}

}

