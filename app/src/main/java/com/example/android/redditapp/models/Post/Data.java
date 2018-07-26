
package com.example.android.redditapp.models.Post;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("dist")
    @Expose
    private Object dist;
    @SerializedName("children")
    @Expose
    private List<Child> children = null;


    public Object getDist() {
        return dist;
    }

    public void setDist(Object dist) {
        this.dist = dist;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }


}
