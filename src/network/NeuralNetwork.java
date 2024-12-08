package network;

import util.Log;

import java.lang.reflect.InvocationTargetException;

public abstract class NeuralNetwork {

	private final int[] sizes;
	protected final int layer_count, input_count, output_count;

	private final NetworkLayer[] layers;

	private static final String log_name = "neural_network";

	public NeuralNetwork(int... layer_sizes) {
		layer_count = layer_sizes.length - 1;
		sizes = layer_sizes;
		input_count = sizes[0];
		output_count = sizes[layer_count];

		layers = new NetworkLayer[layer_count];
		Class[] layer_types = get_layer_types();
		for(int i = 0; i < layer_count; i++) {
			try {
				layers[i] = (NetworkLayer) layer_types[i].getDeclaredConstructors()[0].newInstance(sizes[i], sizes[i + 1]);
			} catch(InvocationTargetException ite) {
				throw new RuntimeException(ite.getTargetException().getMessage());
			} catch(IllegalAccessException iae) {
				throw new RuntimeException("IllegalAccessException generated");
			} catch(InstantiationException ie) {
				throw new RuntimeException("InstantiationException generated");
			}
		}
	}

	public double[] pass(double[] input) {
		double[] out = input;

		StringBuilder log = new StringBuilder("Activation log: ");
		log.append(this.getClass().getName()).append("\n");
		for(double d : out) log.append(d).append(" ");
		log.append("Input end\n");

		for(NetworkLayer layer : layers) {
			out = layer.pass(out);

			for(double d : out) log.append(d).append(" ");
			log.append("Layer end\n");
		}

		Log.log(log_name, log.toString());

		return out;
	}

	public void backpropogate(double[] dCdy) {
		if(dCdy.length != output_count) {
			throw new RuntimeException("Parameter length invalid");
		}

		StringBuilder log = new StringBuilder("Backpropogation log: ");
		log.append(this.getClass().getName()).append("\n");
		for(double d : dCdy) log.append(d).append(" ");
		log.append("Input end\n");

		for(int layer = layer_count - 1; layer >= 0; layer--) {
			dCdy = layers[layer].backpropogate(dCdy);

			for(double d : dCdy) log.append(d).append(" ");
			log.append("Layer end\n");
		}

		Log.log(log_name, log.toString());
	}

	public void backpropogate(double[] input, double[] dCdy) {
		if(input.length != input_count) {
			throw new RuntimeException("Parameter length invalid");
		}

		pass(input);
		backpropogate(dCdy);
	}

	public abstract Class[] get_layer_types();

}

