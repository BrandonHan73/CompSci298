
#include <chrono>
#include <ctime>
#include <iostream>
#include <ostream>
#include <random>
#include <utility>
#include <vector>

#include "CrashGame.h"

CrashGame::CrashGame() : CrashGame(5) { }

CrashGame::CrashGame(int N_) : N(N_), rewards(N_, std::vector<double>(N_)) {

	std::uniform_real_distribution<double> unif_real(-1, 1);
	std::uniform_int_distribution<> unif_int(0, N_ - 1);

	std::mt19937_64 engine;
	engine.seed(std::chrono::high_resolution_clock::now().time_since_epoch().count());

	for(int i = 0; i < N; ++i) {
		for(int j = 0; j < N; ++j) {
			rewards[i][j] = unif_real(engine);
		}
	}

	truck.first = unif_int(engine);
	truck.second = unif_int(engine);
	do {
		car.first = unif_int(engine);
		car.second = unif_int(engine);
	} while(truck == car);

}

std::pair<double, double> CrashGame::update(int truck_action, int car_action) {
	std::pair<int, int> truck_old(truck);
	std::pair<int, int> car_old(car);

	// Action interpretation
	// 0: Up
	// 1: Right
	// 2: Down
	// 3: Left
	truck_action %= 4;
	car_action %= 4;

	if(truck_action % 2 == 0) truck.first = (truck.first + truck_action - 1 + N) % N;
	else truck.second = (truck.second - truck_action + 2 + N) % N;

	if(car_action % 2 == 0) car.first = (car.first + car_action - 1 + N) % N;
	else car.second = (car.second - car_action + 2 + N) % N;

	std::pair<double, double> cycle_rewards(get_rewards(truck), get_rewards(car));

	bool crash = truck == car;
	if(truck == car_old && car == truck_old) {
		crash = true;
		truck = truck_old;
	}
	if(crash) {
		cycle_rewards.first += truck_crash_reward;
		cycle_rewards.second -= car_crash_cost;
		car = car_old;
	}

	return cycle_rewards;
}

std::vector<std::vector<double>> CrashGame::get_rewards() const { return rewards; }

double CrashGame::get_rewards(int row, int col) const {
	return rewards[row][col];
}

double CrashGame::get_rewards(std::pair<int, int> loc) const {
	return get_rewards(loc.first, loc.second);
}

std::pair<int, int> CrashGame::get_truck_loc() const { return truck; }

std::pair<int, int> CrashGame::get_car_loc() const { return car; }

void CrashGame::print_details(std::ostream &os) const {
	os << "Reward matrix: \n";
	for(const std::vector<double> &row : rewards) {
		for(double val : row) {
			os << val << ", ";
		}
		os << "\n";
	}

	os << "Truck location: " << truck.first << ", " << truck.second << "\n";
	os << "Car location: " << car.first << ", " << car.second;
}

