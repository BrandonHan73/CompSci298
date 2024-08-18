
#ifndef _REGRESSION_H_
#define _REGRESSION_H_

#include <cstdint>

#include <Eigen/Dense>

class Regression {

private:
	Eigen::MatrixXd hidden_weights, output_weights;
	Eigen::VectorXd hidden_biases, output_biases;

public:
	const uint32_t input_count, output_count;
	const uint32_t hidden_count;

	Regression(uint32_t inputs, uint32_t hiddens, uint32_t outputs);

	Eigen::VectorXd feed(Eigen::VectorXd input);

};

#endif

