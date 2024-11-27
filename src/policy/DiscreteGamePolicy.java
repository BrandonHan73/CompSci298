package policy;

import java.util.*;

import util.*;
import base.*;
import environment.*;

public abstract class DiscreteGamePolicy extends Q_Policy {

	protected State[] possible_states;

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

		for(State s : get_possible_states()) {
			Q.put(s, new StateQ(s));
		}
	}

	public abstract void load_possible_states();

	public State[] get_possible_states() {
		if(possible_states == null) {
			load_possible_states();
		}
		return possible_states;
	}

	@Override
	public StateQ get_Q(State state) {
		if(state == null) throw new RuntimeException("State must not be null");
		return Q.get(state);
	}

	@Override
	public void train_step() {
		Map<State, StateQ> Q_update = new HashMap<>();

		MaxRecord max_change = new MaxRecord();

		for(State state : get_possible_states()) {

			StateQ state_update = new StateQ(state);
			MaxRecord record_change = new MaxRecord();

			Utility.forEachChoice(state.choices(), action -> {
				ActionSet as = new ActionSet(action, state);

				Game sim = get_base_copy(state);
				double[] rewards = sim.update(as);

				State new_state = sim.get_state();
				double[] state_value = get_Q(new_state).value(true);

				for(int player = 0; player < state.player_count(); player++) {
					double old_Q_value = get_Q(state).get(as, player);
					double new_Q_value = rewards[player] + Config.Beta * state_value[player];

					record_change.record(
						Math.abs(old_Q_value - new_Q_value), () -> {
							ActionDistribution[] test_fictitious_play = NashSolver.evaluate_state(get_Q(new_state), true);
							StringBuilder builder = new StringBuilder("\n\n");
							builder.append(String.format("Change from %.4f to %.4f, ", old_Q_value, new_Q_value) + '\n');
							builder.append(as.toString() +	" at " + state.toString() + '\n');
							builder.append("Truck: " + test_fictitious_play[0] + '\n');
							builder.append("Car: " + test_fictitious_play[1] + '\n');
							builder.append("Reward (truck): ").append(rewards[0]).append('\n');
							builder.append("New state value ").append(state_value[0]).append('\n');

							Utility.forEachChoice(new_state.choices(), actions -> {
								ActionSet aset = new ActionSet(actions, new_state);
								builder.append(aset.toString()).append(" ");
								Game simul = get_base_copy(new_state);
								simul.update(aset);
								State next = simul.get_state();
								builder.append(get_Q(next).value()[0]).append('\n');
							});

							return builder.toString();
						}
					);

					state_update.set(new_Q_value, as, player);
				}

			});

			max_change.record(record_change);

			Q_update.put(state, state_update);
		}

		Log.log(discrete_Q_train_name, "Q update " + max_change.toString());
		if(max_change.get() == 0) {
			discrete_Q_train_convergence.success();
			return;
		}

		Q = Q_update;
	}
	private static final String discrete_Q_train_name = "Q_training";
	private static SuccessLogger discrete_Q_train_convergence = new SuccessLogger(discrete_Q_train_name, "Convergence rate");

}

