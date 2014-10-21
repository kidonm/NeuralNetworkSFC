import java.util.*;


class NeuralNetwork {
    public static void main(String[] args) {
        NeuralNetwork net = new NeuralNetwork(Arrays.asList(784,15,10));
        System.out.println(biases);
        System.out.println(weights);
    }

    private List<List> initBiases (List<Integer> networkShape) {
        List<List> retB = new ArrayList<List>();
        List<Integer> layer;

        for (int i = 1; i < networkShape.size(); i++) {
            layer = new ArrayList<Integer>();
            for (int j =0; j < networkShape.get(i); j++) {
                layer.add(0);
            }
            retB.add(layer);
        } 
        return retB;
    }

    private List<List> initWeights (List<Integer> networkShape) {
        List<List> retW = new ArrayList<List>();
        List<List> layer;
        List<Integer> neuron;
        for (int i = 1; i < networkShape.size(); i++) {
            layer = new ArrayList<List>();
            for (int j =0; j < networkShape.get(i); j++) {
                neuron = new ArrayList<Integer>();
		for (int k=0; k < networkShape.get(i-1); k++) {
                    neuron.add(0); 
		}
                layer.add(neuron);
            }
            retW.add(layer);
        } 
        return retW; 
    }

    NeuralNetwork(List<Integer> networkShape) {
        biases = initBiases(networkShape); 
        weights = initWeights(networkShape);
    }    

    private static List<List> biases;
    private static List<List> weights;
    private static long seed;
}
