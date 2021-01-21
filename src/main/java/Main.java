import Model.Endpoint;
import Model.Resource;
import Model.Vowl.Vowl;
import Parser.Parser;
import Stream.FileReader;
import Stream.FileWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

  static List<String> _apiDirs = new ArrayList<>();
  static String _apiType = Endpoint.API_TYPE_PUBLIC_API;
  static String _jsonFilePath = System.getProperty("user.dir") + "/APIs.json";
  static String _networkFilePath =
      System.getProperty("user.dir") + "/WebVOWL/src/app/data/WebVOWL.json";
  static Level _logLevel = Level.OFF;
  static boolean _networkChart = false;

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  public static void main(String[] args) throws Exception {
    setProperties(args);

    LOGGER.setLevel(_logLevel);

    List<File> files = new ArrayList<>();
    for (String apiDir : _apiDirs) {
      files.addAll(FileReader.getFilesInDirectory(apiDir));
    }

    LOGGER.info(files.size() + " files have been found");
    Parser parser = new Parser(_apiType);

    List<Resource> resources = parser.parseJavaClasses(files);
    String jsonString = getJson(resources);

    FileWriter.writeStringToFile(_jsonFilePath, jsonString);

    System.out.println("Your APIs have been converted in a json format.");

    if (_networkChart) {
      Vowl vowl = parser.getVowlFromResources(resources);
      String jsonStringVowl = getJson(vowl);

      FileWriter.writeStringToFile(_networkFilePath, jsonStringVowl);
    }
  }

  /**
   * @param resources
   * @return String
   */
  private static String getJson(Object resources) {
    String text = "";
    Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    text = gson.toJson(resources);
    return text;
  }

  /**
   * @param args
   * @throws Exception
   */
  private static void setProperties(String[] args) throws Exception {
    if (args == null || args.length < 2) {
      throw new Exception("You need to provide the directory of your APIs (--path)");
    }

    for (int i = 0; i < args.length; i++) {
      switch (args[i]) {
        case "--path":
          _apiDirs.add(args[i + 1]);
          i++;
          break;
        case "--type":
          _apiType = args[i + 1].toUpperCase();
          if (!Endpoint.API_TYPE_ALL.equals(_apiType)
              && !Endpoint.API_TYPE_PUBLIC_API.equals(_apiType)
              && !Endpoint.API_TYPE_WEB_API.equals(_apiType)) {
            throw new Exception(
                String.format(
                    "The value %s is not valid for --type. Valid values: ALL, UI, PROGRAMMATIC",
                    _apiType));
          }
          i++;
          break;
        case "--filePath":
          _jsonFilePath = args[i + 1];
          i++;
          break;
        case "--networkChartFilePath":
          _networkFilePath = args[i + 1];
          i++;
          break;
        case "--log":
          _logLevel = Level.ALL;
          break;
        case "--networkChart":
          _networkChart = true;
          break;
        default:
          throw new Exception(String.format("The flag %s is not valid", args[i]));
      }
    }

    if (_apiType == "") {
      throw new Exception("You need to provide the directory of your APIs (--path)");
    }
  }
}
