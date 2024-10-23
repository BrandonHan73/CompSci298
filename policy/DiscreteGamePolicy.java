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

		Map<State, double[]> value_table;

		Map<State, StateQ> Q_update;

		double max_change;

		int[] action_counts;

		for(int iteration = 0; iteration < iterations; iteration++) {
			Q_update = new HashMap<>();

			max_change = 0;

			value_table = new HashMap<>();

			for(State state : get_possible_states()) {
				action_counts = state.action_counts();

				StateQ state_update = new StateQ(state);
				final Map<State, double[]> value_table_ = value_table;
				Utility.forEachChoice(state.choices(), action -> {
					ActionSet as = new ActionSet(action, state.choices());

					Game sim = get_base_copy(state);
					double[] rewards = sim.update(as);

					State new_state = sim.get_state();
					double[] Q_evaluation;
						if(value_table_.containsKey(new_state)) {
							Q_evaluation = value_table_.get(new_state);
						} else {
							Q_evaluation = Q.get(new_state).value(true);
							value_table_.put(new_state, Q_evaluation);
						}

					for(int player = 0; player < state.player_count(); player++) {
						state_update.set(rewards[player] + Config.Beta * Q_evaluation[player], as, player);
					}

				});

				for(int p1_action = 0; p1_action < action_counts[0]; p1_action++) {
					for(int p2_action = 0; p2_action < action_counts[1]; p2_action++) {


					}
				}

				Q_update.put(state, state_update);
			}

			Q = Q_update;
			Utility.debugln(System.out, "Largest update: ", max_change);
		}

	}

	@Override
	public ActionDistribution[] get_action_options(State state) {
		return NashSolver.evaluate_state(Q.get(state));
	}

}

