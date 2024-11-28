package policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import base.*;
import util.*;
import environment.*;

/**
 * Various library functions for Q learning. 
 *
 * @author Brandon Han
 */
public class NashSolver {

	public static ActionDistribution[] evaluate_state(StateQ Q) {
		return evaluate_state(Q, false);
	}
	
	/**
	 * Takes the Q function for a given state and determines a set of actions
	 * for each player. The output will correspond to the Nash equilibriums of
	 * the given state that results in the highest sum of Q values for both
	 * players. If no Nash equilibriums exist, then the fictitious play algorithm
	 * will be applied instead. 
	 *
	 * @param Q The Q function for the given state.
	 */
	public static ActionDistribution[] evaluate_state(StateQ Q, boolean fast) {
		int player_count = Q.state.player_count();

		ActionSet[] all_nash = basic_nash(Q);
		ActionSet[] best_nash;
		
		ActionDistribution[] out = null;

		if(all_nash.length > 0 && Config.use_pure_nash_optimization) {
			best_nash = pick_nash(Q, all_nash);
			out = distribution_from_nash(player_count, best_nash);
		} else {
			out = fast ? fast_fictitious_play(Q) : fictitious_play(Q);
		}

		return out;
	}

	public static ActionDistribution[] distribution_from_nash(int player_count, ActionSet[] nash) {
		ActionDistribution[] out = new ActionDistribution[player_count];
		for(int i = 0; i < player_count; i++) {
			out[i] = new ActionDistribution();
		}

		for(ActionSet as : nash) {
			for(int i = 0; i < player_count; i++) {
				out[i].add(as.get(i));
			}
		}
		return out;
	}

	/**
	 * Finds all pure Nash equilibriums for the current state. Returns an array of
	 * action pairs. 
	 *
	 * @param Q The Q function for the current state.
	 */
	public static ActionSet[] basic_nash(StateQ Q) {
		Set<ActionSet> nash = new HashSet<>();

		Set<Integer>[] action_options = Q.state.choices();

		Map<ActionSet, ActionDistribution>[] best_responses = best_responses(Q);

		Set<ActionSet>[] choices = new Set[Q.state.player_count()];

		for(int player = 0; player < Q.state.player_count(); player++) {
			choices[player] = new HashSet<>();

			final int player_ = player;
			Utility.forEachChoice(action_options, action -> {
				ActionSet actions = new ActionSet(action, Q.state);

				ActionDistribution distribution = best_responses[player_].get(actions);

				if(distribution == null) {
					throw new RuntimeException("Best response did not provide response to " + actions);
				}
				for(int a : Q.state.choices_for(player_)) {
					if(distribution.get(a) > 0) {
						ActionSet as = new ActionSet(actions);
						as.set(player_, a);

						choices[player_].add(as);
					}
				}
			}, player);

		}

		nash.addAll(choices[0]);
		Set<ActionSet> keep = new HashSet<>();
		for(int player = 1; player < Q.state.player_count(); player++) {
			for(ActionSet action : nash) {
				if(choices[player].contains(action)) {
					keep.add(action);
				}
			}
			nash = keep;
		}

		return Utility.toArray(nash);
	}

	/**
	 * Takes an array of action pairs that lead to Nash equilibriums for the given
	 * Q function. Determines which Nash equilibrium result in the largest sum of
	 * Q values. 
	 *
	 * @param Q The Q function for the current state. 
	 * @param action_pairs The action pairs that correspond to Nash equilibrium.
	 */
	public static ActionSet[] pick_nash(StateQ Q, ActionSet[] nash) {
		if(Config.nash_take_highest_sum_of_Q) {
			int[] choices = Utility.argmax(0, nash.length, i -> {
				double[] eval = Q.get(nash[i]);
				double sum = 0;
				for(double d : eval) {
					sum += d;
				}
				return sum;
			});

			ActionSet[] out = null;
			int[] argmax;
			if(Config.pick_one_pure_nash) {
				argmax = Utility.argmax(0, choices.length, i -> nash[choices[i]].hashCode());
				out = new ActionSet[] { nash[choices[argmax[0]]] };
			} else {
				out = new ActionSet[choices.length];
				for(int i = 0; i < choices.length; i++) {
					out[i] = nash[choices[i]];
				}
			}

			// int index;
			// if(Config.pick_one_pure_nash) {
			// final ActionSet[] out_ = out;
			// choices = Utility.argmax(0, out.length, i -> out_[i].hashCode());
			// out = new ActionSet[] { out[choices[0]] };
			// }

			return out;
		} else {
			return nash;
		}
	}

