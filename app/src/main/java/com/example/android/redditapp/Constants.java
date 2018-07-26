package com.example.android.redditapp;

public class Constants {

    public static String subRedditBase = "https://www.reddit.com/subreddits/search.json?q=";
    public static String soccerSubReddit = subRedditBase + "soccer";

    public static String someRedditPost = "https://www.reddit.com/r/soccer/comments/91ven3/.json";

    public static String SHARED_PREFERENES_SUBREDDITS_KEY="SUBREDDITS";

    public static String getListOfSubreddits() {
        return listOfSubreddits;
    }

    public static void setListOfSubreddits(String listOfSubreddits) {
        Constants.listOfSubreddits = listOfSubreddits;
    }

    public static String listOfSubreddits;
}
