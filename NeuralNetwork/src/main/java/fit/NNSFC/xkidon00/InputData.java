package fit.NNSFC.xkidon00;
import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.nio.file.*;
import java.nio.channels.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class InputData {

  InputData(int[][] s, int[] l) {
    samples = s;
    labels = l;   
  }

  public static int sampleSize() {
    return samples.length;
  }
  
  public static int[][] getSamples() {
    return samples;
  }

  public static int[] getLabels() {
    return labels;
  }

  public int[] getSample(int index) {
    return samples[index];
  }  

  public int getLabel(int index) {
    return labels[index];
  }

  private static int  samples[][];
  private static int labels[];   
}
