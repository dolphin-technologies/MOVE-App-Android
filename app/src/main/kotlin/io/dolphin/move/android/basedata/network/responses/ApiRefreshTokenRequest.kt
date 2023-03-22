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
 * @param timestamp
 * @param appId
 * @param clientVersion
 * @param platform
 * @param language
 * @param isoTime a timestamp as an ISO 8601 date time with an offset
 * @param contractId
 * @param productId
 * @param uuid
 * @param refreshToken 
 */


data class ApiRefreshTokenRequest (

    @SerializedName("timestamp")
    val timestamp: kotlin.Long,

    @SerializedName("appId")
    val appId: kotlin.String? = null,

    @SerializedName("clientVersion")
    val clientVersion: kotlin.String? = null,

    @SerializedName("platform")
    val platform: kotlin.String? = null,

    @SerializedName("language")
    val language: kotlin.String? = null,

    /* a timestamp as an ISO 8601 date time with an offset */
    @SerializedName("isoTime")
    val isoTime: java.time.OffsetDateTime? = null,

    @SerializedName("contractId")
    val contractId: kotlin.String? = null,

    @SerializedName("productId")
    val productId: kotlin.Long? = null,

    @SerializedName("uuid")
    val uuid: kotlin.String? = null,

    @SerializedName("refreshToken")
    val refreshToken: kotlin.String? = null

)

