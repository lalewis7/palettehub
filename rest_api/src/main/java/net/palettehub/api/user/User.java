package net.palettehub.api.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {
    
    @Id
    @Column(name = "user_id")
    @JsonProperty("user_id")
    private String userId;

    @Column(name = "google_id")
    @JsonProperty("google_id")
    private String googleId;

    @Column
    private String name;

    @Column(name = "picture_url")
    @JsonProperty("picture_url")
    private String pictureUrl;

    @Column
    private String email;


    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGoogleId() {
        return this.googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureUrl() {
        return this.pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "{" +
            " userId='" + getUserId() + "'" +
            ", googleId='" + getGoogleId() + "'" +
            ", name='" + getName() + "'" +
            ", pictureUrl='" + getPictureUrl() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
    
}
