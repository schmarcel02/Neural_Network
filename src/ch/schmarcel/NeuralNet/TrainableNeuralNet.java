package ch.schmarcel.NeuralNet;

import ch.schmarcel.matrix.DoubleMatrix;

public class TrainableNeuralNet extends NeuralNet {
    public double learningRate = 0.1;

    private DoubleMatrix[] errors;
    private DoubleMatrix[] gradients;
    private DoubleMatrix[] weight_deltas;
    private DoubleMatrix[] layers_transposed;
    private DoubleMatrix[] weights_transposed;

    public TrainableNeuralNet(int initialMin, int initialMax, ActivationFunction activationFunction, int... layer_sizes) {
        super(initialMin, initialMax, activationFunction, layer_sizes);
    }

    public TrainableNeuralNet(int... layer_sizes) {
        super(-1, 1, ActivationFunction.TANH, layer_sizes);
    }

    public TrainableNeuralNet(DoubleMatrix[] weights, DoubleMatrix[] biases, ActivationFunction activationFunction, int[] layer_sizes) {
        super(weights, biases, activationFunction, layer_sizes);
    }

    public TrainableNeuralNet(NeuralNet neuralNet) {
        super(neuralNet.weights, neuralNet.biases, neuralNet.activationFunction, neuralNet.layer_sizes);
    }

    public synchronized void train(double[] input, double[] target) {
        layers = new DoubleMatrix[layer_sizes.length];
        errors = new DoubleMatrix[layers.length - 1];
        gradients = new DoubleMatrix[layers.length - 1];
        weight_deltas = new DoubleMatrix[layers.length - 1];
        layers_transposed = new DoubleMatrix[layers.length - 1];
        weights_transposed = new DoubleMatrix[layers.length - 2];

        feedForward(input);

        DoubleMatrix targets = new DoubleMatrix(target);
        errors[layers.length - 2] = DoubleMatrix.subtract(targets, layers[layers.length - 1]);

        calculate(layers.length - 2);

        for (int i = layers.length - 3; i >= 0; i--) {
            weights_transposed[i] = DoubleMatrix.transpose(this.weights[i + 1]);
            errors[i] = DoubleMatrix.multiply(weights_transposed[i], errors[i + 1]);

            calculate(i);
        }

        layers = null;
        errors = null;
        gradients = null;
        weight_deltas = null;
        layers_transposed = null;
        weights_transposed = null;
    }

    private void calculate(int index) {
        calculateGradient(index);
        calculateWeightDeltas(index);
        updateWeights(index);
    }

    private void calculateGradient(int index) {
        gradients[index] = new DoubleMatrix(layers[index + 1]);
        gradients[index].applyFunction(activationFunction.df);
        gradients[index].multiply(errors[index]);
        gradients[index].multiply(learningRate);
    }

    private void calculateWeightDeltas(int index) {
        layers_transposed[index] = DoubleMatrix.transpose(layers[index]);
        weight_deltas[index] = DoubleMatrix.multiply(gradients[index], layers_transposed[index]);
    }

    private void updateWeights(int index) {
        this.weights[index].add(weight_deltas[index]);
        this.biases[index].add(gradients[index]);
    }
}
