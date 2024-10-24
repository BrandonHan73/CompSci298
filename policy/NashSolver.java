package policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import base.Config;
import base.Utility;
import environment.ActionSet;
import policy.ActionDistribution;

/**
 * Various library functions for Q learning. 
 *
 * @author Brandon Han
 */
public class NashSolver {

	private static final int P1 = 0, P2 = 1;

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
		int[][] action_choices = Q.action_choices;
		int player_count = action_choices.length;

		ActionSet[] all_nash = basic_nash(Q);
		ActionSet[] best_nash;
		
		ActionDistribution[] out = null;

		if(all_nash.length > 0 && Config.use_pure_nash_optimization) {

			best_nash = pick_nash(Q, all_nash);
			out = new ActionDistribution[player_count];
			for(int i = 0; i < player_count; i++) {
				out[i] = new ActionDistribution();
			}

			for(ActionSet as : best_nash) {
				for(int i = 0; i < player_count; i++) {
					out[i].add(as.get(i));
				}
			}

		} else {
			out = fast ? fast_fictitious_play(Q) : fictitious_play(Q);
		}

		return out;
	}

	/**
	 * Finds all pure Nash equilibriums for the current state. Returns an array of
	 * action pairs. 
	 *
	 * @param Q The Q function for the current state.
	 * @param action_count The number of possible actions for each player. 
	 */
	public static ActionSet[] basic_nash(StateQ Q) {
		Set<ActionSet> nash = new HashSet<>();

		Map<ActionSet, ActionDistribution>[] best_responses = best_responses(Q);

		Set<ActionSet>[] choices = new Set[Q.player_count];
		ActionSet as;
		ActionDistribution ad;
		for(int player = 0; player < Q.player_count; player++) {
			choices[player] = new HashSet<>();

			for(ActionSet actions : choices[player]) {
				ad = best_responses[player].get(actions);
				for(int a : Q.action_choices[player]) {
					if(ad.get(a) > 0) {
						as = new ActionSet(actions);
						as.set(player, a);

						choices[player].add(as);
					}
				}
			}
		}

		nash.addAll(choices[0]);
		Set<ActionSet> keep = new HashSet<>();
		for(int player = 1; player < Q.player_count; player++) {
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
		int[] choices = Utility.argmax(0, nash.length, i -> {
			double[] eval = Q.get(nash[i]);
			double sum = 0;
			for(double d : eval) {
				sum += d;
			}
			return sum;
		});

		ActionSet[] out = new ActionSet[choices.length];
		for(int i = 0; i < choices.length; i++) {
			out[i] = nash[choices[i]];
		}
		return out;
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
		Map<ActionSet, ActionDistribution>[] out = new Map[Q.player_count];

		int[][] action_options = Utility.copy(Q.action_choices);
		int[] temp_substitute = new int[] {-1};
		for(int pl = 0; pl < Q.player_count; pl++) {
			int[] current_player_options = action_options[pl];
			action_options[pl] = temp_substitute;

			out[pl] = new HashMap<>();

			final int pl_ = pl;
			Utility.forEachChoice(action_options, choices -> {
				ActionSet action_set = new ActionSet(choices, Q.action_choices);

				int[] select = Utility.argmax(
					0, current_player_options.length, 
					i -> {
						action_set.set(pl_, current_player_options[i]);
						return Q.get(action_set, pl_);
					}
				);

				ActionDistribution build = new ActionDistribution();
				for(int sel : select) {
					build.add(sel);
				}

				action_set.set(pl_, -1);
				out[pl_].put(action_set, build);
			});

			action_options[pl] = current_player_options;
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

	/**
	 * Runs the fictitious play algorithm for the current state. 
	 *
	 * @param Q The Q function for the current state.
	 * @param action_count The number of possible actions for each player. 
	 */
	public static ActionDistribution[] fictitious_play(StateQ Q, int iterations) {

		Map<ActionSet, ActionDistribution>[] response = best_responses(Q);

		ActionDistribution[] action_counts = new ActionDistribution[Q.player_count];
		
		for(int player = 0; player < Q.player_count; player++) {
			action_counts[player] = new ActionDistribution();
			for(int action : Q.action_choices[player]) {
				action_counts[player].add(action, 1.0 / Q.action_choices.length);
			}
		}

		ActionDistribution[] reaction = new ActionDistribution[Q.player_count];

		int[][] action_options = Utility.copy(Q.action_choices);
		int[] temp_action_set = new int[] {-1}, current_player_choices;

		for(int moves = 1; moves < iterations; moves++) {

			for(int player = 0; player < Q.player_count; player++) {
				reaction[player] = new ActionDistribution();

				current_player_choices = action_options[player];
				action_options[player] = temp_action_set;

				final int player_ = player;
				final int[] current_player_choices_ = current_player_choices;
				Utility.forEachChoice(action_options, action -> {
					ActionSet as = new ActionSet(action, Q.action_choices);
					double probability = 1;

					for(int opponent = 0; opponent < Q.player_count; opponent++) {
						if(player_ == opponent) continue;

						probability *= action_counts[opponent].get(as.get(opponent));
					}

					for(int curr : current_player_choices_) {
						reaction[player_].add(curr, 
							response[player_].get(as).get(curr) * probability
						);
					}
				});

				action_options[player] = current_player_choices;
			}

			Utility.forEachChoice(Q.action_choices, action -> {
				for(int player = 0; player < Q.player_count; player++) {
					action_counts[player].add(action[player], reaction[player].get(action[player]));
				}
			});

		}

		return action_counts;

	}

}

