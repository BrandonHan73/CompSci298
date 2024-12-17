package policy;

import java.util.*;

import util.*;
import base.*;
import environment.*;

public abstract class DiscreteGamePolicy extends AllStatesDefinedPolicy {

	private Enum[][] action_choices;

	private Map<State, StateQ> Q;

	/**
	 * Creates a policy for the given game. Future training will
	 * be done on copies of the passed game. 
	 *
	 * @param game The game to train on. 
	 */
	public DiscreteGamePolicy(Game game) {
		super(game);

		Q = new HashMap<>();

		action_choices = new Enum[game.player_count()][];
		for(int player = 0; player < game.player_count(); player++) {
			action_choices[player] = game.get_possible_actions(player);
		}

		for(State s : get_possible_states()) {
			Q.put(s, new StateQ(s));
		}

		for(State curr : possible_states) {
			Utility.forEachChoice(action_choices, choice -> {
				Game sim = get_base_copy(curr.get_copy());
				ActionSet action = new ActionSet(choice, curr.get_copy());
				double[] rewards = sim.update(action);
				State next = sim.get_state().get_copy();

				Q.get(curr).update(action, rewards, Q.get(next));
			});
		}
	}

	@Override
	public StateQ get_Q(State state) {
		if(state == null) throw new RuntimeException("State must not be null");
		return Q.get(state);
	}

	@Override
	public void train_with_state(State state) {
		MaxRecord max_change = new MaxRecord();
		StateQ state_Q = Q.get(state);
		Utility.forEachChoice(action_choices, choice -> {
			ActionSet actions = new ActionSet(choice, state);
			max_change.record(state_Q.update(actions));
		});
		Log.log("Q_training", "Changed by " + max_change.get() + " (" + state.toString() + ")");
	}

}

