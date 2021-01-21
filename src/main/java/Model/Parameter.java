package Model;

/** Parameter represent an endpoint's parameter */
public class Parameter {
  private String name;
  private String type;
  private String[] annotations;

  public Parameter(String name, String type, String[] annotations) {
    this.name = name;
    this.type = type;
    this.annotations = annotations;
  }
}
