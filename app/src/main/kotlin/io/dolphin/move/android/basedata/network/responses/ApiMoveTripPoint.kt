/**
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 *
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package io.dolphin.move.android.basedata.network.responses

import com.google.gson.annotations.SerializedName

/**
 *
 *
 * @param isoTime a timestamp as an ISO 8601 date time with an offset
 * @param lat
 * @param lon
 * @param roadLat
 * @param roadLon
 * @param altitude
 * @param speed
 * @param speedLimit
 * @param colour
 * @param wayType
 */


data class ApiMoveTripPoint(

    /* a timestamp as an ISO 8601 date time with an offset */
    @SerializedName("isoTime")
    val isoTime: java.time.OffsetDateTime? = null,

    @SerializedName("lat")
    val lat: kotlin.String? = null,

    @SerializedName("lon")
    val lon: kotlin.String? = null,

    @SerializedName("roadLat")
    val roadLat: kotlin.String? = null,

    @SerializedName("roadlon")
    val roadLon: kotlin.String? = null,

    @SerializedName("altitude")
    val altitude: kotlin.Long? = null,

    @SerializedName("speed")
    val speed: kotlin.Long? = null,

    @SerializedName("speedLimit")
    val speedLimit: kotlin.Long? = null,

    @SerializedName("colour")
    val colour: ApiColour? = null,

    @SerializedName("wayType")
    val wayType: kotlin.String? = null

)
