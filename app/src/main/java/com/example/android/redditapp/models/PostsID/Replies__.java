
package com.example.android.redditapp.models.PostsID;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Replies__ {

    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("data")
    @Expose
    private Data______ data;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Data______ getData() {
        return data;
    }

    public void setData(Data______ data) {
        this.data = data;
    }

}
