package com.mark.moveandswipervitem;

/**
 * Created by mark on 18-8-11.
 */
public class ImageModel {

    private long id;
    private String imagePath;
    private String imageName;
    private String imageTitle;

    public ImageModel() {
    }

    public ImageModel(String imagePath, String imageName, String imageTitle) {
        this.imagePath = imagePath;
        this.imageName = imageName;
        this.imageTitle = imageTitle;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
