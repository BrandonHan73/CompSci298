package policy;

import network.FullRangeNetwork;
import network.NeuralNetwork;
import environment.*;
import base.*;
import util.*;

public class PretrainCriticNetwork extends AllStatesDefinedPolicy {

	private int param_count, player_count;
	private NeuralNetwork value_network;
	private AllStatesDefinedPolicy target_policy;

	public PretrainCriticNetwork(Game base, AllStatesDefinedPolicy target) {
		super(base);
		param_count = base.get_state().parameter_count();
		player_count = base.player_count();

		target_policy = target;
		value_network = new FullRangeNetwork(param_count, 128, player_count);
	}

	public NeuralNetwork get_trained() {
		return value_network;
	}

	public void load_possible_states() {
		possible_states = target_policy.get_possible_states();
	}
	public StateQ get_Q(State state) {
		return target_policy.get_Q(state);
	}

	public void train_with_state(State state) {
		int player_count = base_game.player_count();
		StateQ Q = target_policy.get_Q(state);

		double[] param = state.to_double_array();
		double[] dCdy = new double[player_count];
		double[] out = value_network.pass(param);
		double[] value = Q.value();

		for(int player = 0; player < player_count; player++) {
			dCdy[player] = out[player] - value[player];
		}

		Log.log(log_name, String.format("  State: %f %f %f %f", param[0], param[1], param[2], param[3]));
		Log.log(log_name, String.format("   dCdy: %f %f", dCdy[0], dCdy[1]));
		Log.log(log_name, String.format("Network: %f %f", out[0], out[1]));
		Log.log(log_name, String.format(" Target: %f %f", value[0], value[1]));
		Log.log(log_name, "");
		value_network.backpropogate(param, dCdy);
	}
	private final static String log_name = "pretraining_log";

}

