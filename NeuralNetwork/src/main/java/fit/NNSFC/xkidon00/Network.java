package fit.NNSFC.xkidon00;
import java.util.*;
import java.lang.*;
import java.io.*;
import fit.NNSFC.xkidon00.MnistWrapper;
import fit.NNSFC.xkidon00.InputData;
import org.apache.commons.math3.linear.*;

class NeuralNetwork {
    public static void main(String... args) {
        
        System.out.println("initializing network");
        int[] nnShape = {784, 15, 10};

        NeuralNetwork net = new NeuralNetwork(nnShape);
        try {
          System.out.println("reading MNIST training data");
          trainingData = new MnistWrapper(args[0], args[1]).loadData();
          System.out.println("reading MNIST testing data");
          testingData = new MnistWrapper(args[2], args[3]).loadData();
        } catch ( IOException e ) {
          System.out.println("cannot read input data");
        }
        System.out.println("done reading MNIST data");

      net.StochasticGD(3.0, 30);        
    }

    private void initBiases () {
      Random rand = new Random();

      biases = new double[nnShape.length -1][];

      for ( int layer = 0 ; layer < nnShape.length -1 ; layer++ ) {
 
        biases[layer] = new double[nnShape[layer + 1]];
        for ( int neuron = 0; neuron < nnShape[layer + 1]; neuron++ ) {

          biases[layer][neuron] = rand.nextDouble();
           
        } // for each neuron     
      } // first layer is an input vector 
    }


    private void initWeights () {
      Random rand = new Random();

      weights = new double[nnShape.length -1][][];

      for ( int layer = 0 ; layer < nnShape.length -1 ; layer++ ) {
        weights[layer] = new double[nnShape[layer + 1]][];

        for ( int neuron = 0; neuron < nnShape[layer + 1]; neuron++ ) {
          weights[layer][neuron] = new double[nnShape[layer]];

          for ( int weight = 0; weight < nnShape[layer]; weight++) {
            weights[layer][neuron][weight] = rand.nextDouble();
          }
        } // for each neuron     
      } // first layer is an input vector 
    }

    public static void StochasticGD(double eta, int batchSize) {

      if ( InputData.sampleSize() % batchSize != 0 ) {
        System.out.println("batches do not align with input data : " + batchSize + 
          " vs " + InputData.sampleSize());
        System.exit(1);
      }

      for (int batch = 0; batch < InputData.sampleSize(); batch += batchSize) {
        System.out.println("error : " + currentError);
        GD(Arrays.copyOfRange(InputData.getSamples(), batch, batch + batchSize),
           Arrays.copyOfRange(InputData.getLabels(), batch, batch + batchSize), eta);
      }
      
    }

