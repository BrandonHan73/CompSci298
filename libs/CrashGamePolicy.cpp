
#include <cmath>
#include <utility>
#include <vector>

#include "CrashGamePolicy.h"
#include "CrashGame.h"

CrashGamePolicy::CrashGamePolicy() : CrashGamePolicy(5) { }

CrashGamePolicy::CrashGamePolicy(int N_) : N(N_) {
	Q = new std::vector<std::vector<std::vector<double>>>(
		std::pow(N_, 4), 
		std::vector<std::vector<double>>(
			4, std::vector<double>(4, 1)
		)
	);
}

CrashGamePolicy::~CrashGamePolicy() {
	delete Q;
}

void CrashGamePolicy::train(const CrashGame &game) {
	for(int i = 0; i < Q_train_iterations; ++i) {

		std::vector<std::vector<std::vector<double>>> *Q_update;
		Q_update = new std::vector<std::vector<std::vector<double>>>(*Q);

		std::vector<int> truck(4, 0);
		truck[0] = 1;

		std::vector<int> car(4, 0);
		car[0] = 1;

		delete Q;
		Q = Q_update;
	}
}