	/**
	 * Determines the best response for every action in the current state. Returns an array
	 * of action lists. The first dimension is the player. The second dimension is the action
	 * that the opponent takes. The result is an array containing the best possible actions 
	 * for the current player and opponent's action. 
	 *
	 * @param Q The Q function for the current state.
	 * @param action_count The number of possible actions for each player. 
	 */
	public static Map<ActionSet, ActionDistribution>[] best_responses(StateQ Q) {
		Map<ActionSet, ActionDistribution>[] out = new Map[Q.state.player_count()];

		Set<Integer>[] action_options = Q.state.choices();

		for(int pl = 0; pl < Q.state.player_count(); pl++) {
			Set<Integer> current_player_options = action_options[pl];

			out[pl] = new HashMap<>();

			final int pl_ = pl;
			Utility.forEachChoice(action_options, choices -> {
				ActionSet action_set = new ActionSet(choices, Q.state);

				Set<Integer> select = Utility.argmax(
					current_player_options,
					i -> {
						ActionSet as = new ActionSet(action_set);
						as.set(pl_, i);
						return Q.get(as, pl_);
					}
				);

				ActionDistribution build = new ActionDistribution();
				for(int sel : select) {
					build.add(sel);
				}

				action_set.set(pl_, Config.placeholder_action);
				out[pl_].put(action_set, build);
			}, pl);

		}

		return out;
	}

	/**
	 * Takes an array of action choices and makes a random decision for each
	 * player. The input array's first dimension represents which player. The result
	 * of action[ployer] is an array of action distributions. 
	 *
	 * @param actions The array of action distributions. 
	 */
	public static ActionSet evaluate_options(ActionDistribution[] options) {
		int[] result = new int[options.length];

		for(int i = 0; i < result.length; i++) {
			result[i] = options[i].poll();
		}

		return new ActionSet(result, null);
	}

	public static ActionDistribution[] fictitious_play(StateQ Q) {
		return fictitious_play(Q, Config.fictitious_play_iterations);
	}

	public static ActionDistribution[] fast_fictitious_play(StateQ Q) {
		return fictitious_play(Q, Config.fast_fictitious_play_iterations);
	}

	public static ActionDistribution[] react_to_actions(StateQ Q, ActionDistribution[] action_counts) {
		Map<ActionSet, ActionDistribution>[] response = best_responses(Q);

		final Set<Integer>[] action_options = Q.state.choices();
		ActionDistribution[] reaction = new ActionDistribution[Q.state.player_count()];
		Set<Integer> current_player_choices;

		for(int player = 0; player < Q.state.player_count(); player++) {
			reaction[player] = new ActionDistribution();

			current_player_choices = action_options[player];

			final int player_ = player;
			final Map<ActionSet, ActionDistribution> current_best_response = response[player];
			final Set<Integer> current_player_choices_ = current_player_choices;
			final ActionDistribution current_reaction = reaction[player];
			Utility.forEachChoice(action_options, action -> {
				ActionSet as = new ActionSet(action, Q.state);
				double probability = 1;

				for(int opponent = 0; opponent < Q.state.player_count(); opponent++) {
					if(player_ == opponent) continue;

					probability *= action_counts[opponent].get(as.get(opponent));
				}

				for(int curr : current_player_choices_) {
					current_reaction.add(curr, probability * current_best_response.get(as).get(curr));
				}
			}, player);
		}

		return reaction;
	}

