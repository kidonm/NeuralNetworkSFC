package fit.NNSFC.xkidon00;
import java.util.*;
import java.util.regex.*;
import java.io.*;

// jython related
import org.python.core.PyDictionary;
import org.python.core.PyFile;
import org.python.core.PyList;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.modules.cPickle;

class MnistWrapper {
    MnistWrapper(String filename) {
        location = filename;
    }

    public List<List<Double>> loadData() {
/*
        File data = new File(location);
        StringBuilder strBuilder = new StringBuilder();
*/
        PyList list = new PyList();
        try {
   //         BufferedReader buffReader = new BufferedReader(new FileReader(data));
            InputStream input = new FileInputStream(location);
/*
            String aLine;
            while (null != (aLine = buffReader.readLine())) {
                strBuilder.append(aLine);//.append("\n");
*/  
             list = (PyList) cPickle.load(new PyFile(input));         
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        //PyString pyStr = new PyString(strBuilder.toString());

        return list.subList(0, list.size());
    }

    public List<Double> loadLabels() {
        PyList list = new PyList();
        try {
            InputStream input = new FileInputStream(location);
             list = (PyList) cPickle.load(new PyFile(input));         
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return list.subList(0, list.size());
    }

    private String location;
}
