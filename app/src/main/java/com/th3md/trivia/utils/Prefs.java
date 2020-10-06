package com.th3md.trivia.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private static final String MESSGAE_ID ="ccbcd2d0cc99b5597b1b53788f5c4d2d5c9a9415"; // date -u +%V$(uname)|sha1sum|sed 's/\W//g'
    private SharedPreferences pref;

    public Prefs(Activity activity) {
        this.pref = activity.getSharedPreferences(MESSGAE_ID,Context.MODE_PRIVATE);
    }

    public void saveHighScore(int score) {
        int lastScore = pref.getInt("score", 0);

        if (score > lastScore) {
            pref.edit().putInt("score", score).apply();
        }

    }
    public int getHighScore() {
        return pref.getInt("score", 0);
    }

    public  void setState(int index) {
        pref.edit().putInt("question", index).apply();
    }

    public int getState() {
        return pref.getInt("question", 0);
    }
}
