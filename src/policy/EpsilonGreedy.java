package policy;

import base.Config;
import base.State;
import util.Utility;
import environment.*;

public abstract class EpsilonGreedy extends Policy {

	public EpsilonGreedy(Game base) {
		super(base);
	}

	@Override
	public void train_step() {
		Game game = base_game.get_random_copy();
		for(int time = 0; time < Config.DQN_simulation_time; time++) {
			State curr = game.get_state().get_copy();

			ActionDistribution[] choices = evaluate(curr);
			ActionSet action = new ActionSet(choices, curr);
			double[] prob = new double[game.player_count()];

			for(int player = 0; player < game.player_count(); player++) {
				Enum[] possible_actions = game.get_possible_actions(player);
				if(Math.random() < Config.epsilon) {
					ActionDistribution greedy = new ActionDistribution(possible_actions);
					action.set(player, greedy.poll());
				}
				prob[player] = 
					(1 - Config.epsilon) * choices[player].get(action.get(player)) + 
					(Config.epsilon / possible_actions.length)
				;
			}

			double[] reward = game.update(action);
			// Clamping reward
			for(int player = 0; player < game.player_count(); player++) {
				reward[player] = Utility.logistic(reward[player]) * (1 - Config.Beta);
			}

			State next = game.get_state();

			step(curr, action, next, reward, prob);
		}
	}

	public abstract void step(State curr, ActionSet action, State next, double[] reward, double[] prob);

}

