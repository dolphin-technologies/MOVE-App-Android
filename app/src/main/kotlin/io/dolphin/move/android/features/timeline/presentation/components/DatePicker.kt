package io.dolphin.move.android.features.timeline.presentation.components

import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.components.TextNormal
import io.dolphin.move.android.ui.theme.white
import java.time.LocalDate

/**
 * MaterialDatePicker is only available in alpha version of Compose.
 * So here used standard DatePicker view wrapped in Compose Dialog
 */
@Composable
fun DatePicker(
    currentDate: LocalDate = LocalDate.now(),
    onDateSelected: (LocalDate?) -> Unit,
) {
    val (year, onYearChanged) = remember {
        mutableStateOf(currentDate.year)
    }
    val (monthOfYear, onMonthOfYearChanged) = remember {
        mutableStateOf(currentDate.monthValue)
    }
    val (dayOfMonth, onDayOfMonthChanged) = remember {
        mutableStateOf(currentDate.dayOfMonth)
    }
    Dialog(onDismissRequest = {}) {
        Column(
            modifier = Modifier
                .background(
                    color = white,
                    shape = RoundedCornerShape(10.dp),
                )
                .padding(8.dp)
        ) {
            AndroidView(
                factory = {
                    DatePicker(it).apply {
                        maxDate = System.currentTimeMillis()
                        init(
                            currentDate.year,
                            currentDate.monthValue - 1, // The initial month starting from zero.
                            currentDate.dayOfMonth
                        ) { _, year, monthOfYear, dayOfMonth ->
                            onYearChanged(year)
                            onMonthOfYearChanged(monthOfYear + 1) // month in DatePicker starts from zero.
                            onDayOfMonthChanged(dayOfMonth)
                        }
                    }
                }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                TextButton(
                    onClick = { onDateSelected(null) },
                ) {
                    TextNormal(text = stringResource(id = R.string.btn_cancel).uppercase())
                }
                TextButton(
                    onClick = {
                        onDateSelected(
                            LocalDate.of(
                                year,
                                monthOfYear,
                                dayOfMonth,
                            ),
                        )
                    },
                ) {
                    TextNormal(text = stringResource(id = R.string.btn_ok))
                }
            }
        }
    }
}