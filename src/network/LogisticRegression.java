package network;

public class LogisticRegression extends NeuralNetwork {

	public LogisticRegression(int... layer_sizes) {
		super(layer_sizes);
	}

	public Class[] get_layer_types() {
		Class[] out = new Class[layer_count];

		for(int i = 0; i < layer_count; i++) {
			out[i] = LogisticLayer.class;
		}

		return out;
	}

}

