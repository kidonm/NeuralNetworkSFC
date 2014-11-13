package fit.NNSFC.xkidon00;

import fit.NNSFC.xkidon00.InputData;

import java.util.*;
import java.lang.*;
import java.util.regex.*;
import java.io.*;
import java.nio.file.*;
import java.nio.channels.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import static java.nio.file.StandardOpenOption.*;

class MnistWrapper {
    public static void main(String... args) {
      try {
        MnistWrapper mw = new MnistWrapper(args[0], args[1]);
        mw.loadData();
      } catch(IOException e) {
         System.out.println(e);
      }
    }

    MnistWrapper(String trFilename, String lblFilename) {
        trainingDat = trFilename;
        trainingLbl = lblFilename;
    }

    public InputData loadData() throws IOException{

        Path path = Paths.get(trainingDat);
        ReadableByteChannel trainingData = Files.newByteChannel(path, EnumSet.of(READ));

        path = Paths.get(trainingLbl);
        ReadableByteChannel trainingLabels = Files.newByteChannel(path, EnumSet.of(READ));

        byte[][] samples = new byte[60000][784] ;
        byte[] labels = new byte[60000];

        ByteBuffer trbuff = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
        ByteBuffer lblbuff = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
        ByteBuffer sampleBuff = ByteBuffer.allocate(1);
        ByteBuffer labelBuff = ByteBuffer.allocate(1);

        trainingData.read(trbuff);
        trainingLabels.read(lblbuff);
// magic number check        
        if(trbuff.getInt(0) != 2051 && lblbuff.getInt(0) != 2051) {
          System.out.println("magic numbers do not match");
          System.exit(1);
        }
        trbuff.flip();
        lblbuff.flip();

        trainingData.read(trbuff);
        trainingLabels.read(lblbuff);
// data length check
        if(trbuff.getInt(0) != 60000 && lblbuff.getInt(0) != 60000) {
          System.out.println("data lenght does not match");
          System.exit(1);
        }
        trbuff.flip();
        lblbuff.flip();

        trainingData.read(trbuff);
        trainingLabels.read(lblbuff);
        if(trbuff.getInt(0) != 28 && lblbuff.getInt(0) != 28) {
          System.out.println("rows do not match");
          System.exit(1);
        }
        trbuff.flip();
        lblbuff.flip();

        trainingData.read(trbuff);
        trainingLabels.read(lblbuff);
        if(trbuff.getInt(0) != 28 && lblbuff.getInt(0) != 28) {
          System.out.println("cols do not match");
          System.exit(1);
        }
        trbuff.flip();
        lblbuff.flip();

        int read;
        for ( int i = 0; i < 60000; i++ ) {
          read = trainingLabels.read(labelBuff);
          labelBuff.flip();
         // try {  
            labels[i] = labelBuff.get();
	 //   } catch(IndexOutOfBoundsException e) {
	  //    System.out.println(i);
           //   System.out.println(read);
           // }

          for ( int j = 0; j < 784 /* 28x28 */; j++ ) {
            trainingData.read(sampleBuff);
            sampleBuff.flip();
            samples[i][j] = sampleBuff.get();
          }
        }
        return new InputData(samples, labels);
    }

    private String trainingDat;
    private String trainingLbl;
}
