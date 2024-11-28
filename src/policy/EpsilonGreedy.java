package policy;

import base.Config;
import base.State;
import environment.*;

public abstract class EpsilonGreedy extends Policy {

	public EpsilonGreedy(Game base) {
		super(base);
	}

	@Override
	public void train_step() {
		Game game = base_game.get_random_copy();
		for(int time = 0; time < Config.DQN_simulation_time; time++) {
			State curr = game.get_state();

			ActionDistribution[] choices = evaluate(curr);
			ActionSet action = new ActionSet(choices, curr);
			for(int player = 0; player < game.player_count(); player++) {
				if(Math.random() < Config.epsilon) {
					ActionDistribution greedy = new ActionDistribution(game.get_possible_actions()[player]);
					action.set(player, greedy.poll());
				}
			}

			double[] prob = new double[game.player_count()];
			for(int player = 0; player < game.player_count(); player++) {
				prob[player] = 
					(1 - Config.epsilon) * choices[player].get(action.get(player)) + 
					(Config.epsilon * game.get_possible_actions()[player].size())
				;
			}

			double[] reward = game.update(action);
			State next = game.get_state();

			step(curr, action, next, reward, prob);
		}
	}

	public abstract void step(State curr, ActionSet action, State next, double[] reward, double[] prob);

}

