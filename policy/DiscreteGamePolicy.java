package policy;

import java.util.HashMap;
import java.util.Map;

import base.Utility;
import base.Config;
import base.State;
import game.CrashGame;
import game.Game;

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
			Q.put(s, new StateQ(s.action_count()));
		}
	}

	@Override
	public void train(int iterations) {

		Game sim;
		double[] rewards;
		double[] Q_evaluation;
		State new_state;

		Map<State, double[]> value_table;

		Map<State, StateQ> Q_update;

		double max_change;
		double change;
		State change_state = null;
		int change_truck = -1, change_car = -1;

		StateQ state_update;
		int[] action_counts;

		for(int iteration = 0; iteration < iterations; iteration++) {
			Q_update = new HashMap<>();

			max_change = 0;

			value_table = new HashMap<>();

			for(State state : get_possible_states()) {
				action_counts = state.action_counts();
				state_update = new StateQ(state.action_count());

				for(int p1_action = 0; p1_action < action_counts[0]; p1_action++) {
					for(int p2_action = 0; p2_action < action_counts[1]; p2_action++) {

						sim = get_base_copy(state);
						rewards = sim.update(p1_action, p2_action);

						new_state = sim.get_state();
						if(value_table.containsKey(new_state)) {
							Q_evaluation = value_table.get(new_state);
						} else {
							Q_evaluation = NashSolver.value_of(Q.get(new_state), true);
							value_table.put(new_state, Q_evaluation);
						}

						state_update.set(rewards[P1] + Config.Beta * Q_evaluation[P1], p1_action, p2_action, P1);
						state_update.set(rewards[P2] + Config.Beta * Q_evaluation[P2], p1_action, p2_action, P2);

						change = Math.max(
							Math.abs(state_update.get(p1_action, p2_action, P2) - Q.get(state).get(p1_action, p2_action, P2)),
							Math.abs(state_update.get(p1_action, p2_action, P1) - Q.get(state).get(p1_action, p2_action, P1))
						);
						if(change > max_change) {
							max_change = change;
							change_state = state;
							change_truck = p1_action;
							change_car = p2_action;
						}

					}
				}

				Q_update.put(state, state_update);
			}

			Q = Q_update;
			if(max_change > 0) {
				Utility.debugln(System.out, 
					"Largest update: ", max_change, 
					" at ", change_state, 
					" doing ", change_truck, " and ", change_car
				);
			} else {
				Utility.println(System.out, "No change");
				break;
			}
		}

	}

	@Override
	public double[][] get_action_options(State state) {
		return NashSolver.evaluate_state(Q.get(state));
	}

}

