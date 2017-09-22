package com.example.ivan.kuharicav4;

import java.util.List;

/**
 * Created by Ivan on 18.9.2017..
 */

public class Recipes {

    private String title;
    private String desc;
    private String image;
    private String username;
    private String prTime;
    private String categories;
    private String ingredients;
    private long numLikes;

    public Recipes(){

    }

    public Recipes(String title, String desc, String image, String username, String prTime, String categories,String ingredients ) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.username = username;
        this.prTime = prTime;
        this.categories = categories;
        this.ingredients = ingredients;
        this.numLikes = numLikes;
    }

    public long getNumLikes() { return numLikes; }

    public void setNumLikes(long numLikes) { this.numLikes = numLikes; }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getPrTime() { return prTime; }

    public void setPrTime(String prTime) { this.prTime = prTime; }

    public String getCategories() { return categories; }

    public void setCategories(String categories) { this.categories = categories; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
