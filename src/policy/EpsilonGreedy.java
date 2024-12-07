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
			for(int player = 0; player < game.player_count(); player++) {
				choices[player].set_epsilon(Config.epsilon);
			}

			ActionSet action = new ActionSet(choices, curr);
			double[] reward = game.update(action);
			State next = game.get_state().get_copy();

			step(curr, action, next, reward, choices);
		}
	}

	public abstract void step(State curr, ActionSet action, State next, double[] reward, ActionDistribution[] prob);

}

