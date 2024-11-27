package policy;

import environment.*;
import base.*;

public class RandomPolicy extends Policy {

	public RandomPolicy(Game g) {
		super(g);
	}

	@Override
	public ActionDistribution[] evaluate(State state) {
		ActionDistribution[] out = new ActionDistribution[state.player_count()];

		for(int player = 0; player < state.player_count(); player++) {
			out[player] = new ActionDistribution(state.choices_for(player));
		}

		return out;
	}

	@Override
	public void train_step() {}

}

