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
 * @param accessToken 
 * @param refreshToken 
 */


data class ApiProductAuthInfo (

    @SerializedName("accessToken")
    val accessToken: kotlin.String? = null,

    @SerializedName("refreshToken")
    val refreshToken: kotlin.String? = null

)

