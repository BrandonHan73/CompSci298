
#ifndef _CRASH_GAME_POLICY_H_
#define _CRASH_GAME_POLICY_H_

#include <ostream>
#include <utility>
#include <vector>

#include "CrashGame.h"

class CrashGamePolicy {

private:
	std::vector<std::vector<std::vector<double>>> *Q;
	int N;
	int Q_train_iterations = 1024;
	int best_response_iterations = 1024;

	std::pair<int, int> best_response(CrashGame game, int truck, int car);

public:
	CrashGamePolicy();
	CrashGamePolicy(int N_);

	~CrashGamePolicy();

	void train(const CrashGame &game);

};

std::ostream operator<<(std::ostream &os, const CrashGamePolicy &policy);

#endif

