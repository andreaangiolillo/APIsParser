package Stream;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Logger;

public class FileReader {

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  /**
   * getFilesInDirectory returns files in a directory. Nested directories are visited.
   *
   * @param directory
   * @return
   */
  public static List<File> getFilesInDirectory(String directory) {
    Queue<File> directories = new LinkedList<>();
    List<File> files = new ArrayList<>();

    directories.add(new File(directory));

    while (!directories.isEmpty()) {
      File folder = new File(directories.remove().getAbsolutePath());
      File[] listOfFiles = folder.listFiles();

      LOGGER.info("Starting to search java files inside " + directory);
      for (int i = 0; i < listOfFiles.length; i++) {
        if (listOfFiles[i].isFile()) {
          if (isAJavaResource(listOfFiles[i])) {
            LOGGER.info("File " + listOfFiles[i].getAbsolutePath());
            files.add(listOfFiles[i]);
          }

        } else if (listOfFiles[i].isDirectory()) {
          LOGGER.info("Directory " + listOfFiles[i].getName());
          directories.add(listOfFiles[i]);
        }
      }
    }

    return files;
  }

  private static boolean isAJavaResource(File file) {
    return file.getName().contains("Resource.java");
  }
}
