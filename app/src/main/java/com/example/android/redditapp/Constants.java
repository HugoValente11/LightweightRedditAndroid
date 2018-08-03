package com.example.android.redditapp;

public class Constants {

    public static String subRedditBase = "https://www.reddit.com/subreddits/search.json?q=";
    public static String limitTo5= "&limit=5";
    public static String soccerSubReddit = subRedditBase + "soccer" + limitTo5;

    public static String searchPostsBase = "https://www.reddit.com/r/";
    public static String jsonExtension = "/.json";
    public static String commentsExtension = "/comments/";

    public static String limitTo10= "?&limit=10";
    public static String someRedditPost = "https://www.reddit.com/r/soccer/comments/91ven3/.json" + limitTo10;

    public static String anotherRedditPost = "https://www.reddit.com/r/funny/comments/92tw8e/i_was_stood_taking_a_photo_of_my_girlfriend_in/.json" + limitTo10;

    public static String someAskRedditPost = "https://www.reddit.com/r/AskReddit/comments/93wbva/.json" + limitTo10;
    public static String anotheranotherRedditPost = "https://www.reddit.com/r/australia/comments/92tnth/congratulations_on_the_career_of_this_aussie/.json" + limitTo10;

    public static String getListOfSubreddits() {
        return listOfSubreddits;
    }

    public static void setListOfSubreddits(String listOfSubreddits) {
        Constants.listOfSubreddits = listOfSubreddits;
    }

    public static String listOfSubreddits;
}
