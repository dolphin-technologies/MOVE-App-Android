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
 * @param password 
 * @param newPassword 
 */


data class ApiChangePasswordRequest (

    @SerializedName("password")
    val password: kotlin.String? = null,

    @SerializedName("newPassword")
    val newPassword: kotlin.String? = null

)
