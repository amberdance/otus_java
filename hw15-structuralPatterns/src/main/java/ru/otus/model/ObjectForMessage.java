package ru.otus.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage {
    private List<String> data;

    public ObjectForMessage() {
    }

    ObjectForMessage(List<String> data) {
        this.setData(data);
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }


    public ObjectForMessage copy() {
        if (data != null) {
            return new ObjectForMessage(new ArrayList<>(data));
        }

        return null;
    }
}
