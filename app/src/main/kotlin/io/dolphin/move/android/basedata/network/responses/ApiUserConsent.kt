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
 * @param type 
 * @param state 
 * @param webUrl 
 * @param docVersion 
 */


data class ApiUserConsent (

    @SerializedName("type")
    val type: kotlin.String? = null,

    @SerializedName("state")
    val state: kotlin.Boolean? = null,

    @SerializedName("webUrl")
    val webUrl: kotlin.String? = null,

    @SerializedName("docVersion")
    val docVersion: kotlin.String? = null

)
