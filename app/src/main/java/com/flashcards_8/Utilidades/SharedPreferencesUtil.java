package com.flashcards_8.Utilidades;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.util.HashSet;
import java.util.Set;

public class SharedPreferencesUtil {

    private static final String ELIMINATED_WORDS_KEY = "eliminated_words";

    public static void addEliminatedWord(Context context, String word) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> eliminatedWords = prefs.getStringSet(ELIMINATED_WORDS_KEY, new HashSet<String>());
        eliminatedWords.add(word);
        prefs.edit().putStringSet(ELIMINATED_WORDS_KEY, eliminatedWords).apply();
    }

    public static Set<String> getEliminatedWords(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getStringSet(ELIMINATED_WORDS_KEY, new HashSet<String>());
    }

    public static void removeEliminatedWord(Context context, String word) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> eliminatedWords = prefs.getStringSet(ELIMINATED_WORDS_KEY, new HashSet<String>());
        eliminatedWords.remove(word);
        prefs.edit().putStringSet(ELIMINATED_WORDS_KEY, eliminatedWords).apply();
    }
}
