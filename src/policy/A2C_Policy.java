package policy;

import environment.*;
import base.*;
import network.LogisticRegression;
import network.SoftMax;

public class A2C_Policy extends EpsilonGreedy {

	LogisticRegression value_network;
	SoftMax[] policy_network;

	public A2C_Policy(Game game) {
		super(game);
	}

	public void step(State curr, ActionSet action, State next, double[] reward, double[] prob) {
	}

	public ActionDistribution[] evaluate(State state) {
		return null;
	}

}

