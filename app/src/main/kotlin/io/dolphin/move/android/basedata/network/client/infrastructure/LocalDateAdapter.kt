package io.dolphin.move.android.basedata.network.client.infrastructure

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken.NULL
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateAdapter(private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE) : TypeAdapter<LocalDate>() {
    @Throws(IOException::class)
    override fun write(out: JsonWriter?, value: LocalDate?) {
        if (value == null) {
            out?.nullValue()
        } else {
            out?.value(formatter.format(value))
        }
    }

    @Throws(IOException::class)
    override fun read(out: JsonReader?): LocalDate? {
        out ?: return null

        when (out.peek()) {
            NULL -> {
                out.nextNull()
                return null
            }
            else -> {
                return LocalDate.parse(out.nextString(), formatter)
            }
        }
    }
}
