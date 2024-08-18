
#include "Regression.h"

Regression::Regression(uint32_t inputs, uint32_t hiddens, uint32_t outputs) 
	: input_count(inputs), hidden_count(hiddens), output_count(outputs), hidden_weights(inputs, hiddens), 
	output_weights(hiddens, outputs), hidden_biases(hiddens), output_biases(outputs) {

	// TODO: Find a way to change the seed
	hidden_weights.setRandom();
	output_weights.setRandom();
	hidden_biases.setRandom();
	output_biases.setRandom();
}

Eigen::VectorXd Regression::feed(Eigen::VectorXd input) {
	Eigen::VectorXd hidden = (input.transpose() * hidden_weights).transpose() + hidden_biases;
	Eigen::VectorXd output = (hidden.transpose() * output_weights).transpose() + output_biases;

	return output;
}

