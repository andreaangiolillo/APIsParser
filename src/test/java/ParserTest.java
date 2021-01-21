import static junit.framework.TestCase.assertEquals;

import Model.Endpoint;
import Model.Resource;
import Model.Vowl.Vowl;
import Model.Vowl.VowlClass;
import Parser.Parser;
import Stream.FileReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class ParserTest {

  final String DIRECTORY = "src/test/resources/ParserTest";

  @Test
  public void parseJavaClassesTest() throws IOException {
    List<File> files = FileReader.getFilesInDirectory(DIRECTORY + "/java");
    Parser parser = new Parser(Endpoint.API_TYPE_PUBLIC_API);
    String jsonString = getJson(parser.parseJavaClasses(files));

    String expected =
        FileUtils.readFileToString(
            new File(DIRECTORY + "/json/ParseJavaClassesTest.json"), "UTF-8");
    ;

    assertEquals(expected, jsonString);

    // Consider only PROGRAMMATIC endpoints
    parser.setApiType(Endpoint.API_TYPE_PUBLIC_API);
    jsonString = getJson(parser.parseJavaClasses(files));
    assertEquals(expected, jsonString);

    // Consider only UI endpoints
    String BillingSearchResource =
        FileUtils.readFileToString(
            new File(DIRECTORY + "/json/BillingSearchResource.json"), "UTF-8");

    parser.setApiType(Endpoint.API_TYPE_WEB_API);
    jsonString = getJson(parser.parseJavaClasses(files));
    assertEquals(BillingSearchResource, jsonString);
  }

  @Test
  public void getVowlFromResourcesTest() throws IOException {
    List<File> files = FileReader.getFilesInDirectory(DIRECTORY + "/java");
    Parser parser = new Parser(Endpoint.API_TYPE_ALL);
    List<Resource> resources = parser.parseJavaClasses(files);
    Vowl vowl = parser.getVowlFromResources(resources);

    String WebVOWL =
        FileUtils.readFileToString(new File(DIRECTORY + "/json/WebVOWL.json"), "UTF-8");
    Vowl expected = getJava(WebVOWL);

    assertEquals(26, expected.getClasses().size(), vowl.getClasses().size());
    assertEquals(23, expected.getProperties().size(), vowl.getProperties().size());
    assertEquals(23, expected.getPropertyAttribute().size(), vowl.getPropertyAttribute().size());

    List<VowlClass> expectedClasses = expected.getClasses();
    List<VowlClass> vowlClasses = vowl.getClasses();

    assertEquals(expectedClasses.size(), vowlClasses.size());

    for (int i = 0; i < expectedClasses.size(); i++) {
      assertEquals(expectedClasses.get(i).getId(), vowlClasses.get(i).getId());
    }
  }

  private static Vowl getJava(String json) {
    Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    return gson.fromJson(json, Vowl.class);
  }

  private static String getJson(Object resources) {
    String text = "";
    Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    text = gson.toJson(resources);
    return text;
  }
}
