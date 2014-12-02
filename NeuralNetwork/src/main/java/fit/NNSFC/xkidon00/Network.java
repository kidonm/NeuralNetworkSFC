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
        int[] nnShape = {784, 40, 10};

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

        //double[][] tr = {
        //{1.0, 1.0, 1.0, 1.0, 1.0}
        //};
 
        //int[] lb = {1};

        //trainingData = new InputData(tr, lb);

      net.StochasticGD(4.0, 30);        
    }

    private void initBiases () {
      Random rand = new Random();

      biases = new double[nnShape.length -1][];

      for ( int layer = 0 ; layer < nnShape.length -1 ; layer++ ) {
 
        biases[layer] = new double[nnShape[layer + 1]];
        for ( int neuron = 0; neuron < nnShape[layer + 1]; neuron++ ) {

          biases[layer][neuron] = rand.nextGaussian();
           
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
            weights[layer][neuron][weight] = rand.nextGaussian();
          }
        } // for each neuron     
      } // first layer is an input vector 
    }

    public static void StochasticGD(double eta, int batchSize) {

      if ( trainingData.getSamples().length % batchSize != 0 ) {
        System.out.println("batches do not align with input data : " + batchSize + 
          " vs " + trainingData.sampleSize());
        System.exit(1);
      }
      while(true) {
      for (int batch = 0; batch < trainingData.sampleSize(); batch += batchSize) {
        System.out.print("after batch : " + batch + " out of : " + trainingData.sampleSize() + " -> ");
        GD(Arrays.copyOfRange(trainingData.getSamples(), batch, batch + batchSize),
           Arrays.copyOfRange(trainingData.getLabels(), batch, batch + batchSize), eta);

        evaluate(testingData.getSamples(), testingData.getLabels());
      }
    }
      
    }

    public static void evaluate(double[][] samples, int[] labels) {
      double[][] potentials = new double[biases.length][];
      double[][] activations = new double[biases.length][];

      for ( int layer = 0; layer < biases.length; layer++ ) {
        potentials[layer] = new double[biases[layer].length];
        activations[layer] = new double[biases[layer].length];
      }

      int counter = 0;
      int total = samples.length;

      for ( int sampleNum = 0; sampleNum < samples.length; sampleNum++ ) {
        for ( int layer = 0; layer < biases.length; layer++ ) {
          for ( int neuron = 0; neuron < biases[layer].length; neuron++ ) {

            activations[layer][neuron] = 0;
            potentials[layer][neuron] = 0;
            for ( int weight = 0; weight < weights[layer][neuron].length; weight ++ ) {
              if ( layer == 0 ) {                                                      
                potentials[layer][neuron] += weights[layer][neuron][weight] * samples[sampleNum][weight];
              } else {
                potentials[layer][neuron] += weights[layer][neuron][weight] * activations[layer-1][weight];
              }
            } // for each weight
            potentials[layer][neuron] += biases[layer][neuron];
            activations[layer][neuron] = sigmoid(potentials[layer][neuron]);
          } // for each neuron
        } // for each layer 
        //System.out.println(labels[sampleNum]);
        double[] vecLabel = vectorizeLabel(labels[sampleNum]);
        //for (int i = 0 ; i < 10; i++ ) {
        //  System.out.println(vecLabel[i]);
       // }
       // for (int i = 0 ; i < 10; i++ ) {
        //  System.out.println(activations[biases.length-1][i]);
        //}
        //System.out.println(max(activations[biases.length-1]) + " " + max(vecLabel));
        if ( max(vecLabel) == max(activations[biases.length-1])) {
          counter++;
        }
        //System.exit(1);
      } // for each sample in batch
      System.out.println("correctly classified : " + counter + " out of : " + total);
    }

    public static void GD(double[][] samples, int[] labels , double eta) {
      double[][] potentials = new double[biases.length][];
      double[][] activations = new double[biases.length][];
      double[][] globalDeltaB = new double[biases.length][];
      double[][] deltab = new double[biases.length][];
      double[][][] deltaw = new double[nnShape.length -1][][];
      double[][][] globalDeltaW = new double[nnShape.length -1][][];


      for ( int layer = 0; layer < biases.length; layer++ ) {
        potentials[layer] = new double[biases[layer].length];
        activations[layer] = new double[biases[layer].length];
        deltab[layer] = new double[biases[layer].length];
        globalDeltaB[layer] = new double[biases[layer].length];
      }

      for ( int layer = 0 ; layer < nnShape.length -1 ; layer++ ) {
        deltaw[layer] = new double[nnShape[layer + 1]][];
        globalDeltaW[layer] = new double[nnShape[layer + 1]][];
        for ( int neuron = 0; neuron < nnShape[layer + 1]; neuron++ ) {
          deltaw[layer][neuron] = new double[nnShape[layer]];
          globalDeltaW[layer][neuron] = new double[nnShape[layer]];
        }
      }

        //for( int tmp = 0; tmp < deltab[0].length; tmp++ ) {
        //  System.out.print(globalDeltaB[0][tmp]);
        //}
        //System.exit(1);
        //System.out.println();

      for ( int sampleNum = 0; sampleNum < samples.length; sampleNum++ ) {
        for ( int layer = 0; layer < biases.length; layer++ ) {
//          System.out.println("layer : " + layer);
          for ( int neuron = 0; neuron < biases[layer].length; neuron++ ) {
//            System.out.println("neuron : " + neuron);
            activations[layer][neuron] = 0;
            potentials[layer][neuron] = 0;
            for ( int weight = 0; weight < weights[layer][neuron].length; weight ++ ) {
              if (layer == 1) {
              }
              if ( layer == 0 ) {                                                      

                if ( weights[layer][neuron].length != samples[sampleNum].length ) {
                  System.out.println("samplesize != input layer size"); 
                  System.exit(1);
                }

                potentials[layer][neuron] += weights[layer][neuron][weight] * samples[sampleNum][weight];
//                System.out.println("weight/sample : " + weights[layer][neuron][weight] + " " + samples[sampleNum][weight] + " " + potentials[layer][neuron]);
              } else {

                if ( weights[layer][neuron].length != activations[layer-1].length ) {
                  System.out.println("samplesize != input layer size"); 
                }

                potentials[layer][neuron] += weights[layer][neuron][weight] * activations[layer-1][weight];
//                System.out.println("weight/sample : " + weights[layer][neuron][weight] + " " + activations[sampleNum][weight]+ " " + potentials[layer][neuron]);
              }

            } // for each weight
            potentials[layer][neuron] += biases[layer][neuron];
//            System.out.println("potential " + potentials[layer][neuron] + " bias " + biases[layer][neuron]);
            activations[layer][neuron] = sigmoid(potentials[layer][neuron]);
//            System.out.println("activation " + activations[layer][neuron]);

          } // for each neuron
        } // for each layer 
        /* BACKPROPAGATION */


//        System.out.println("BACKPROPAGATION");
       
        // last layer
        deltab[biases.length -1 ] = hadamardProduct(costPrimeVec(activations[biases.length-1], labels[sampleNum])
          , sigmoidPrimeVec(potentials[biases.length - 1]));

//        for( int tmp = 0; tmp < deltab[biases.length-1].length; tmp++ ) {
// /         System.out.println("ll delta " + deltab[biases.length-1][tmp] + " ");
//        }

        deltaw[weights.length -1] = hadamardProductNested(deltab[weights.length-1]
          , activations[activations.length-2]);

//        for( int tmp = 0; tmp < deltab[biases.length-1].length; tmp++ ) {
//          System.out.println("lw delta " + deltab[biases.length-1][tmp] + " ");
// /         for (int j = 0; j < biases[biases.length-2].length; j++) {
//            System.out.println(activations[activations.length-2][j] + " vs " + deltaw[weights.length-1][tmp][j]);
//          }
//        }

      // hidden layers

         for ( int layer = 2; biases.length - layer >= 0; layer++ ) {
//           System.out.println("layer : " + (biases.length - layer));
           
           for ( int neuron = 0; neuron < biases[biases.length-layer].length; neuron++ ) {
  //         System.out.println("neuron : " + neuron);

             double sp = sigmoidPrime(potentials[biases.length-layer][neuron]);
             double deltaWeightSum = 0.0;
             
    //         System.out.println("dws : ");
             for ( int delta = 0; delta < deltab[biases.length-layer+1].length ; delta++ ) {
               deltaWeightSum +=
                 deltab[biases.length-layer+1][delta] * weights[biases.length-layer+1][delta][neuron];
      //           System.out.println(deltab[biases.length-layer+1][delta] + " " + weights[biases.length-layer+1][delta][neuron] + " " + deltaWeightSum);
             }  
          
             deltab[biases.length-layer][neuron] = sp * deltaWeightSum;
        //     System.out.println("deltab : " +deltab[biases.length-layer][neuron]);
             if ( biases.length-layer == 0 ) {
               deltaw[weights.length -layer] = hadamardProductNested(deltab[weights.length-layer]
               , samples[sampleNum]);
             } else {
               deltaw[weights.length -layer] = hadamardProductNested(deltab[weights.length-layer]
               , activations[activations.length-layer-1]);
             }
           } // for each neuron
         } // for each layer except the last

        // alter gradient
        for ( int layer = 0; layer < biases.length; layer++ ) {
          for ( int neuron = 0; neuron < biases[layer].length; neuron++ ) {
            globalDeltaB[layer][neuron] += deltab[layer][neuron];
            for ( int weight = 0; weight < weights[layer][neuron].length; weight ++ ) {
              globalDeltaW[layer][neuron][weight] += deltaw[layer][neuron][weight]; 
            }
          } 
        }
      
        
      } // for each sample in batch

      // update the network

        for ( int layer = 0; layer < biases.length; layer++ ) {
          for ( int neuron = 0; neuron < biases[layer].length; neuron++ ) {
            biases[layer][neuron] += ((-eta/samples.length)*globalDeltaB[layer][neuron]);
            for ( int weight = 0; weight < weights[layer][neuron].length; weight ++ ) {
              weights[layer][neuron][weight] += ((-eta/samples.length)*globalDeltaW[layer][neuron][weight]);
            }
          } 
        }
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

    private static double[] vectorizeLabel(int label) {
         double[] vecLabel = new double[10];
         Arrays.fill(vecLabel, 0.0);
         vecLabel[label] = 1.0;
         return vecLabel;
    }

    private static double[] costPrimeVec(double[] activation, int label) {
         double[] ret = new double[activation.length];
         double[] vecLabel = vectorizeLabel(label);

         for (int dim = 0; dim < activation.length; dim++) {
  //         System.out.print("neuron activation : " + activation[dim] + " " + vecLabel[dim]);
           ret[dim] = (activation[dim] - vecLabel[dim]);
           //System.out.println(" " + ret[dim]);
         }
         return ret; 
    }

    private static double sigmoidPrime(double potential) {
//         System.out.println("sigmoidprime : " + sigmoid(potential)*(1.0-sigmoid(potential)));
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

    private static int max(double[] a) {
      int mindex = 0;
      for ( int i = 1; i < a.length; i++ ) {
        if (a[i] > a[mindex]) {
          mindex = i;
        }
      }
      return mindex;
    }

    private static double sigmoid(double value) {
      return 1.0/(1.0 + Math.exp(-value));
    }

    private static double currentError;
    private static double[][] biases;
    private static double[][][] weights;
    private static InputData trainingData;
    private static InputData testingData;
    private static long seed;
    private static int numEpochs;
    private static int batchSize;
    private static int[] nnShape;
}
