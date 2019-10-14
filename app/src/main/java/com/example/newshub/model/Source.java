package com.example.newshub.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Source {
    @SerializedName("id")
    @Expose
    private Object id;
    @SerializedName("name")
    @Expose
    private String name;

    /**
     * No args constructor for use in serialization
     *
     */
    public Source() {
    }

    /**
     *
     * @param id
     * @param name
     */
    public Source(Object id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Source withId(Object id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Source withName(String name) {
        this.name = name;
        return this;
    }
}
