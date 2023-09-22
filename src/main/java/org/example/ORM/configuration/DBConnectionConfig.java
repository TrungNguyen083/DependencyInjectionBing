package org.example.ORM.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DBConnectionConfig {
    private String url;
    private String username;
    private String password;

    @JsonProperty("URL")
    public String getURL() { return url; }
    @JsonProperty("URL")
    public void setURL(String value) { this.url = value; }

    @JsonProperty("USERNAME")
    public String getUsername() { return username; }
    @JsonProperty("USERNAME")
    public void setUsername(String value) { this.username = value; }

    @JsonProperty("PASSWORD")
    public String getPassword() { return password; }
    @JsonProperty("PASSWORD")
    public void setPassword(String value) { this.password = value; }
}
