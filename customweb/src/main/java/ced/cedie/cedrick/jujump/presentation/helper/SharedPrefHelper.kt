package ced.cedie.cedrick.jujump.presentation.helper

import android.content.Context
import android.content.ContextWrapper

class SharedPrefHelper(context: Context): ContextWrapper(context) {

    val sharedPreferences by lazy {
        getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
    }

    companion object{
        private const val SHARED_PREFS = "sharedPrefs"
        const val APP_FRESH_INSTALLED = "APP_FRESH_INSTALLED"
    }


}