package network;

import apple_lib.network.*;

public class SoftMax extends NeuralNetwork {

	public SoftMax(int... layer_sizes) {
		super(layer_sizes);
	}

	public Class[] get_layer_types() {
		Class[] out = new Class[layer_count];

		for(int i = 0; i < layer_count - 1; i++) {
			out[i] = LogisticLayer.class;
		}
		out[layer_count - 1] = SoftmaxLayer.class;

		return out;
	}

}

