package unit;

import static junit.framework.TestCase.assertEquals;

import Stream.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;

public class FileReaderUnitTest {

  final String DIRECTORY = "src/test/resources/testGetFilesInDirectoryFiles";

  @Test
  public void testGetFilesInDirectory() {
    // Create a directory
    File directory = new File(DIRECTORY);
    directory.mkdir();

    // Create 50 java resource files
    for (int i = 0; i < 50; i++) {
      File file = new File(DIRECTORY + "/file" + i + "Resource.java");
      try {
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    List<File> files = FileReader.getFilesInDirectory(directory.getAbsolutePath());
    assertEquals(50, files.size());

    // Create 10 json files
    for (int i = 0; i < 10; i++) {
      File file = new File(DIRECTORY + "/file" + i + "Resource.json");
      try {
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    files = FileReader.getFilesInDirectory(directory.getAbsolutePath());
    assertEquals(50, files.size());

    // Create 10 java files that are not resources
    for (int i = 0; i < 10; i++) {
      File file = new File(DIRECTORY + "/file" + i + ".java");
      try {
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    files = FileReader.getFilesInDirectory(directory.getAbsolutePath());
    assertEquals(50, files.size());
  }

  @After
  public void removeDirectory() throws IOException {
    FileUtils.cleanDirectory(new File(DIRECTORY));
  }
}
