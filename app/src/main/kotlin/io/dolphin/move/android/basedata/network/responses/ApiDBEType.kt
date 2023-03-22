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
 * Values: aCC,cRN,bRK
 */

enum class ApiDBEType(val value: kotlin.String) {

    @SerializedName(value = "ACC")
    aCC("ACC"),

    @SerializedName(value = "CRN")
    cRN("CRN"),

    @SerializedName(value = "BRK")
    bRK("BRK");

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
        fun encode(data: kotlin.Any?): kotlin.String? = if (data is ApiDBEType) "$data" else null

        /**
         * Returns a valid [ApiDBEType] for [data], null otherwise.
         */
        fun decode(data: kotlin.Any?): ApiDBEType? = data?.let {
          val normalizedData = "$it".lowercase()
          values().firstOrNull { value ->
            it == value || normalizedData == "$value".lowercase()
          }
        }
    }
}
