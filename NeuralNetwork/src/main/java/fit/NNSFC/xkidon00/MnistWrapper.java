package fit.NNSFC.xkidon00;

import fit.NNSFC.xkidon00.InputData;

import java.util.*;
import java.awt.image.*;
import java.awt.*;
import java.lang.*;
import java.util.regex.*;
import java.io.*;
import java.nio.file.*;
import java.nio.channels.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.imageio.ImageIO;
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

        int dataLength = 0;

        ByteBuffer trbuff = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
        ByteBuffer lblbuff = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
        ByteBuffer sampleBuff = ByteBuffer.allocate(1);
        ByteBuffer labelBuff = ByteBuffer.allocate(1);

        trainingData.read(trbuff);
        trainingLabels.read(lblbuff);

        // magic number check        
        trbuff.getInt(0) ;
        lblbuff.getInt(0) ;
        trbuff.flip();
        lblbuff.flip();

        trainingData.read(trbuff);
        trainingLabels.read(lblbuff);
        // data length check
        if(trbuff.getInt(0) != lblbuff.getInt(0)) {
          System.out.println("data lenght does not match " + trbuff.getInt(0) + " vs " + lblbuff.getInt(0));
          System.exit(1);
        } else {
          dataLength = trbuff.getInt(0); 
        }

        double[][] samples = new double[dataLength][784] ;
        int[] labels = new int[dataLength];

        trbuff.flip();
        lblbuff.flip();

        trainingData.read(trbuff);
        if(trbuff.getInt(0) != 28) {
          System.out.println("rows do not match");
          System.exit(1);
        }
        trbuff.flip();

        trainingData.read(trbuff);
        if(trbuff.getInt(0) != 28 ) {
          System.out.println("cols do not match");
          System.exit(1);
        }
        trbuff.flip();

        int readL = 0;
        int readS = 0;
        for ( int i = 0; i < dataLength; i++ ) {
          readL = trainingLabels.read(labelBuff);
          labels[i] = labelBuff.get(0);
          labelBuff.flip();
          if ( readL != 1) {
            System.out.println("cannot read labels" + i);
            System.exit(1);
          }

          for ( int j = 0; j < 28 /* 28x28 */; j++ ) {
            for ( int k = 0; k < 28 ; k++ ) {
		    readS = trainingData.read(sampleBuff);
		    //samples[i][j * k] = (sampleBuff.get(0) & 0xff) / 255.0;
                    System.out.print((((sampleBuff.get(0) & 0xff) == 0) ? 0 : " ") + " ");
		    sampleBuff.flip();
		    if ( readS != 1) {
		      System.out.print("cannot read samples " + j);
		      System.exit(1);
		    }
            }
            System.out.println();
          }
          //System.out.println(Arrays.toString((int[])samples[i]));
          //System.exit(1);
        }
        return new InputData(samples, labels);
    }

    private String trainingDat;
    private String trainingLbl;
}
