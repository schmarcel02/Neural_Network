package ch.schmarcel.NeuralNet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.Function;

public class Matrix {
    public int height, width;
    public double[][] data;

    public Matrix(final int width, final int height) {
        this.height = height;
        this.width = width;
        data = new double[height][width];
    }

    public Matrix(double[] data) {
        this(1, data.length);
        for (int y = 0; y < height; y++) {
            this.data[y][0] = data[y];
        }
    }

    public Matrix(Matrix matrix) {
        this(matrix.width, matrix.height);
        for (int y = 0; y < height; y++) {
            System.arraycopy(matrix.data[y], 0, this.data[y], 0, width);
        }
    }

    public static Matrix add(Matrix a, Matrix b) {
        if (a.height != b.height || a.width != b.width)
            throw new IllegalArgumentException("matrizes must have same dimensions ({" + a.width + "," + a.height + "}, {" + b.width + "," + b.height + "})");
        Matrix c = new Matrix(a.width, a.height);
        for (int y = 0; y < a.height; y++) {
            for (int x = 0; x < a.width; x++) {
                c.data[y][x] = a.data[y][x] + b.data[y][x];
            }
        }
        return c;
    }

    public void add(Matrix b) {
        if (height != b.height || width != b.width)
            throw new IllegalArgumentException("matrizes must have same dimensions ({" + width + "," + height + "}, {" + b.width + "," + b.height + "})");
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                data[y][x] += b.data[y][x];
            }
        }
    }

    public void add(double b) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                data[y][x] += b;
            }
        }
    }

    public static Matrix subtract(Matrix a, Matrix b) {
        if (a.height != b.height || a.width != b.width)
            throw new IllegalArgumentException("matrizes must have same dimensions ({" + a.width + "," + a.height + "}, {" + b.width + "," + b.height + "})");
        Matrix c = new Matrix(a.width, a.height);
        for (int y = 0; y < a.height; y++) {
            for (int x = 0; x < a.width; x++) {
                c.data[y][x] = a.data[y][x] - b.data[y][x];
            }
        }
        return c;
    }

    public void subtract(Matrix b) {
        if (height != b.height || width != b.width)
            throw new IllegalArgumentException("matrizes must have same dimensions ({" + width + "," + height + "}, {" + b.width + "," + b.height + "})");
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                data[y][x] -= b.data[y][x];
            }
        }
    }

    public void subtract(double b) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                data[y][x] -= b;
            }
        }
    }

    public static Matrix multiply(Matrix a, Matrix b) {
        if (a.width != b.height)
            throw new IllegalArgumentException("width of a must match height of b");
        Matrix c = new Matrix(b.width, a.height);
        for (int y = 0; y < c.height; y++) {
            for (int x = 0; x < c.width; x++) {
                double sum = 0;
                for (int z = 0; z < a.width; z++) {
                    sum += a.data[y][z] * b.data[z][x];
                }
                c.data[y][x] = sum;
            }
        }
        return c;
    }

    public void multiply(Matrix b) {
        if (height != b.height || width != b.width)
            throw new IllegalArgumentException("matrizes must have same dimensions ({" + width + "," + height + "}, {" + b.width + "," + b.height + "})");
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < height; x++) {
                data[x][y] *= b.data[x][y];
            }
        }
    }

    public void multiply(double b) {
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < height; x++) {
                data[x][y] *= b;
            }
        }
    }

    public static Matrix transpose(Matrix a) {
        Matrix b = new Matrix(a.height, a.width);
        for (int y = 0; y < b.width; y++) {
            for (int x = 0; x < b.height; x++) {
                b.data[x][y] = a.data[y][x];
            }
        }
        return b;
    }

    public void randomize(double min, double max) {
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < height; x++) {
                data[x][y] = Math.random() * (max - min) + min;
            }
        }
    }

    public double[] toArray() {
        double[] arr = new double[height * width];
        int count = 0;
        for (int x = 0; x < this.height; x++) {
            for (int y = 0; y < this.width; y++) {
                arr[count] = this.data[x][y];
                count++;
            }
        }
        return arr;
    }

    public void doFunction(Function<Double, Double> function) {
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < height; x++) {
                data[x][y] = function.apply(data[x][y]);
            }
        }
    }

    public JSONObject matrixToJSON() throws JSONException {
        JSONObject JSON_matrix = new JSONObject();

        JSON_matrix.put("width", width);
        JSON_matrix.put("height", height);

        StringBuilder b = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                b.append(",").append(data[y][x]);
            }
        }

        JSON_matrix.put("data", b.substring(1));

        return JSON_matrix;
    }

    public static Matrix matrixFromJSON(JSONObject JSON_matrix) throws JSONException {
        int width = JSON_matrix.getInt("width");
        int height = JSON_matrix.getInt("height");
        String[] data = JSON_matrix.getString("data").split(",");

        Matrix matrix = new Matrix(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                matrix.data[y][x] = Double.valueOf(data[y*width+x]);
            }
        }

        return matrix;
    }
}