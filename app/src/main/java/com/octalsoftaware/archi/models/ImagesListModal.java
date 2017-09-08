package com.octalsoftaware.archi.models;

/**
 * Created by anandj on 4/19/2017.
 */

public class ImagesListModal {
    String id;
    String record_id;
    String ImageLocation;
    String ImageSource;
    String user_id;
    String created;
    String url;
    String ImageName;
    String extension;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    public String getImageLocation() {
        return ImageLocation;
    }

    public void setImageLocation(String imageLocation) {
        ImageLocation = imageLocation;
    }

    public String getImageSource() {
        return ImageSource;
    }

    public void setImageSource(String imageSource) {
        ImageSource = imageSource;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
