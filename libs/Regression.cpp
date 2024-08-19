
#include <cmath>

#include "Regression.h"

Regression::Regression(uint32_t inputs, uint32_t outputs, uint32_t hiddens, double alpha) 
	: input_count(inputs), hidden_count(hiddens), output_count(outputs), hidden_weights(inputs, hiddens), 
	output_weights(hiddens, outputs), hidden_biases(hiddens), output_biases(outputs), 
	learning_rate(alpha), generation(0) {

	// TODO: Find a way to change the seed
	hidden_weights.setRandom();
	output_weights.setRandom();
	hidden_biases.setRandom();
	output_biases.setRandom();
}

Eigen::VectorXd Regression::feed(const Eigen::VectorXd &input) const {
	Eigen::VectorXd hidden = logistic(hidden_weights.transpose() * input + hidden_biases);
	Eigen::VectorXd output = logistic(output_weights.transpose() * hidden + output_biases);

	return output;
}

Eigen::VectorXd Regression::logistic(Eigen::VectorXd vec) const {
	for(double &val : vec) {
		val = 1 / (1 + exp(-val));
	}
	return vec;
}

void Regression::backpropogate(const Eigen::VectorXd &input, const Eigen::VectorXd &output, double alpha) {
	if(alpha > 0) {
		learning_rate = alpha;
		generation = 0;
	}
	alpha = learning_rate / (log(generation + 1) + 1);

	Eigen::VectorXd hid = logistic(hidden_weights.transpose() * input + hidden_biases);
	Eigen::VectorXd out = logistic(output_weights.transpose() * hid + output_biases);

	Eigen::ArrayXd dC_dz_output = (out - output).array() * out.array() * (Eigen::VectorXd::Ones(out.size()) - out).array();
	Eigen::ArrayXd dC_dz_hidden = (output_weights * dC_dz_output.matrix()).array() * hid.array() * (Eigen::VectorXd::Ones(hid.size()) - hid).array();

	output_weights -= alpha * hid * dC_dz_output.transpose().matrix();
	output_biases -= alpha * dC_dz_output.matrix();

	hidden_weights -= alpha * input * dC_dz_hidden.transpose().matrix();
	hidden_biases -= alpha * dC_dz_hidden.matrix();

	++generation;
}

