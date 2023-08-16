package net.palettehub.api.collection;

import java.util.List;

public class CollectionList {
    
    private List<Collection> collections;
    private int count;

    public CollectionList(){}

    public CollectionList(List<Collection> collections, int count){
        this.collections = collections;
        this.count = count;
    }

    public List<Collection> getCollections() {
        return this.collections;
    }

    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
