package fileIO;

import java.io.*;

public class MyFileReader {

    private BufferedReader bufferedReader;

    public MyFileReader(String inputFilePath) {
        try {
            String path = inputFilePath;
            bufferedReader = new BufferedReader(new FileReader(path));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getNextLine() {
        try {
            String line = bufferedReader.readLine();
            if (line != null) {
                return line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
