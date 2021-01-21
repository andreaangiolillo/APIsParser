package Model.Vowl;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/** Vowl represent the json file used by WebVOWL to create the network. */
public class Vowl {

  @SerializedName(value = "class")
  private List<VowlClass> classes;

  @SerializedName(value = "property")
  private List<VowlProperty> properties;

  @SerializedName(value = "propertyAttribute")
  private List<VowlPropertyAttribute> propertyAttribute;

  @SerializedName(value = "classAttribute")
  private List<VowlClassAttribute> classAttributes;

  public Vowl(
      List<VowlClass> classes,
      List<VowlProperty> properties,
      List<VowlPropertyAttribute> propertyAttribute,
      List<VowlClassAttribute> classAttributes) {

    this.classes = classes;
    this.properties = properties;
    this.propertyAttribute = propertyAttribute;
    this.classAttributes = classAttributes;
  }

  public List<VowlClass> getClasses() {
    return classes;
  }

  public List<VowlProperty> getProperties() {
    return properties;
  }

  public List<VowlPropertyAttribute> getPropertyAttribute() {
    return propertyAttribute;
  }
}
