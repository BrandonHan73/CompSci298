package policy;

import environment.*;

import base.*;
import util.*;
import network.*;

public class A2C_Policy extends EpsilonGreedy {

	private NeuralNetwork value_network;
	private NeuralNetwork[] policy_network;

	private boolean needs_pretraining;
	AllStatesDefinedPolicy target_critic;

	public A2C_Policy(Game game) {
		super(game);

		int player_count = game.player_count();
		int param_count = game.get_state().parameter_count();
		needs_pretraining = false;
		target_critic = null;

		value_network = new FullRangeNetwork(param_count, 5, 5, 5, 5, player_count);

		policy_network = new SoftMax[player_count];
		for(int player = 0; player < player_count; player++) {
			policy_network[player] = new SoftMax(param_count, 5, 5, 5, game.get_possible_actions(player).length);
		}
	}

	public A2C_Policy(Game game, AllStatesDefinedPolicy value_target) {
		this(game);
		needs_pretraining = true;
		target_critic = value_target;
	}

	@Override
	public void train() {
		if(needs_pretraining) {
			PretrainCriticNetwork trainer = new PretrainCriticNetwork(base_game, target_critic);
			trainer.train(Config.a2c_critic_pretrain_iterations);
			value_network = trainer.get_trained();
		}
		train(Config.A2C_iterations);
	}

	public void step(State curr, ActionSet action, State next, double[] reward, ActionDistribution[] probabilities) {
		int player_count = base_game.player_count();

		double[] prob = new double[player_count];
		for(int player = 0; player < player_count; player++) {
			prob[player] = probabilities[player].get(action.get(player));
		}
		for(double p : prob) {
			if(p == 0) {
				throw new RuntimeException("Action with zero probability provided");
			}
		}

		double[] curr_param = curr.to_double_array();
		double[] next_param = next.to_double_array();

		Log.log(A2C_log_name, curr_param[0] + " " + curr_param[1] + " " + curr_param[2] + " " + curr_param[3]);
		Log.log(A2C_log_name, next_param[0] + " " + next_param[1] + " " + next_param[2] + " " + next_param[3]);
		Log.log(A2C_log_name, action.get(0) + " " + action.get(1));
		Log.log(A2C_log_name, "Rewards: " + reward[0] + " " + reward[1]);
		Log.log(A2C_log_name, "Action probabilities: "+ (int) (100 * prob[0]) + "% " + (int) (100 * prob[1]) + "%");

		double[] curr_value = value_network.pass(curr_param);
		double[] next_value = value_network.pass(next_param);

		double[] advantage = new double[player_count];
		double[] critic_target = new double[player_count];
		for(int player = 0; player < player_count; player++) {
			critic_target[player] = reward[player] + Config.Beta * next_value[player];
			advantage[player] = critic_target[player] - curr_value[player];
		}
		Log.log(A2C_log_name, "Advantage: " + advantage[0] + " " + advantage[1]);
		Log.log(A2C_log_name, "   Target: " + critic_target[0] + " " + critic_target[1]);
		Log.log(A2C_log_name, "Current value: " + curr_value[0] + " " + curr_value[1]);
		Log.log(A2C_log_name, "   Next value: " + next_value[0] + " " + next_value[1]);

		double[] critic_loss_gradient = new double[player_count];
		for(int player = 0; player < player_count; player++) {
			critic_loss_gradient[player] = curr_value[player] - critic_target[player];
		}
		Log.log(A2C_log_name, "Critic gradient: " + critic_loss_gradient[0] + " " + critic_loss_gradient[1]);
		if(!needs_pretraining) {
			value_network.backpropogate(curr_param, critic_loss_gradient);
		}

		for(int player = 0; player < player_count; player++) {
			Enum[] choices = base_game.get_possible_actions(player);

			double[] gradient = new double[choices.length];
			for(Enum a : curr.choices_for(player)) {
				gradient[a.ordinal()] = 1 + Math.log(probabilities[player].get(a));
			}
			gradient[ action.get(player).ordinal() ] -= advantage[player] / prob[player];

			Log.log(A2C_log_name, "Player " + player + " actor gradient: " + gradient[0] + " " + gradient[1] + " " + gradient[2] + " " + gradient[3]);
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

