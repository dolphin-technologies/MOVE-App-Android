package io.dolphin.move.android.basepresentation.maps

import com.google.gson.annotations.SerializedName

// You can add all elements if you need
// Check https://developers.google.com/maps/documentation/javascript/style-reference

object FeatureType {
    const val ALL = "all"
    const val POI_ALL = "poi"
}

object ElementType {
    const val ALL = "all"
}

object StylerOption {
    const val VISIBILITY_ON = "on"
    const val VISIBILITY_OFF = "off"
}

data class GoogleMapOption(
    @SerializedName("featureType")
    val featureType: String? = null,
    @SerializedName("elementType")
    val elementType: String? = null,
    @SerializedName("stylers")
    val stylers: List<Stylers>? = null,
)

data class Stylers(
    @SerializedName("visibility")
    val visibility: String? = null,
)
