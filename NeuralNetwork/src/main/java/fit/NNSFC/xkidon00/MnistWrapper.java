package fit.NNSFC.xkidon00;
import java.util.*;
import java.util.regex.*;
import java.io.*;
//import java.nio.file.Files;
import java.nio.file.*;
import java.nio.channels.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
//import java.nio.file.Path;
//import java.nio.file.Paths;
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

    public void loadData() throws IOException{

        Path path = Paths.get(trainingDat);
        ReadableByteChannel trainingData = Files.newByteChannel(path, EnumSet.of(READ));

        path = Paths.get(trainingLbl);
        ReadableByteChannel trainingLabels = Files.newByteChannel(path, EnumSet.of(READ));

        byte[][] samples = new byte[60000][784] ;

        ByteBuffer trbuff = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
        ByteBuffer lblbuff = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
        ByteBuffer sampleBuff = ByteBuffer.allocate(1);

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

        for ( int i = 0; i < 60000; i++ ) {
          for ( int j = 0; j < 784 /* 28x28 */; j++ ) {
            trainingData.read(sampleBuff);
            samples[i][j] = sampleBuff.get(0);
            sampleBuff.flip();
          }
          System.out.println(samples[i]);
          System.exit(1);
        }
    }

    private String trainingDat;
    private String trainingLbl;
}
