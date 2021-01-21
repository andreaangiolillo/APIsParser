package Model;

import java.util.List;

/** Endpoint represents an API Endpoint. */
public class Endpoint {

  public static final String API_TYPE_ALL = "ALL";
  public static final String API_TYPE_WEB_API = "WEB_API";
  public static final String API_TYPE_PUBLIC_API = "PUBLIC_API";

  private String httpMethod;
  private String path;
  private String type;
  private String[] rolesAllowed;
  private Parameter[] inputParameters;

  public Endpoint() {
    super();
  }

  public void setHttpMethod(String httpMethod) {
    this.httpMethod = httpMethod;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public void setRolesAllowed(String[] rolesAllowed) {
    this.rolesAllowed = rolesAllowed;
  }

  public void setInputParameters(List<Parameter> params) {
    if (params == null) {
      this.inputParameters = null;
    } else {
      Parameter[] inputParameters = new Parameter[params.size()];

      for (int i = 0; i < params.size(); i++) {
        inputParameters[i] = params.get(i);
      }

      this.inputParameters = inputParameters;
    }
  }

  public String getHttpMethod() {
    return httpMethod;
  }

  public String getPath() {
    return path;
  }

  public String[] getRolesAllowed() {
    return rolesAllowed;
  }

  public Parameter[] getInputParameters() {
    return inputParameters;
  }
}
