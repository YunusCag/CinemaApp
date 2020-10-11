package com.yunuscagliyan.sinemalog.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPref @Inject constructor(@ApplicationContext context: Context) {

    private lateinit var mSharedPreferences: SharedPreferences

    companion object {
        const val SHARED_PREFERENCE_FILE_NAME = "sinemalog"
        const val THEME_STATE_KEY = "DARK_MODE"
        const val FIRST_TIME_OPENING = "FIRST_TIME_OPENING"
    }

    init {
        mSharedPreferences = context.getSharedPreferences(
            SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE
        )
    }

    var nightMode: Boolean
        get() {
            return mSharedPreferences.getBoolean(
                THEME_STATE_KEY, true
            )
        }
        set(state: Boolean) {
            val editor = mSharedPreferences.edit()
            editor.putBoolean(THEME_STATE_KEY, state)
            editor.apply()
        }
    var isFirstTime: Boolean
        get() {
            return mSharedPreferences.getBoolean(
                FIRST_TIME_OPENING, true
            )
        }
    set(value:Boolean) {
        val editor=mSharedPreferences.edit()
        editor.putBoolean(FIRST_TIME_OPENING,value)
        editor.apply()
    }
}