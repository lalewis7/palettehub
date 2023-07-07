package net.palettehub.api.palette;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "palettes")
public class Palette implements Serializable{
    
    @Id
    @Column(name = "palette_id")
    private String paletteId;

    @Column(name = "user_id")
    private String UserId;

    @Column(name = "color_1")
    private String color1;

    @Column(name = "color_2")
    private String color2;

    @Column(name = "color_3")
    private String color3;

    @Column(name = "color_4")
    private String color4;

    @Column(name = "color_5")
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
        return this.UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
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

    @Override
    public String toString() {
        return "{" +
            " paletteId='" + getPaletteId() + "'" +
            ", UserId='" + getUserId() + "'" +
            ", color1='" + getColor1() + "'" +
            ", color2='" + getColor2() + "'" +
            ", color3='" + getColor3() + "'" +
            ", color4='" + getColor4() + "'" +
            ", color5='" + getColor5() + "'" +
            ", posted='" + getPosted() + "'" +
            "}";
    }

}
