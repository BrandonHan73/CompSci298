package network;

public class FullRangeNetwork extends NeuralNetwork {

	public FullRangeNetwork(int... layer_sizes) {
		super(layer_sizes);
	}

	public Class[] get_layer_types() {
		Class[] out = new Class[layer_count];

		for(int i = 0; i < layer_count - 1; i++) {
			out[i] = TanhLayer.class;
		}
		out[layer_count - 1] = CleanLayer.class;

		return out;
	}

}

