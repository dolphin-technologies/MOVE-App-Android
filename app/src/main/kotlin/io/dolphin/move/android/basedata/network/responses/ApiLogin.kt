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
 * @param sdkUserLoginInfo 
 * @param productAuthInfo 
 * @param consents 
 */


data class ApiLogin (

    @SerializedName("contractId")
    val contractId: kotlin.String? = null,

    @SerializedName("sdkUserLoginInfo")
    val sdkUserLoginInfo: ApiSdkUserLoginInfo? = null,

    @SerializedName("sdkUserAuthCode")
    val sdkUserAuthCode: kotlin.String? = null,

    @SerializedName("productAuthInfo")
    val productAuthInfo: ApiProductAuthInfo? = null,

    @SerializedName("consents")
    val consents: kotlin.collections.List<ApiUserConsent>? = null

)

