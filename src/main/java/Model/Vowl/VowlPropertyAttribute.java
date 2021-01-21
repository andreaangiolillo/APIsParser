package Model.Vowl;

import com.google.gson.annotations.SerializedName;

/** VowlPropertyAttribute represents an edge in the network. */
public class VowlPropertyAttribute {

  private String id;
  private String type;

  @SerializedName("domain")
  private String fromNode;

  @SerializedName("range")
  private String toNode;

  private String label;

  public static final String TYPE = "owl:ObjectProperty";
  public static final String ENDPOINT_TYPE = "rdf:Property";
  public static final String ENDPOINT_LABEL = "belongs to";
  public static final String DEP_LABEL = "depends on";

  public VowlPropertyAttribute(
      String id, String type, String fromNode, String toNode, String label) {
    this.id = id;
    this.type = type;
    this.fromNode = fromNode;
    this.toNode = toNode;
    this.label = label;
  }

  public String getId() {
    return id;
  }
}
