
package com.example.android.models.Post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.example.android.models.Post.Data___;

public class Child_ {

    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("data")
    @Expose
    private Data___ data;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Data___ getData() {
        return data;
    }

    public void setData(Data___ data) {
        this.data = data;
    }

}
