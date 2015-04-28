package com.brunocesar.trackmypack.models;

import java.io.Serializable;

/**
 * Created by BrunoCesar on 15/04/2015.
 */
public class Package  implements Serializable {

    private long id;
    private String name;
    private String code;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
