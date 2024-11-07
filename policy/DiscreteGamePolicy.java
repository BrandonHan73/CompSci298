package policy;

import java.util.HashMap;
import java.util.Map;

import base.Utility;
import base.Config;
import base.State;
import environment.Game;
import environment.ActionSet;

public class DiscreteGamePolicy extends Policy {

	private Map<State, StateQ> Q;

	private final int P1 = 0, P2 = 1;

	/**
	 * Creates a policy for the given game. Future training will
	 * be done on copies of the passed game. 
	 *
	 * @param game The game to train on. 
	 */
	public DiscreteGamePolicy(Game game) {
		super(game);

		Q = new HashMap<>();

		for(State s : game.get_possible_states()) {
			Q.put(s, new StateQ(s));
		}
	}

	@Override
	public void train(int iterations) {

		Map<State, StateQ> Q_update;

		Utility.MaxRecord max_change;

		for(int iteration = 0; iteration < iterations; iteration++) {
			Q_update = new HashMap<>();

			max_change = new Utility.MaxRecord();

			for(State state : get_possible_states()) {

				StateQ state_update = new StateQ(state);
				Utility.MaxRecord record_change = new Utility.MaxRecord();
				Utility.forEachChoice(state.choices(), action -> {
					ActionSet as = new ActionSet(action, state);

					Game sim = get_base_copy(state);
					double[] rewards = sim.update(as);

					State new_state = sim.get_state();
					double[] state_value = Q.get(new_state).value(true);

					for(int player = 0; player < state.player_count(); player++) {
						double old_Q_value = Q.get(state).get(as, player);
						double new_Q_value = rewards[player] + Config.Beta * state_value[player];

						record_change.record(
							Math.abs(old_Q_value - new_Q_value), 
							() -> {
								ActionDistribution[] test_fictitious_play = NashSolver.evaluate_state(Q.get(new_state), true);

								return "\n" + 
								String.format("Change from %.4f to %.4f, ", old_Q_value, new_Q_value) + '\n' + 
								as.toString() +	" at " + state.toString() + '\n' +
								"Truck: " + test_fictitious_play[0] + '\n' +
								"Car: " + test_fictitious_play[1] + '\n';
							}
						);

						state_update.set(new_Q_value, as, player);
					}

				});

				max_change.record(record_change);

				Q_update.put(state, state_update);
			}

			Utility.println(System.out, "Q update ", max_change);
			if(max_change.get() == 0) {
				break;
			}

			Q = Q_update;
		}

	}

	@Override
	public ActionDistribution[] get_action_options(State state) {
		return NashSolver.evaluate_state(Q.get(state));
	}

}

