
package com.example.android.redditapp.models.PostsID;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Child__ {

    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("data")
    @Expose
    private Data_____ data;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Data_____ getData() {
        return data;
    }

    public void setData(Data_____ data) {
        this.data = data;
    }

}
