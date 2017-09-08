package com.octalsoftaware.archi.models;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by anandj on 4/18/2017.
 */

public class AdvancedQIModal implements Serializable {
    String id;
    String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name.toLowerCase();
    }
}