    public static void GD(int[][] samples, int[] labels , double eta) {
      double[][] potentials = Arrays.copyOf(biases, biases.length);
      double[][] activations = Arrays.copyOf(biases, biases.length);

      double[][][] deltaw = Arrays.copyOf(weights, weights.length);
      double[][] deltab = Arrays.copyOf(biases, biases.length);

      for ( int sampleNum = 0; sampleNum < samples.length; sampleNum++ ) {
        for ( int layer = 0; layer < biases.length; layer++ ) {
          for ( int neuron = 0; neuron < biases[layer].length; neuron++ ) {

            activations[layer][neuron] = 0;
            potentials[layer][neuron] = 0;
            for ( int weight = 0; weight < weights[layer][neuron].length; weight ++ ) {
              potentials[layer][neuron] += potentials[layer][neuron] + 
                weights[layer][neuron][weight] * samples[sampleNum][weight];
            } // for each weight
            potentials[layer][neuron] += biases[layer][neuron];
            activations[layer][neuron] = sigmoid(potentials[layer][neuron]);
            //System.out.println(Arrays.toString(activations[layer]));// = sigmoid(potentials[layer][neuron]);
            //System.exit(1);
          } // for each neuron
        } // for each layer 
     
        /* BACKPROPAGATION */
       
        // last layer
        deltab[biases.length -1 ] = hadamardProduct(costPrimeVec(activations[biases.length-1], labels[sampleNum])
          , sigmoidPrimeVec(potentials[biases.length - 1]));

        //System.out.println(Arrays.toString(deltab[biases.length -1]));
        //System.exit(0);

        deltaw[weights.length -1] = hadamardProductNested(deltab[weights.length-1]
          , activations[activations.length-2]);


      // hidden layers

         for ( int layer = 2; layer < biases.length; layer++ ) {
           for ( int neuron = 0; neuron < biases[biases.length-layer].length; neuron++ ) {

             double sp = sigmoidPrime(potentials[biases.length-layer][neuron]);
             double deltaWeightSum = 0.0;

             for ( int delta = 0; delta < deltab[biases.length-layer+1][neuron]; delta++ ) {
                deltaWeightSum += deltaWeightSum + (deltab[biases.length-layer+1][delta] * weights[biases.length-layer+1][delta][neuron]);
             }  

             deltab[biases.length-2][neuron] = sp * deltaWeightSum;
             deltaw[weights.length -1] = hadamardProductNested(deltab[weights.length-1]
             , activations[activations.length-2]);
           } // for each neuron
         } // for each layer except the last

      // update the network
        for ( int layer = 0; layer < biases.length; layer++ ) {
          for ( int neuron = 0; neuron < biases[layer].length; neuron++ ) {
            biases[layer][neuron] = (-eta) * deltab[layer][neuron];
            for ( int weight = 0; weight < weights[layer][neuron].length; weight ++ ) {
              weights[layer][neuron][weight] = (-eta) * deltaw[layer][neuron][weight]; 
            }
          } 
        }
        
      } // for each sample in batch
    }

    NeuralNetwork(int[] networkShape) {
        nnShape = networkShape;
        initBiases(); 
        initWeights();

        /*
        for ( int i = 0; i < weights.length; i++) {
          System.out.println(weights[i].length);
          for ( int j = 0; j < weights[i].length; j++) {
          System.out.println(weights[i][j].length);
          }
        }
        System.exit(1);
        */
    }    

    private static double[] costPrimeVec(double[] activation, int label) {
         double[] ret = new double[activation.length];
         double[] vecLabel = new double[10];
         Arrays.fill(vecLabel, 0.0);
         vecLabel[label] = 1.0;

         for (int dim = 0; dim < activation.length; dim++) {
           ret[dim] = (activation[dim] - vecLabel[dim]);
         }
         return ret; 
    }

    private static double sigmoidPrime(double potential) {
         return sigmoid(potential)*(1.0-sigmoid(potential));
    }

    private static double[] sigmoidPrimeVec(double[] potentials) {
      double[] ret = new double[potentials.length];
         for (int dim = 0; dim < potentials.length; dim++) {
           ret[dim] = sigmoidPrime(potentials[dim]);
         }
         return ret; 
    }

    private static double[][] hadamardProductNested(double[] f1, double[] f2) {
      double[][] ret = new double[f1.length][f2.length];
      
      for (int i =0; i < f1.length; i++) {
         for (int j =0; j < f2.length; j++) {
           ret[i][j] = f1[i] * f2[j];
         } 
      }

      return ret;
    }

    private static double[] hadamardProduct(double[] f1, double[] f2) {
      if(f1.length != f2.length) {
        System.out.println("hadamardProduct argument lengths do not match");
      } 
      double[] ret = new double[f1.length];
      
      for (int i =0; i < f1.length; i++) {
        ret[i] = f1[i] * f2[i];
      }

      return ret;
    }

    private static double sigmoid(double value) {
      return 1.0/(1.0 + Math.exp(-value));
    }

    private static double currentError;
    private static double[][] biases;
    private static double[][][] weights;
    private static InputData trainingData;
    private static InputData testingData;
    private static List<List<Double>> validationData;
    private static List<Double> validationLabels;
    private static long seed;
    private static int numEpochs;
    private static int batchSize;
    private static int[] nnShape;
}
