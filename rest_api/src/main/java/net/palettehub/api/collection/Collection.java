package net.palettehub.api.collection;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import net.palettehub.api.palette.Palette;

/**
 * Collection model.
 * 
 * @author Arthur Lewis
 */
@Entity
public class Collection {
    
    @Id
    @Column(name = "collection_id")
    @JsonProperty("collection_id")
    private String collectionId;

    @Column(name = "user_id")
    @JsonProperty("user_id")
    private String userId;

    @Column(name = "user_name")
    @JsonProperty("user_name")
    private String userName;

    @Column(name = "user_img")
    @JsonProperty("user_img")
    private String userImg;

    @Column(name = "show_picture")
    @JsonProperty(access = Access.WRITE_ONLY)
    private boolean userShowImg;

    @Column
    private String name;

    private List<Palette> palettes;
    private int count;

    public String getCollectionId() {
        return this.collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Palette> getPalettes() {
        return this.palettes;
    }

    public void setPalettes(List<Palette> palettes) {
        this.palettes = palettes;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImg() {
        return this.userShowImg ? this.userImg : "";
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public boolean getUserShowImg() {
        return this.userShowImg;
    }

    public void setUserShowImg(boolean userShowImg) {
        this.userShowImg = userShowImg;
    }

}
