package net.palettehub.api.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * User model.
 * 
 * @author Arthur Lewis
 */
@Entity
public class User {
    
    @Id
    @Column(name = "user_id")
    @JsonProperty("user_id")
    private String userId;

    @Column(name = "google_id")
    @JsonProperty(defaultValue = "google_id", access = Access.WRITE_ONLY)
    private String googleId;

    @Column
    private String name;

    @Column(name = "picture_url")
    @JsonProperty("picture_url")
    private String pictureUrl;

    @Column
    @JsonProperty(access = Access.WRITE_ONLY)
    private String email;

    @Column(name = "show_picture")
    @JsonProperty("picture_visible")
    private boolean showPicture;

    @Column(name = "banner_color_1")
    @JsonProperty("banner_color_left")
    private String bannerColorLeft;

    @Column(name = "banner_color_2")
    @JsonProperty("banner_color_right")
    private String bannerColorRight;

    @Column(name = "role")
    @JsonProperty("role")
    private String role;

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

    public boolean isShowPicture() {
        return this.showPicture;
    }

    public boolean getShowPicture() {
        return this.showPicture;
    }

    public void setShowPicture(boolean showPicture) {
        this.showPicture = showPicture;
    }

    public String getBannerColorLeft() {
        return this.bannerColorLeft;
    }

    public void setBannerColorLeft(String bannerColorLeft) {
        this.bannerColorLeft = bannerColorLeft;
    }

    public String getBannerColorRight() {
        return this.bannerColorRight;
    }

    public void setBannerColorRight(String bannerColorRight) {
        this.bannerColorRight = bannerColorRight;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
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
