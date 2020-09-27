package com.example.senpik;

public class LoadImage {
    private String Name,ImageUri;

    public LoadImage() {
    }

    public LoadImage(String name, String imageUri) {
        Name = name;
        ImageUri = imageUri;
    }

    public String getName() {
        return Name;
    }

    public String getImageUri() {
        return ImageUri;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setImageUri(String imageUri) {
        ImageUri = imageUri;
    }
}
