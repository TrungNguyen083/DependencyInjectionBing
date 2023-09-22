package org.example.dilibrary.configuaration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ControllerConfig {
    private String basePackage;
    private String fileExtension;
    private String forNamePackage;

    @JsonProperty("basePackage")
    public String getBasePackage() { return basePackage; }
    @JsonProperty("basePackage")
    public void setBasePackage(String value) { this.basePackage = value; }

    @JsonProperty("fileExtension")
    public String getFileExtension() { return fileExtension; }
    @JsonProperty("fileExtension")
    public void setFileExtension(String value) { this.fileExtension = value; }

    @JsonProperty("forNamePackage")
    public String getForNamePackage() { return forNamePackage; }
    @JsonProperty("forNamePackage")
    public void setForNamePackage(String value) { this.forNamePackage = value; }
}
