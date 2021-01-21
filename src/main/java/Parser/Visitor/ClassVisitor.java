package Parser.Visitor;

import Model.Resource;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.util.ArrayList;
import java.util.List;

public class ClassVisitor extends VoidVisitorAdapter<Resource> {

  /**
   * visit parses a ClassOrInterfaceDeclaration representing a java class to a Resource Obj
   *
   * @param cl
   * @param resource
   */
  @Override
  public void visit(ClassOrInterfaceDeclaration cl, Resource resource) {
    super.visit(cl, resource);

    // Ignore the class without a base path
    String basePath = getPath(cl);
    if (basePath != "") {
      resource.setBaseURL(getPath(cl));
      resource.setDependencies(getDependencies(cl));

      if (cl.getFullyQualifiedName().isPresent()) {
        resource.setJavaClass(cl.getFullyQualifiedName().get());
      }
    }
  }

  /**
   * getPath returns the value of the javax.ws.rs.Path annotation for a class
   *
   * @param cl
   * @return
   */
  static String getPath(ClassOrInterfaceDeclaration cl) {
    NodeList<AnnotationExpr> annotations = cl.getAnnotations();
    for (AnnotationExpr annotation : annotations) {
      String name = annotation.getName().asString();
      if (name.equals("Path")) {
        List<Node> nodes = annotation.getChildNodes();
        // getChildNodes returns a list of nodes where the first element is "Path" and the second
        // element is the value. Example: @Path("/test"), output= ["Path", "/test"]
        if (nodes.size() == 2) {
          String path = nodes.get(1).toString();
          return path.substring(1, path.length() - 1);
        }
      }
    }
    return "";
  }

  /**
   * getDependencies returns the class dependencies. A dependence is a service that the class use
   * and can be found in the class variable definition or inside class constructors
   *
   * @param cl
   * @return
   */
  public List<String> getDependencies(ClassOrInterfaceDeclaration cl) {
    List<String> dependencies = new ArrayList<>();
    getDependenciesFromConstructors(cl, dependencies);
    getDependenciesFromClassVariables(cl, dependencies);
    return dependencies;
  }

  /**
   * getDependenciesFromConstructors returns an array containing all the dependencies found inside
   * the class constructors
   *
   * @param cl
   * @param dependencies
   */
  private void getDependenciesFromConstructors(
      ClassOrInterfaceDeclaration cl, List<String> dependencies) {

    for (ConstructorDeclaration constructor : cl.getConstructors()) {
      if (constructor.getAnnotationByName("Inject").isPresent()) {
        for (Parameter param : constructor.getParameters()) {
          if (param
              .getName()
              .asString()
              .contains("Svc")) { // if the name of the variable contains "Svc" is a service
            dependencies.add(param.getType().asString());
          }
        }
      }
    }
  }

  /**
   * getDependenciesFromClassVariables returns an array containing the dependencies found in the
   * class variables
   *
   * @param cl
   * @param dependencies
   */
  private void getDependenciesFromClassVariables(
      ClassOrInterfaceDeclaration cl, List<String> dependencies) {
    for (FieldDeclaration field : cl.getFields()) {
      VariableDeclarator variable =
          field.getVariables().get(0); // we don't support multiple declaration
      if (variable
          .getName()
          .asString()
          .contains("Svc")) { // if the name of the variable contains "Svc" is a service
        String dep = variable.getType().asString();
        if (!dependencies.contains(dep)) {
          dependencies.add(variable.getType().asString());
        }
      }
    }
  }
}
