
package com.example.android.models.Post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Child {

    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("data")
    @Expose
    private com.example.android.models.Post.Data_ data;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public com.example.android.models.Post.Data_ getData() {
        return data;
    }

    public void setData(com.example.android.models.Post.Data_ data) {
        this.data = data;
    }

}
