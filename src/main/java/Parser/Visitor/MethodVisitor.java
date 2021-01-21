package Parser.Visitor;

import Model.Endpoint;
import Model.Parameter;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.util.ArrayList;
import java.util.List;

public class MethodVisitor extends VoidVisitorAdapter<List<Endpoint>> {

  /**
   * visit parses a MethodDeclaration representing a java function to a List<Endpoint>
   *
   * @param md
   * @param endpoints
   */
  @Override
  public void visit(MethodDeclaration md, List<Endpoint> endpoints) {
    super.visit(md, endpoints);

    String httpMethod = getHTTPMethod(md);

    if (!httpMethod.isEmpty()) {
      Endpoint endpoint = new Endpoint();
      endpoint.setHttpMethod(getHTTPMethod(md));
      endpoint.setPath(getPath(md));
      endpoint.setRolesAllowed(getRoles(md));
      endpoint.setInputParameters(getParameters(md));
      endpoint.setType(getType(md));

      endpoints.add(endpoint);
    }
  }

  /**
   * getParameters returns the input parameters for an endpoint
   *
   * @param md
   * @return
   */
  private List<Parameter> getParameters(MethodDeclaration md) {
    NodeList<com.github.javaparser.ast.body.Parameter> parsedParams = md.getParameters();

    List<Parameter> out = new ArrayList<>(parsedParams.size());

    for (com.github.javaparser.ast.body.Parameter parsedParam : parsedParams) {
      NodeList<AnnotationExpr> parsedAnnotations = parsedParam.getAnnotations();
      String[] annotations = new String[parsedAnnotations.size()];

      // Get the param annotations
      for (int i = 0; i < parsedAnnotations.size(); i++) {
        annotations[i] = parsedAnnotations.get(i).toString().replaceAll("@|[|]", "");
      }

      // Create a new Parameter
      Parameter parameter =
          new Parameter(
              parsedParam.getName().asString(), parsedParam.getType().asString(), annotations);
      out.add(parameter);
    }

    return out;
  }

  /**
   * getPath returns the value of the javax.ws.rs.Path annotation for an endpoint
   *
   * @param md
   * @return
   */
  private String getPath(MethodDeclaration md) {
    NodeList<AnnotationExpr> annotations = md.getAnnotations();
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
   * getRoles returns the value of the javax.annotation.security.RolesAllowed annotation for an
   * endpoint
   *
   * @param md
   * @return
   */
  private String[] getRoles(MethodDeclaration md) {
    // Don't calculate roles if the method has @UiCall
    if (getType(md) == Endpoint.API_TYPE_WEB_API) {
      return null;
    }
    NodeList<AnnotationExpr> annotations = md.getAnnotations();
    for (AnnotationExpr annotation : annotations) {
      String name = annotation.getName().asString();
      if (name.equals("RolesAllowed")) {
        List<Node> nodes = annotation.getChildNodes();
        if (nodes.size() == 2) {
          return nodes
              .get(1)
              .toString()
              .substring(1)
              .replace(" ", "")
              .replaceAll("}|RoleSet.NAME.", "")
              .split(",");
        }
        return null;
      }
    }
    return null;
  }

  /**
   * getHTTPMethod returns the HTTP method for an endpoint
   *
   * @param md
   * @return
   */
  private String getHTTPMethod(MethodDeclaration md) {
    NodeList<AnnotationExpr> annotations = md.getAnnotations();
    for (AnnotationExpr annotation : annotations) {
      String name = annotation.getName().asString();
      if (name.matches("GET|PUT|DELETE|POST|PATCH")) {
        return name;
      }
    }
    return "";
  }

  /**
   * getType returns Endpoint.API_TYPE_UI if the function has the annotation @UiCall,
   * Endpoint.API_TYPE_PROGRAMMATIC otherwise.
   *
   * @param md
   * @return
   */
  private String getType(MethodDeclaration md) {
    NodeList<AnnotationExpr> annotations = md.getAnnotations();
    for (AnnotationExpr annotation : annotations) {
      String name = annotation.getName().asString();
      if (name.equals("UiCall")) {
        return Endpoint.API_TYPE_WEB_API;
      }
    }
    return Endpoint.API_TYPE_PUBLIC_API;
  }
}
