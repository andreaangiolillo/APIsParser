package Stream;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class FileWriter {

  public static void writeStringToFile(String path, String text) throws FileNotFoundException {
    try (PrintWriter out = new PrintWriter(path)) {
      out.println(text);
    }
  }
}
