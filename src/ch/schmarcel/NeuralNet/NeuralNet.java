package ch.schmarcel.NeuralNet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NeuralNet {
    protected int[] layer_sizes;
    protected ActivationFunction activationFunction;

    protected Matrix[] layers;
    protected Matrix[] weights;
    protected Matrix[] biases;

    public NeuralNet(int initialMin, int initialMax, ActivationFunction activationFunction, int... layer_sizes) {
        this.layer_sizes = layer_sizes;
        this.activationFunction = activationFunction;

        weights = new Matrix[layer_sizes.length - 1];
        biases = new Matrix[layer_sizes.length - 1];

        randomize(initialMin, initialMax);
    }

    public NeuralNet(int... layer_sizes) {
        this(-1, 1, ActivationFunction.TANH, layer_sizes);
    }

    public NeuralNet(Matrix[] weights, Matrix[] biases, ActivationFunction activationFunction, int[] layer_sizes) {
        this.layer_sizes = layer_sizes;
        this.activationFunction = activationFunction;

        this.weights = weights;
        this.biases = biases;
    }

    public NeuralNet(String string) throws JSONConversionError {
        try {
            JSONObject JSON_nn = new JSONObject(string);

            JSONArray JSON_layers = JSON_nn.getJSONArray("layers");
            JSONArray JSON_weights = JSON_nn.getJSONArray("weights");
            JSONArray JSON_biases = JSON_nn.getJSONArray("biases");
            String function = JSON_nn.getString("activationFunction");

            layer_sizes = new int[JSON_layers.length()];
            for (int i = 0; i < JSON_layers.length(); i++) {
                layer_sizes[i] = JSON_layers.getInt(i);
            }

            weights = new Matrix[JSON_weights.length()];
            for (int i = 0; i < JSON_weights.length(); i++) {
                weights[i] = Matrix.matrixFromJSON(JSON_weights.getJSONObject(i));
            }

            biases = new Matrix[JSON_biases.length()];
            for (int i = 0; i < JSON_biases.length(); i++) {
                biases[i] = Matrix.matrixFromJSON(JSON_biases.getJSONObject(i));
            }

            activationFunction = ActivationFunction.forString(function);
        } catch (JSONException | IllegalArgumentException e) {
            throw new JSONConversionError(e);
        }
    }

    public void randomize(int min, int max) {
        for (int i = 0; i < layer_sizes.length - 1; i++) {
            weights[i] = new Matrix(layer_sizes[i], layer_sizes[i + 1]);
            biases[i] = new Matrix(1, layer_sizes[i + 1]);
            weights[i].randomize(min, max);
            biases[i].randomize(min, max);
        }
    }

    public synchronized double[] guess(double[] input) {
        feedForward(input);
        return layers[layers.length - 1].toArray();
    }

    void feedForward(double[] input) {
        layers = new Matrix[this.layer_sizes.length];

        layers[0] = new Matrix(input);

        for (int i = 1; i < layers.length; i++) {
            layers[i] = Matrix.multiply(weights[i - 1], layers[i - 1]);
            layers[i].add(biases[i - 1]);
            layers[i].doFunction(activationFunction.f);
        }
    }

    public String toJSONString() throws JSONConversionError {
        try {
            JSONObject JSON_nn = new JSONObject();

            JSONArray JSON_layers = new JSONArray();
            for (int i = 0; i < layer_sizes.length; i++) {
                JSON_layers.put(layer_sizes[i]);
            }

            JSONArray JSON_weights = new JSONArray();
            for (int i = 0; i < weights.length; i++) {
                JSON_weights.put(weights[i].matrixToJSON());
            }

            JSONArray JSON_biases = new JSONArray();
            for (int i = 0; i < biases.length; i++) {
                JSON_biases.put(biases[i].matrixToJSON());
            }

            JSON_nn.put("layers", JSON_layers);
            JSON_nn.put("weights", JSON_weights);
            JSON_nn.put("biases", JSON_biases);
            JSON_nn.put("activationFunction", ActivationFunction.asString(activationFunction));

            return JSON_nn.toString(4);
        } catch (JSONException | IllegalArgumentException e) {
            throw new JSONConversionError(e);
        }
    }
}