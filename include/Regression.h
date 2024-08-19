
#ifndef _REGRESSION_H_
#define _REGRESSION_H_

#include <cstdint>

#include <Eigen/Dense>

class Regression {

private:
	Eigen::MatrixXd hidden_weights, output_weights;
	Eigen::VectorXd hidden_biases, output_biases;
	double learning_rate;
	uint32_t generation;

public:
	const uint32_t input_count, output_count;
	const uint32_t hidden_count;

	Regression(uint32_t inputs, uint32_t outputs, uint32_t hiddens = 10, double alpha = 0.6);

	Eigen::VectorXd feed(const Eigen::VectorXd &input) const;
	Eigen::VectorXd logistic(Eigen::VectorXd vec) const;

	void backpropogate(const Eigen::VectorXd &input, const Eigen::VectorXd &output, double alpha = -1);

};

#endif

