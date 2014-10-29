package fit.NNSFC.xkidon00;
import java.util.*;
import java.lang.Math;
import fit.NNSFC.xkidon00.MnistWrapper;
import org.apache.commons.math3.linear.*;

class NeuralNetwork {
    public static void main(String[] args) {
        NeuralNetwork net = new NeuralNetwork(Arrays.asList(784,15,10));
//        for ( List<List<Double>> weight : weights ) {
  ///          System.out.println(weight.get(0).size());
     //   }
       // System.exit(1);
        
//        System.out.println(weights);
        trainingData = new MnistWrapper("data/mnist_tr.dat").loadData();
        trainingLabels = new MnistWrapper("data/mnist_tr.lbl").loadLabels();
        validationData = new MnistWrapper("data/mnist_val.dat").loadData();
        validationLabels = new MnistWrapper("data/mnist_val.lbl").loadLabels();
        batchSize = 100;
        //System.out.println(biases);
	
        if (trainingData.size() != trainingLabels.size()) {
            System.out.println("#data is not equal to #labels");
            System.exit(1);
        }
        StochasticGradientDescent();
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
        // create batches
        List<List<List<Double>>> dataBatches = new ArrayList<List<List<Double>>>();
        List<List<Double>> labelBatches = new ArrayList<List<Double>>();

        if(trainingData.size() % batchSize != 0) { 
            System.out.println("batch size does not align to trainingData");
            System.exit(1);
        }
        if (weights.size() != biases.size()) {
            System.out.println("weights dim != biases dim");
            System.exit(1);
        }

        for(int i = 0; i < trainingData.size() ; i+= batchSize) {
            dataBatches.add(trainingData.subList(i, i+batchSize));
            labelBatches.add(trainingLabels.subList(i, i+batchSize));
        } 
        for ( int i = 0; i < dataBatches.size(); i++) {
            for ( int j = 0; j < dataBatches.get(i).size(); j++) {
                for ( int k = 0; k < biases.size(); k++) {
                        List<List<Double>> nnPotential = new ArrayList<List<Double>>(biases);
                        List<List<Double>> nnActivation = new ArrayList<List<Double>>(biases);
                    for ( int l = 0; l < biases.get(k).size(); l++) {
                        Double potential = new Double(0.0);

                        if ( dataBatches.get(i).get(j).size() != weights.get(k).get(l).size() ) {
                           System.out.println("neuron weights dim != input dim");
                           System.out.println(k);
                           System.out.println(l);
                           System.out.println(dataBatches.get(i).get(j).size());
                           System.out.println(weights.get(k).get(l).size());
                           System.exit(1);
                        } 

                        for ( int m = 0; m < dataBatches.get(i).get(j).size(); m++ ) {
                            // evaluate neuron potential
                            potential += weights.get(k).get(l).get(m) * dataBatches.get(i).get(j).get(m);
                        } // for each weight
                        nnPotential.get(k).set(l,potential += biases.get(k).get(l)); 
                        nnActivation.get(k).set(l, sigmoid(nnPotential.get(k).get(l)));

                        // backpropagation
                        // last layer
                        //for (int m = 0; m <   )  
                        
                        
                    } // for each neuron 
                } // for each layer               
            } // for each sample         
        }  // for each batch
        System.out.println(dataBatches.size());
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
    private static List<List<Double>> trainingData;
    private static List<Double> trainingLabels;
    private static List<List<Double>> validationData;
    private static List<Double> validationLabels;
    private static long seed;
    private static int numEpochs;
    private static int batchSize;
}
