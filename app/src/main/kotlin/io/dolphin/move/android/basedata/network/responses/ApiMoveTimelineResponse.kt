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
 * @param status
 * @param id
 * @param serverTs
 * @param serverIsoTime a timestamp as an ISO 8601 date time with an offset
 * @param `data` 
 */


data class ApiMoveTimelineResponse (

    @SerializedName("status")
    val status: ApiStatus? = null,

    @SerializedName("id")
    val id: kotlin.Long? = null,

    @SerializedName("serverTs")
    val serverTs: kotlin.Long? = null,

    /* a timestamp as an ISO 8601 date time with an offset */
    @SerializedName("serverIsoTime")
    val serverIsoTime: java.time.OffsetDateTime? = null,

    @SerializedName("data")
    val `data`: ApiMoveTimelineResponseData? = null

)