	/**
	 * Runs the fictitious play algorithm for the current state. 
	 *
	 * @param Q The Q function for the current state.
	 * @param action_count The number of possible actions for each player. 
	 */
	public static ActionDistribution[] fictitious_play(StateQ Q, int iterations) {

		StringBuilder sb = new StringBuilder();
		sb.append("\n\n>>>>>>>>>>>>>>\nFictitious play\n");
		sb.append(Q.state).append('\n');
		for(int i = 0; i < 4; ++i) {
			for(int j = 0; j < 4; ++j) {
				ActionSet lol = new ActionSet(new int[] {i, j}, Q.state);
				sb.append(Q.get(lol, 0)).append(" ");
			}
			sb.append('\n');
		}
		sb.append("<<<<<<<<<<<<\n");
		Log.log(fictitious_play_name, sb.toString());

		ActionDistribution[] action_counts = new ActionDistribution[Q.state.player_count()];
		
		for(int player = 0; player < Q.state.player_count(); player++) {
			action_counts[player] = new ActionDistribution(Q.state.choices_for(player));
		}

		ActionDistribution[] reaction;

		MaxRecord max_change = null;
		boolean converged = false;

		for(int moves = 1; moves < iterations; moves++) {

			reaction = react_to_actions(Q, action_counts);

			ActionDistribution[] old_distribution = new ActionDistribution[Q.state.player_count()];
			for(int i = 0; i < Q.state.player_count(); i++) {
				old_distribution[i] = new ActionDistribution(action_counts[i]);
			}

			max_change = new MaxRecord();
			final ActionDistribution[] reaction_ = reaction;
			ActionDistribution[] action_counts_ = action_counts;
			Utility.forEachChoice(Q.state.choices(), action -> {
				for(int player = 0; player < Q.state.player_count(); player++) {
					action_counts_[player].add(action[player], reaction_[player].get(action[player]));
				}
			});

			for(int player = 0; player < Q.state.player_count(); player++) {
				for(int action : Q.state.choices_for(player)) {
					max_change.record(Math.abs(
						old_distribution[player].get(action) - action_counts[player].get(action)
					));
				}
			}

			if(max_change.get() == 0) {
				fictitious_play_convergence.success();
				fictitious_play_panic_rate.fail();
				converged = true;
				break;
			}
		}

		fictitious_play_panic_rate.success();
		if(!converged && Config.fictitious_play_panic) {
			for(int panic = 0; panic < Config.fictitious_play_panic_iterations; panic++) {
				ActionDistribution[] old_distribution = action_counts;

				action_counts = react_to_actions(Q, action_counts);

				max_change = new MaxRecord();
				for(int player = 0; player < Q.state.player_count(); player++) {
					for(int action : Q.state.choices_for(player)) {
						max_change.record(Math.abs(
							old_distribution[player].get(action) - action_counts[player].get(action)
						));
					}
				}

				if(max_change.get() == 0) {
					fictitious_play_convergence.success();
					fictitious_play_panic_convergence.success();
					converged = true;
					break;
				}
			}
			fictitious_play_panic_convergence.fail();
		}

		if(!converged) {
			fictitious_play_convergence.fail();
		}

		Log.log(fictitious_play_name, "");
		Log.log(fictitious_play_name, action_counts[0].toString());
		Log.log(fictitious_play_name, action_counts[1].toString());
		return action_counts;
	}

	private static final String fictitious_play_name = "fictitious_play";
	private static SuccessLogger fictitious_play_convergence = new SuccessLogger(fictitious_play_name, "Convergence rate");
	private static SuccessLogger fictitious_play_panic_rate = new SuccessLogger(fictitious_play_name, "Panic rate");
	private static SuccessLogger fictitious_play_panic_convergence = new SuccessLogger(fictitious_play_name, "Panic success rate");

}

