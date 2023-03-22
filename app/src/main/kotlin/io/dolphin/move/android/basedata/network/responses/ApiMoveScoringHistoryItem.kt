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
 * @param id
 * @param label
 * @param color
 * @param score
 */


data class ApiMoveScoringHistoryItem(

    @SerializedName("id")
    val id: kotlin.String? = null,

    @SerializedName("label")
    val label: kotlin.String? = null,

    @SerializedName("color")
    val color: ApiHistoryItemColor? = null,

    @SerializedName("score")
    val score: kotlin.Int? = null

)
