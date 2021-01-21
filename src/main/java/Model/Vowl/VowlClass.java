package Model.Vowl;

import com.google.gson.annotations.SerializedName;

/** VowlClass represents a node in the network. */
public class VowlClass {

  private String id;
  private String type;
  private String label;

  @SerializedName("comment")
  private String javaClass;

  @SerializedName("description")
  private String basePath;

  // Excluded from the serialization
  private transient boolean service;

  public static final String CLASS_TYPE = "owl:Class";
  public static final String ENDPOINT_TYPE = "owl:Thing";

  public VowlClass(String id, String type, String label) {
    this.id = id;
    this.type = type;
    this.label = label;
    this.service = false;
  }

  public VowlClass(String id, String type, String label, boolean service) {
    this.id = id;
    this.type = type;
    this.label = label;
    this.service = service;
  }

  public VowlClass(String id, String type, String label, String javaClass, String basePath) {
    this.id = id;
    this.type = type;
    this.label = label;
    this.javaClass = javaClass;
    this.basePath = basePath;
    this.service = false;
  }

  public String getType() {
    return type;
  }

  public String getId() {
    return id;
  }

  public boolean isService() {
    return service;
  }
}
