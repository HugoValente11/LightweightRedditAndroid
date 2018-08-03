
package com.example.android.redditapp.models.PostsID;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Replies_ {

    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("data")
    @Expose
    private Data____ data;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Data____ getData() {
        return data;
    }

    public void setData(Data____ data) {
        this.data = data;
    }

}
