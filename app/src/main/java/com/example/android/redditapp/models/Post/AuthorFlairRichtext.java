
package com.example.android.redditapp.models.Post;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthorFlairRichtext {

    @SerializedName("e")
    @Expose
    private String e;
    @SerializedName("t")
    @Expose
    private String t;

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

}
