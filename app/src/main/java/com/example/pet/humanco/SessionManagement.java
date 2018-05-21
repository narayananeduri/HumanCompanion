package com.example.pet.humanco;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;


/**
 * Created by NARAYANA on 23-02-2018.
 */

class SessionManagement {
    static SessionManagement prefs;
    static final String Shared_Pref_Name = "my saherdpref12";
    static final String userId = "user_id";
    static final String useremail = "uemail";
    private static final String user_loggedin = "t_loggedin";
    private static final String user_mobile = "umoblie";
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public SessionManagement(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(Shared_Pref_Name, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String user_Id, String e_mail, String umoblie, boolean loggedin){
        // Storing login value as TRUE
        editor.putBoolean(user_loggedin, loggedin);
        editor.putString(userId,user_Id);
        // Storing name in pref
        editor.putString(useremail, e_mail);
        // Storing email in pref
        editor.putString(user_mobile, umoblie);
        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, Login.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(userId, pref.getString(userId, null));
        user.put(useremail, pref.getString(useremail, null));


        // user email id
        user.put(user_mobile, pref.getString(user_mobile, null));

        // return user
        return user;
    }
    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }
    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(user_loggedin, false);
    }


}
