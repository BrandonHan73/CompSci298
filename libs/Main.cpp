
#include <iostream>

#include "Policy.h"
#include "Regression.h"

int main() {

	Policy pol(3);

	Regression reg(2, 2, 64);

	Eigen::VectorXd input(2);
	Eigen::VectorXd output(2);

	for(int i = 0; i < 2048; i++) {
		input << 0, 0;
		output << 0, 0;
		reg.backpropogate(input, output);

		input << 0, 1;
		output << 1, 1;
		reg.backpropogate(input, output);

		input << 1, 0;
		output << 1, 1;
		reg.backpropogate(input, output);

		input << 1, 1;
		output << 1, 0;
		reg.backpropogate(input, output);
	}

	input << 0, 0;
	std::cout << reg.feed(input) << std::endl << std::endl;

	input << 0, 1;
	std::cout << reg.feed(input) << std::endl << std::endl;

	input << 1, 0;
	std::cout << reg.feed(input) << std::endl << std::endl;

	input << 1, 1;
	std::cout << reg.feed(input) << std::endl << std::endl;

}

