package Model;

import java.util.ArrayList;
import java.util.List;

/** Resource represents an API Resource */
public class Resource {

  private String name;
  private String javaClass;
  private String baseURL;
  private String[] dependencies;
  private Endpoint[] endpoints;

  public Resource() {
    super();
  }

  public void setDependencies(List<String> list) {
    if (list == null) {
      this.dependencies = null;
    } else {
      String[] dependencies = new String[list.size()];
      for (int i = 0; i < list.size(); i++) {
        dependencies[i] = list.get(i);
      }
      this.dependencies = dependencies;
    }
  }

  public void setEndpoints(List<Endpoint> listEndpoints) {
    Endpoint[] endpoints = new Endpoint[listEndpoints.size()];
    for (int i = 0; i < listEndpoints.size(); i++) {
      endpoints[i] = listEndpoints.get(i);
    }
    this.endpoints = endpoints;
  }

  public List<String> getDependencies() {
    List<String> list = new ArrayList<>();
    if (dependencies != null && dependencies.length > 0) {
      for (int i = 0; i < dependencies.length; i++) {
        list.add(dependencies[i]);
      }
    }

    return list;
  }

  public String getName() {
    return name;
  }

  public void setName() {
    this.name = this.baseURL.substring(this.baseURL.lastIndexOf("/") + 1);
  }

  public void setJavaClass(String javaClass) {
    this.javaClass = javaClass;
  }

  public void setBaseURL(String baseURL) {
    this.baseURL = baseURL;
    setName();
  }

  public Endpoint[] getEndpoints() {
    return endpoints;
  }

  public String getJavaClass() {
    return javaClass;
  }

  public String getBaseURL() {
    return baseURL;
  }
}
