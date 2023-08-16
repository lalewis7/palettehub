package net.palettehub.api.palette;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Palette model.
 * 
 * @author Arthur Lewis
 */
@Entity
public class Palette {
    
    @Id
    @Column(name = "palette_id")
    @JsonProperty("palette_id")
    private String paletteId;

    @Column(name = "user_id")
    @JsonProperty("user_id")
    private String userId;

    // @Column(name = "user_name")
    // @JsonProperty("user_name")
    // private String userName;

    // @Column(name = "user_img")
    // @JsonProperty("user_img")
    // private String userImg;

    // @Column(name = "show_picture")
    // @JsonProperty(access = Access.WRITE_ONLY)
    // @JdbcTypeCode(SqlTypes.BOOLEAN)
    // private boolean userShowImg;

    @Valid
    @NotBlank
    @Pattern(regexp = "^[A-Fa-f0-9]{6}$", message="Color 1 is not a 6 digit hex color code.")
    @Column(name = "color_1")
    @JsonProperty("color_1")
    private String color1;

    @Valid
    @NotBlank
    @Pattern(regexp = "^[A-Fa-f0-9]{6}$", message="Color 2 is not a 6 digit hex color code.")
    @Column(name = "color_2")
    @JsonProperty("color_2")
    private String color2;

    @Valid
    @NotBlank
    @Pattern(regexp = "^[A-Fa-f0-9]{6}$", message="Color 3 is not a 6 digit hex color code.")
    @Column(name = "color_3")
    @JsonProperty("color_3")
    private String color3;

    @Valid
    @NotBlank
    @Pattern(regexp = "^[A-Fa-f0-9]{6}$", message="Color 4 is not a 6 digit hex color code.")
    @Column(name = "color_4")
    @JsonProperty("color_4")
    private String color4;

    @Valid
    @NotBlank
    @Pattern(regexp = "^[A-Fa-f0-9]{6}$", message="Color 5 is not a 6 digit hex color code.")
    @Column(name = "color_5")
    @JsonProperty("color_5")
    private String color5;

    @Column(name = "posted_timestamp")
    private long posted;

    @Column
    private int likes;

    @Column
    private boolean liked;

    // getters and setters

    public String getPaletteId() {
        return this.paletteId;
    }

    public void setPaletteId(String paletteId) {
        this.paletteId = paletteId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getColor1() {
        return this.color1;
    }

    public void setColor1(String color1) {
        this.color1 = color1;
    }

    public String getColor2() {
        return this.color2;
    }

    public void setColor2(String color2) {
        this.color2 = color2;
    }

    public String getColor3() {
        return this.color3;
    }

    public void setColor3(String color3) {
        this.color3 = color3;
    }

    public String getColor4() {
        return this.color4;
    }

    public void setColor4(String color4) {
        this.color4 = color4;
    }

    public String getColor5() {
        return this.color5;
    }

    public void setColor5(String color5) {
        this.color5 = color5;
    }

    public long getPosted() {
        return this.posted;
    }

    public void setPosted(long posted) {
        this.posted = posted;
    }

    public int getLikes() {
        return this.likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isLiked() {
        return this.liked;
    }

    public boolean getLiked() {
        return this.liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    // public String getUserName() {
    //     return this.userName;
    // }

    // public void setUserName(String userName) {
    //     this.userName = userName;
    // }

    // public String getUserImg() {
    //     return this.userImg;//this.userShowImg ? this.userImg : "";
    // }

    // public void setUserImg(String userImg) {
    //     this.userImg = userImg;
    // }

    // public boolean getUserShowImg() {
    //     return this.userShowImg;
    // }

    // public void setUserShowImg(boolean userShowImg) {
    //     this.userShowImg = userShowImg;
    // }

    @Override
    public String toString() {
        return "{" +
            " paletteId='" + getPaletteId() + "'" +
            ", userId='" + getUserId() + "'" +
            ", color1='" + getColor1() + "'" +
            ", color2='" + getColor2() + "'" +
            ", color3='" + getColor3() + "'" +
            ", color4='" + getColor4() + "'" +
            ", color5='" + getColor5() + "'" +
            ", posted='" + getPosted() + "'" +
            ", likes='" + getLikes() + "'" +
            ", liked='" + isLiked() + "'" +
            "}";
    }

}
