package fit.NNSFC.xkidon00;
import java.util.*;
import java.lang.*;
import java.io.*;
import fit.NNSFC.xkidon00.MnistWrapper;
import fit.NNSFC.xkidon00.InputData;
import org.apache.commons.math3.linear.*;

class NeuralNetwork {
    public static void main(String... args) {
        NeuralNetwork net = new NeuralNetwork(Arrays.asList(784,15,10));
        try {
          System.out.println("reading training data");
          trainingData = new MnistWrapper(args[0], args[1]).loadData();
          System.out.println("reading testing data");
          testingData = new MnistWrapper(args[2], args[3]).loadData();
        } catch(IOException e) {
          System.out.println("cannot read input data");
        }
        System.out.println("done reading data");
    }

    private List<List<Double>> initBiases (List<Integer> networkShape) {
        List<List<Double>> retB = new ArrayList<List<Double>>();
        List<Double> layer;
        Random rnd = new Random();

        for (int i = 1; i < networkShape.size(); i++) {
            layer = new ArrayList<Double>();
            for (int j =0; j < networkShape.get(i); j++) {
                layer.add(rnd.nextDouble());
            }
            retB.add(layer);
        } 
        return retB;
    }

    private List<List<List<Double>>> initWeights (List<Integer> networkShape) {
        List<List<List<Double>>> retW = new ArrayList<List<List<Double>>>();
        List<List<Double>> layer;
        List<Double> neuron;
        Random rnd = new Random();

        for (int i = 1; i < networkShape.size(); i++) {
            layer = new ArrayList<List<Double>>();
            for (int j =0; j < networkShape.get(i); j++) {
                neuron = new ArrayList<Double>();
		for (int k=0; k < networkShape.get(i-1); k++) {
                    neuron.add(rnd.nextDouble()); 
		}
                layer.add(neuron);
            }
            retW.add(layer);
        } 
        return retW; 
    }

    public static void StochasticGradientDescent() {
    }

    NeuralNetwork(List<Integer> networkShape) {
        biases = initBiases(networkShape); 
        weights = initWeights(networkShape);
    }    

    private static Double sigmoid(Double innerPotential) {
        return 1.0/1.0 + (Math.exp(- innerPotential));
    }

    private static Double costPrime(Double activation, Double label) {
         return (activation - label);
    }

    private static List<List<Double>> biases;
    private static List<List<List<Double>>> weights;
    private static InputData trainingData;
    private static InputData testingData;
    private static List<List<Double>> validationData;
    private static List<Double> validationLabels;
    private static long seed;
    private static int numEpochs;
    private static int batchSize;
}
