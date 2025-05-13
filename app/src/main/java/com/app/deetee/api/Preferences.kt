package com.app.deetee.api

import android.content.Context
import android.content.SharedPreferences

object Preferences {

    private const val APP_PREF = "DeeTee"
    private fun getPrefs(context: Context): SharedPreferences =
        context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE)

    const val KEY_USER_ID = "KEY_USER_ID"
    const val KEY_TOKEN = "TOKEN"

    fun save(context: Context, key: String, value: String) {
        getPrefs(context).edit().putString(key, value).apply()
    }

    fun get(context: Context, key: String): String =
        getPrefs(context).getString(key, "") ?: ""

    fun saveInt(context: Context, key: String, value: Int) {
        getPrefs(context).edit().putInt(key, value).apply()
    }

    fun getInt(context: Context, key: String): Int =
        getPrefs(context).getInt(key, 0)

    fun saveLong(context: Context, key: String, value: Long) {
        getPrefs(context).edit().putLong(key, value).apply()
    }

    fun getLong(context: Context, key: String): Long =
        getPrefs(context).getLong(key, 0)

    fun saveBool(context: Context, key: String, value: Boolean) {
        getPrefs(context).edit().putBoolean(key, value).apply()
    }

    fun getBool(context: Context, key: String): Boolean =
        getPrefs(context).getBoolean(key, false)

    fun clearPreference(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
}