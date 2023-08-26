package net.palettehub.api.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

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

    @Valid
    @NotBlank
    @Pattern(regexp = "^.{1,64}$", message="Name must be between 1 and 64 characters.")
    @Column
    private String name;

    @Column(name = "picture_url")
    @JsonProperty("picture_url")
    private String pictureUrl;

    @Column
    @JsonProperty(access = Access.WRITE_ONLY)
    private String email;

    @Valid
    @NotNull
    @Column(name = "show_picture")
    @JsonProperty("picture_visible")
    private boolean showPicture;

    @Valid
    @NotBlank
    @Pattern(regexp = "^[A-Fa-f0-9]{6}$", message="Banner color left is not a 6 digit hex color code.")
    @Column(name = "banner_color_1")
    @JsonProperty("banner_color_left")
    private String bannerColorLeft;

    @Valid
    @NotBlank
    @Pattern(regexp = "^[A-Fa-f0-9]{6}$", message="Banner color right is not a 6 digit hex color code.")
    @Column(name = "banner_color_2")
    @JsonProperty("banner_color_right")
    private String bannerColorRight;

    @Column(name = "role")
    @JsonProperty("role")
    private String role;

    @Column
    private int palettes;

    @Column
    private int likes;

    @Column
    private int liked;

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

    public int getPalettes() {
        return this.palettes;
    }

    public void setPalettes(int palettes) {
        this.palettes = palettes;
    }

    public int getLikes() {
        return this.likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getLiked() {
        return this.liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }

    @Override
    public String toString() {
        return "{" +
            " userId='" + getUserId() + "'" +
            ", googleId='" + getGoogleId() + "'" +
            ", name='" + getName() + "'" +
            ", pictureUrl='" + getPictureUrl() + "'" +
            ", email='" + getEmail() + "'" +
            ", showPicture='" + isShowPicture() + "'" +
            ", bannerColorLeft='" + getBannerColorLeft() + "'" +
            ", bannerColorRight='" + getBannerColorRight() + "'" +
            ", role='" + getRole() + "'" +
            ", palettes='" + getPalettes() + "'" +
            ", likes='" + getLikes() + "'" +
            ", liked='" + getLiked() + "'" +
            "}";
    }
    
}
