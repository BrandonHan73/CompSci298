
#ifndef _REGRESSION_H_
#define _REGRESSION_H_

#include <cstdint>

class Regression {

public:
	const uint32_t input_count, output_count;

	Regression(uint32_t inputs, uint32_t outputs);

};

#endif

