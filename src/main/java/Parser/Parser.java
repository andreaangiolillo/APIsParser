package Parser;

import Model.Endpoint;
import Model.Resource;
import Model.Vowl.Vowl;
import Model.Vowl.VowlClass;
import Model.Vowl.VowlClassAttribute;
import Model.Vowl.VowlProperty;
import Model.Vowl.VowlPropertyAttribute;
import Parser.Visitor.ClassVisitor;
import Parser.Visitor.ImportVisitor;
import Parser.Visitor.MethodVisitor;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Parser {

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  String _apiType;
  Set<String> _services;

  public Parser(String apiType) {
    this._apiType = apiType;
    this._services = new HashSet<>();
  }
  /**
   * @param files
   * @return
   * @throws FileNotFoundException
   */
  public List<Resource> parseJavaClasses(List<File> files) throws FileNotFoundException {
    if (files.size() == 0) {
      return null;
    }

    List<Resource> resources = new ArrayList<>(files.size());
    Resource resource;
    for (File file : files) {
      LOGGER.info("Parsing File " + file.getName());
      resource = parseJavaClass(file);
      if (resource != null) {
        resources.add(resource);
      }
    }

    return resources;
  }

  /**
   * parseJavaClass goes through a java class and create a Resource.
   *
   * @param file
   * @return
   * @throws FileNotFoundException
   */
  private Resource parseJavaClass(File file) throws FileNotFoundException {
    CompilationUnit cu = StaticJavaParser.parse(file);

    // Get the class information
    VoidVisitor<Resource> classVisitor = new ClassVisitor();
    Resource resource = new Resource();
    classVisitor.visit(cu, resource);

    if (!resourceValidation(resource)) {
      return null;
    }

    // Get imports information
    VoidVisitor<Resource> importVisitor = new ImportVisitor();
    importVisitor.visit(cu, resource);

    // Get methods information
    List<Endpoint> endpoints = new ArrayList<>();
    VoidVisitor<List<Endpoint>> methodVisitor = new MethodVisitor();
    methodVisitor.visit(cu, endpoints);

    endpoints = endpointFilter(endpoints);
    if (endpoints.isEmpty()) {
      LOGGER.warning(
          String.format(resource.getJavaClass() + " doesn't have %s endpoints", getApiType()));
      return null;
    }

    resource.setEndpoints(endpoints);
    return resource;
  }

  /**
   * @param resource
   * @return
   */
  private static boolean resourceValidation(Resource resource) {
    if (resource.getBaseURL() == null || resource.getBaseURL() == "") {
      LOGGER.warning(resource.getJavaClass() + " doesn't have a base url and won't be parsed");
      return false;
    }
    return true;
  }

  /**
   * endpointFilter returns all the endpoints that match the apiType. It returns all the endpoints
   * when apiType = API_TYPE_ALL
   *
   * @param endpoints
   * @return
   */
  private List<Endpoint> endpointFilter(List<Endpoint> endpoints) {
    String apiType = getApiType();
    if (apiType.equals(Endpoint.API_TYPE_WEB_API)) {
      return endpoints.stream()
          .filter(endpoint -> endpoint.getType() == Endpoint.API_TYPE_WEB_API)
          .collect(Collectors.toList());
    }

    if (apiType.equals(Endpoint.API_TYPE_PUBLIC_API)) {
      return endpoints.stream()
          .filter(endpoint -> endpoint.getType() == Endpoint.API_TYPE_PUBLIC_API)
          .collect(Collectors.toList());
    }

    return endpoints;
  }

  /**
   * @param resources
   * @return
   */
  public Vowl getVowlFromResources(List<Resource> resources) {
    List<VowlClass> vowlClasses = new ArrayList<>();
    List<VowlPropertyAttribute> vowlPropertyAttrs = new ArrayList<>();

    for (Resource resource : resources) {
      List<VowlClass> vowlClassResource = getVowlClasses(resource);
      List<VowlPropertyAttribute> propertyAttributeResource =
          getVowlPropertyAttribute(vowlClassResource, resource);

      vowlClasses.addAll(vowlClassResource);
      vowlPropertyAttrs.addAll(propertyAttributeResource);
    }

    List<VowlProperty> vowlProps = getVowlProperties(vowlPropertyAttrs);
    List<VowlClassAttribute> vowlClassProperties = getVowlClassAttribute(vowlClasses);

    return new Vowl(vowlClasses, vowlProps, vowlPropertyAttrs, vowlClassProperties);
  }

  /**
   * @param resource
   * @return
   */
  private List<VowlClass> getVowlClasses(Resource resource) {
    List<VowlClass> vowlClasses = new ArrayList<>();
    // Add resource
    vowlClasses.add(
        new VowlClass(
            resource.getJavaClass(),
            VowlClass.CLASS_TYPE,
            resource.getName(),
            resource.getJavaClass(),
            resource.getBaseURL()));

    // Add endpoints
    for (Endpoint endpoint : resource.getEndpoints()) {
      String label = endpoint.getHttpMethod() + " " + endpoint.getPath();
      String id = resource.getBaseURL() + endpoint.getHttpMethod() + endpoint.getPath();
      vowlClasses.add(new VowlClass(id, VowlClass.ENDPOINT_TYPE, label));
    }

    // Add services
    for (String dep : resource.getDependencies()) {
      _services = getDependencies();
      if (!_services.contains(dep)) {
        _services.add(dep);
        vowlClasses.add(new VowlClass(dep, VowlClass.CLASS_TYPE, dep, true));
      }
    }

    return vowlClasses;
  }

  /**
   * @param vowlClasses
   * @param resource
   * @return
   */
  private List<VowlPropertyAttribute> getVowlPropertyAttribute(
      List<VowlClass> vowlClasses, Resource resource) {

    List<VowlPropertyAttribute> propertyAttributes = new ArrayList<>();

    // Creating edges from the resource to dependencies
    for (String dep : resource.getDependencies()) {
      VowlPropertyAttribute prop =
          new VowlPropertyAttribute(
              resource.getBaseURL() + "_dep_" + ThreadLocalRandom.current().nextInt(),
              VowlPropertyAttribute.ENDPOINT_TYPE,
              resource.getJavaClass(),
              dep,
              VowlPropertyAttribute.DEP_LABEL);
      propertyAttributes.add(prop);
    }

    // Getting endpoints
    List<VowlClass> endpoints =
        vowlClasses.stream()
            .filter(cl -> cl.getType() == VowlClass.ENDPOINT_TYPE)
            .collect(Collectors.toList());

    // Creating edges from endpoints to resource
    for (int i = 0; i < endpoints.size(); i++) {
      VowlPropertyAttribute prop =
          new VowlPropertyAttribute(
              endpoints.get(i).getId() + i,
              VowlPropertyAttribute.TYPE,
              endpoints.get(i).getId(),
              resource.getJavaClass(),
              VowlPropertyAttribute.ENDPOINT_LABEL);
      propertyAttributes.add(prop);
    }

    return propertyAttributes;
  }

  /**
   * @param propertyAttribute
   * @return
   */
  private List<VowlProperty> getVowlProperties(List<VowlPropertyAttribute> propertyAttribute) {
    List<VowlProperty> vowlProps = new ArrayList<>();
    for (VowlPropertyAttribute propertyAtt : propertyAttribute) {
      vowlProps.add(new VowlProperty(propertyAtt.getId()));
    }

    return vowlProps;
  }

  /**
   * @param vowlClasses
   * @return
   */
  private List<VowlClassAttribute> getVowlClassAttribute(List<VowlClass> vowlClasses) {
    List<VowlClassAttribute> vowlClassProps = new ArrayList<>();
    for (VowlClass vowlClass : vowlClasses) {
      if (vowlClass.isService()) {
        vowlClassProps.add(
            new VowlClassAttribute(vowlClass.getId(), VowlClassAttribute.SERVICE_ATTR));
      }
    }

    return vowlClassProps;
  }

  public Set<String> getDependencies() {
    return _services;
  }

  public String getApiType() {
    return _apiType;
  }

  public void setApiType(String _apiType) {
    this._apiType = _apiType;
  }
}
