
#ifndef _POLICY_H_
#define _POLICY_H_

#include <cstdint>
#include <ostream>
#include <vector>

class Policy {

private:
	std::vector<double> distribution;

public:
	const uint32_t action_count;
	Policy(uint32_t actions);
	std::vector<double> getDistribution() const;

};

std::ostream& operator<< (std::ostream& os, const Policy& str);

#endif

