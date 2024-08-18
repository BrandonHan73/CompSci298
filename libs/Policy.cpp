
#include <chrono>
#include <cstdint>
#include <ctime>
#include <iostream>
#include <random>
#include <vector>

#include "Policy.h"

Policy::Policy(uint32_t actions) : action_count(actions), distribution(actions) {

	std::uniform_real_distribution<double> unif(0, 1);
	std::mt19937_64 engine;
	engine.seed(std::chrono::high_resolution_clock::now().time_since_epoch().count());

	double sum = 0;
	for(double &val : distribution) {
		val = unif(engine);
		sum += val;
	}
	for(double &val : distribution) {
		val /= sum;
	}

}

std::vector<double> Policy::getDistribution() const {
	return distribution;
}

std::ostream& operator<< (std::ostream& os, const Policy& pol) {
	std::vector<double> dist = pol.getDistribution();
	for(const double val : dist) {
		os << val << ", ";
	}

	return os;
}

