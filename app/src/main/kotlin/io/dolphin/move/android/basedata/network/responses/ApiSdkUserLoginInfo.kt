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
 * @param contractId 
 * @param audience 
 * @param installationId 
 * @param productId 
 * @param jwt 
 * @param refreshToken 
 * @param accessToken 
 */


data class ApiSdkUserLoginInfo (

    @SerializedName("contractId")
    val contractId: kotlin.String? = null,

    @SerializedName("audience")
    val audience: kotlin.String? = null,

    @SerializedName("installationId")
    val installationId: kotlin.String? = null,

    @SerializedName("productId")
    val productId: kotlin.Long? = null,

    @SerializedName("jwt")
    val jwt: kotlin.String? = null,

    @SerializedName("refreshToken")
    val refreshToken: kotlin.String? = null,

    @SerializedName("accessToken")
    val accessToken: kotlin.String? = null

)

