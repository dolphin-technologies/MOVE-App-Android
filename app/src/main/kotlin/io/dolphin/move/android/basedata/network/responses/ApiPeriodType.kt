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
 * Values: wEEK,mONTH,yEAR
 */

enum class ApiPeriodType(val value: kotlin.String) {

    @SerializedName(value = "WEEK")
    wEEK("WEEK"),

    @SerializedName(value = "MONTH")
    mONTH("MONTH"),

    @SerializedName(value = "YEAR")
    yEAR("YEAR");

    /**
     * Override toString() to avoid using the enum variable name as the value, and instead use
     * the actual value defined in the API spec file.
     *
     * This solves a problem when the variable name and its value are different, and ensures that
     * the client sends the correct enum values to the server always.
     */
    override fun toString(): String = value

    companion object {
        /**
         * Converts the provided [data] to a [String] on success, null otherwise.
         */
        fun encode(data: kotlin.Any?): kotlin.String? = if (data is ApiPeriodType) "$data" else null

        /**
         * Returns a valid [ApiPeriodType] for [data], null otherwise.
         */
        fun decode(data: kotlin.Any?): ApiPeriodType? = data?.let {
            val normalizedData = "$it".lowercase()
            values().firstOrNull { value ->
                it == value || normalizedData == "$value".lowercase()
            }
        }
    }
}
