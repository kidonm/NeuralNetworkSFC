package fit.NNSFC.xkidon00;
import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.nio.file.*;
import java.nio.channels.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class InputData {

  InputData(byte[][] s, byte[] l) {
    samples = s;
    labels = l;   
  }

  public byte[] getSample(int index) {
    return samples[index];
  }  

  public byte getLabel(int index) {
    return labels[index];
  }

  private static byte samples[][];
  private static byte labels[];   
}
