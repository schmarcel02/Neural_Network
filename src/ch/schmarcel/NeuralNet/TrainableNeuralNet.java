package ch.schmarcel.NeuralNet;

public class TrainableNeuralNet extends NeuralNet {
    public double learningRate = 0.1;

    private Matrix[] errors;
    private Matrix[] gradients;
    private Matrix[] weight_deltas;
    private Matrix[] layers_transposed;
    private Matrix[] weights_transposed;

    public TrainableNeuralNet(int initialMin, int initialMax, ActivationFunction activationFunction, int... layer_sizes) {
        super(initialMin, initialMax, activationFunction, layer_sizes);
    }

    public TrainableNeuralNet(int... layer_sizes) {
        super(-1, 1, ActivationFunction.TANH, layer_sizes);
    }

    public TrainableNeuralNet(Matrix[] weights, Matrix[] biases, ActivationFunction activationFunction, int[] layer_sizes) {
        super(weights, biases, activationFunction, layer_sizes);
    }

    public TrainableNeuralNet(String string) throws JSONConversionError{
        super(string);
    }

    public synchronized void train(double[] input, double[] target) {
        layers = new Matrix[layer_sizes.length];
        errors = new Matrix[layers.length - 1];
        gradients = new Matrix[layers.length - 1];
        weight_deltas = new Matrix[layers.length - 1];
        layers_transposed = new Matrix[layers.length - 1];
        weights_transposed = new Matrix[layers.length - 2];

        feedForward(input);

        Matrix targets = new Matrix(target);
        errors[layers.length - 2] = Matrix.subtract(targets, layers[layers.length - 1]);

        calculate(layers.length - 2);

        for (int i = layers.length - 3; i >= 0; i--) {
            weights_transposed[i] = Matrix.transpose(this.weights[i + 1]);
            errors[i] = Matrix.multiply(weights_transposed[i], errors[i + 1]);

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
        gradients[index] = new Matrix(layers[index + 1]);
        gradients[index].doFunction(activationFunction.df);
        gradients[index].multiply(errors[index]);
        gradients[index].multiply(learningRate);
    }

    private void calculateWeightDeltas(int index) {
        layers_transposed[index] = Matrix.transpose(layers[index]);
        weight_deltas[index] = Matrix.multiply(gradients[index], layers_transposed[index]);
    }

    private void updateWeights(int index) {
        this.weights[index].add(weight_deltas[index]);
        this.biases[index].add(gradients[index]);
    }
}
