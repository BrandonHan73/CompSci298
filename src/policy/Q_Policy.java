package policy;

import environment.*;
import base.*;

public abstract class Q_Policy extends Policy {

	public Q_Policy(Game base) {
		super(base);
	}

	public abstract StateQ get_Q(State state);

	@Override
	public ActionDistribution[] evaluate(State state) {
		return NashSolver.evaluate_state(get_Q(state));
	}

}

