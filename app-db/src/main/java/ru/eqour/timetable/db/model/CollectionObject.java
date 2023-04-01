package ru.eqour.timetable.db.model;

import java.util.List;

public class CollectionObject {

    private String type;
    private List<String> collection;

    public CollectionObject() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getCollection() {
        return collection;
    }

    public void setCollection(List<String> collection) {
        this.collection = collection;
    }
}
