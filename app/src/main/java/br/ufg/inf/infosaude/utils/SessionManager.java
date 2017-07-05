package br.ufg.inf.infosaude.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences pref;

    SharedPreferences.Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "InfoSaudePref";

    private static final String IS_LOGGED_IN = "IsLoggedIn";

    public static final String KEY_NOME = "nome";

    public static final String KEY_EMAIL = "email";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String name, String email) {
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(KEY_NOME, name);
        editor.putString(KEY_EMAIL, email);

        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_NOME, pref.getString(KEY_NOME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        return user;
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGGED_IN, false);
    }
}