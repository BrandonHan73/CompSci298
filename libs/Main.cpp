
#include <iostream>
#include <ostream>

#include "Policy.h"
#include "Regression.h"

int main() {

	Policy pol(3);
	std::cout << pol << std::endl;

	Regression reg(3, 3, 2);
	Eigen::VectorXd input(3);
	input << 1, 1, 1;
	std::cout << reg.feed(input);

}

