
#ifndef _CRASH_GAME_H_
#define _CRASH_GAME_H_

#include <ostream>
#include <utility>
#include <vector>

class CrashGame {

private:
	int N;
	std::vector<std::vector<double>> rewards;
	std::pair<int, int> truck;
	std::pair<int, int> car;

	double truck_crash_reward = 1;
	double car_crash_cost = 1;

public:
	CrashGame();
	CrashGame(int N_);

	std::pair<double, double> update(int truck_action, int car_action);

	std::vector<std::vector<double>> get_rewards() const;
	double get_rewards(int row, int col) const;
	double get_rewards(std::pair<int, int> loc) const;

	std::pair<int, int> get_truck_loc() const;
	std::pair<int, int> get_car_loc() const;

	void print_details(std::ostream &os) const;
};

std::ostream &operator<<(std::ostream &os, const CrashGame &game);

#endif

