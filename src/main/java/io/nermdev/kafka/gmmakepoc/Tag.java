package io.nermdev.kafka.gmmakepoc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag {

//  @JsonProperty
  private int Quality;

//  @JsonProperty
  private String TagName;

//  @JsonProperty
  private String TimeStampUTC;

//  @JsonProperty
  private Float Value;

//  @JsonProperty
  private String vValue;

  @JsonProperty("Quality")
  public int getQuality() {
    return Quality;
  }

  @JsonProperty("TagName")
  public String getTagName() {
    return TagName;
  }

  @JsonProperty("TimeStampUTC")
  public String getTimeStampUTC() {
    return TimeStampUTC;
  }


  @JsonProperty("Value")
  public Float getValue() {
    return Value;
  }


  @JsonProperty("vValue")
  public String getvValue() {
    return vValue;
  }
}
