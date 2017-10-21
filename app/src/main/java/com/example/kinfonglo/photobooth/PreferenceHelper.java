package com.example.kinfonglo.photobooth;


import android.content.Context;
import android.content.SharedPreferences;


public class PreferenceHelper {


    // ***** FUNCTIONS ******

    //  GENERAL GET SHARED PREFERENCE
    public SharedPreferences getSharedPreference(Context ctx, int prefFileKey) {
        SharedPreferences sharedPref = null;

        try {
            sharedPref = ctx.getSharedPreferences(ctx.getString(prefFileKey), Context.MODE_PRIVATE);
        } catch (Exception ex) {

        }

        return sharedPref;
    }

    /*
     DIRECTORY INFO
     */

    public void setPhotoDirPath(Context ctx, String val) {
        SharedPreferences sharedPref = getSharedPreference(ctx, R.string.key_photobooth_sharedpref);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(ctx.getString(R.string.key_photo_dir_path), val);
        editor.commit();
    }

    public String getPhotoDirPath(Context ctx) {
        SharedPreferences sharedPref = getSharedPreference(ctx, R.string.key_photobooth_sharedpref);
        String photoParentDir = sharedPref.getString(ctx.getString(R.string.key_photo_dir_path), null);

        return photoParentDir;
    }


    public void setPhotoParentDir(Context ctx, String val) {
        SharedPreferences sharedPref = getSharedPreference(ctx, R.string.key_photobooth_sharedpref);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(ctx.getString(R.string.key_photo_parent_dir), val);
        editor.commit();
    }

    public String getPhotoParentDir(Context ctx) {
        SharedPreferences sharedPref = getSharedPreference(ctx, R.string.key_photobooth_sharedpref);
        String photoParentDir = sharedPref.getString(ctx.getString(R.string.key_photo_parent_dir), null);

        return photoParentDir;
    }

    /*******
     * SIGN UP PREFERENCE SPECIFIC

     // GET SIGN UP PREFERENCE FILE
     public SharedPreferences getSignUpPreference(Context ctx) {
     SharedPreferences signupPref = getSharedPreference(ctx, R.string.pref_key_signup);
     return signupPref;
     }

     // SET KEY/VAL IN SIGN UP PREFERENCE
     public void setSignUpPreference(Context ctx, String key, String val) {
     SharedPreferences sharedPref = getSignUpPreference(ctx);
     SharedPreferences.Editor editor = sharedPref.edit();
     editor.putString(key, val);
     editor.commit();
     }

     // GET VALUE BY KEY FROM SIGN UP PREFERENCE
     public String getSignUpPreference(Context ctx, String key) {
     SharedPreferences sharedPref = getSignUpPreference(ctx);
     String prefValue = sharedPref.getString(key, null);

     return prefValue;
     }
     ******/

}
