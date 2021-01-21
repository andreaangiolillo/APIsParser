package Model.Vowl;

/**
 * In order to add the property service to the node we needs to create a classAttribute. All the
 * VowlClass with service == true will have this attribute.
 */
public class VowlClassAttribute {

  public static final String SERVICE_ATTR = "service";

  private String id;
  private String[] attributes;

  public VowlClassAttribute(String id, String attribute) {
    this.id = id;
    this.attributes = new String[] {attribute};
  }
}
