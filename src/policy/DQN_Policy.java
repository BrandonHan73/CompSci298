package policy;

import environment.ActionDistribution;
import policy.NashSolver;
import policy.StateQ;

public class DQN_Policy extends Policy {

	private LogisticRegression network;

	public DQN_Policy(Game base) {
		super(base);
	}

	public StateQ evaluate_Q(State state) {
		double[] network_eval = network.pass(state.to_double_array());

		StateQ output = new StateQ(state);

		int i = 0;
		Utility.forEachChoice(state.choices(), actions -> {
			ActionSet as = new ActionSet(actions, state);

			for(int player = 0; player < state.player_count(); player++) {
				output.set(network_eval[i], as, player);
				i++;
			}
		});

		return output;
	}

	@Override
	public void train() {
		train(Config.DQN_iterations);
	}

	@Override 
	public void train(int iterations) {
		for(int iteration = 0; iteration < iterations; iteration++) {
			Game sim = get_randomized();

			for(int time = 0; time < Config.DQN_simulation_time; time++) {
				State start_state = sim.get_state();

				StateQ Q = evaluate_Q(start_state);
				ActionDistribution[] options = NashSolver.evaluate_state(Q, true);
				for(int player = 0; player < start_state.player_count(); player++) {
					if(Math.random() < Config.epsilon) {
						options[player] = new ActionDistribution(start_state.choices_for(player));
					}
				}
				ActionSet decision = new ActionSet(options, start_state);

				double[] rewards = sim.update(decision);
				State new_state = sim.get_state();

				StateQ new_Q = evaluate_Q(new_state);
				double[] new_value = new_Q.value(true);
			}
		}
	}

	@Override
	public ActionDistribution[] get_action_options(State state) {
		StateQ state_q = evaluate_Q(state);

		return NashSolver.evaluate_state(state_q);
	}

}

