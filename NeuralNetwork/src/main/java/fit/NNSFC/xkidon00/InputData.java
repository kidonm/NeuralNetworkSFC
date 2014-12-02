package fit.NNSFC.xkidon00;
import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.nio.file.*;
import java.nio.channels.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class InputData {

  InputData(double[][] s, int[] l) {
    samples = s;
    labels = l;   
  }

  public int sampleSize() {
    return samples.length;
  }
  
  public double[][] getSamples() {
    return samples;
  }

  public int[] getLabels() {
    return labels;
  }

  public double[] getSample(int index) {
    return samples[index];
  }  

  public int getLabel(int index) {
    return labels[index];
  }

  private double  samples[][];
  private int labels[];   
}
