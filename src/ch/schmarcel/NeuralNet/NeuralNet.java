package ch.schmarcel.NeuralNet;

import ch.schmarcel.matrix.DoubleMatrix;
public class NeuralNet {
    protected int[] layer_sizes;
    protected ActivationFunction activationFunction;

    protected DoubleMatrix[] layers;
    protected DoubleMatrix[] weights;
    protected DoubleMatrix[] biases;

    public NeuralNet(int initialMin, int initialMax, ActivationFunction activationFunction, int... layer_sizes) {
        this.layer_sizes = layer_sizes;
        this.activationFunction = activationFunction;

        weights = new DoubleMatrix[layer_sizes.length - 1];
        biases = new DoubleMatrix[layer_sizes.length - 1];

        randomize(initialMin, initialMax);
    }

    public NeuralNet(int... layer_sizes) {
        this(-1, 1, ActivationFunction.TANH, layer_sizes);
    }

    public NeuralNet(DoubleMatrix[] weights, DoubleMatrix[] biases, ActivationFunction activationFunction, int[] layer_sizes) {
        this.layer_sizes = layer_sizes;
        this.activationFunction = activationFunction;

        this.weights = weights;
        this.biases = biases;
    }

    public void randomize(int min, int max) {
        for (int i = 0; i < layer_sizes.length - 1; i++) {
            weights[i] = new DoubleMatrix(layer_sizes[i], layer_sizes[i + 1]);
            biases[i] = new DoubleMatrix(1, layer_sizes[i + 1]);
            weights[i].randomize(min, max);
            biases[i].randomize(min, max);
        }
    }

    public synchronized double[] guess(double[] input) {
        feedForward(input);
        return layers[layers.length - 1].toArray();
    }

    void feedForward(double[] input) {
        layers = new DoubleMatrix[this.layer_sizes.length];

        layers[0] = new DoubleMatrix(input);

        for (int i = 1; i < layers.length; i++) {
            layers[i] = DoubleMatrix.multiply(weights[i - 1], layers[i - 1]);
            layers[i].add(biases[i - 1]);
            layers[i].applyFunction(activationFunction.f);
        }
    }
}