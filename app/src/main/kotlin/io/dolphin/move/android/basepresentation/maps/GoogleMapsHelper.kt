package io.dolphin.move.android.basepresentation.maps

import com.google.gson.GsonBuilder

private val gson = GsonBuilder().create()

fun getGoogleMapStyleJson(vararg options: GoogleMapOption): String? {
    return gson.toJson(options)
}