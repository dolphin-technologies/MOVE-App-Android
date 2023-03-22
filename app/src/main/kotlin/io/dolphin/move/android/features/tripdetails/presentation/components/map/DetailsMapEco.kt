package io.dolphin.move.android.features.tripdetails.presentation.components.map

import android.graphics.BitmapFactory
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.scale
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MarkerInfoWindow
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.components.TextNormal
import io.dolphin.move.android.features.tripdetails.presentation.EcoState
import io.dolphin.move.android.features.tripdetails.presentation.MapViewState
import io.dolphin.move.android.ui.theme.orange
import io.dolphin.move.android.ui.theme.watermelon
import io.dolphin.move.android.ui.theme.white

@Composable
@GoogleMapComposable
fun DetailsMapEco(
    mapViewState: MapViewState,
) {
    val context = LocalContext.current
    val heightPx = with(LocalDensity.current) { 31.dp.toPx().toInt() }
    val widthPx = with(LocalDensity.current) { 22.dp.toPx().toInt() }
    val iconModeratePinDescriptor = remember {
        BitmapDescriptorFactory.fromBitmap(
            BitmapFactory.decodeResource(
                context.resources,
                R.drawable.icon_orange_pin,
            ).scale(widthPx, heightPx)
        )
    }
    val iconStrongPinDescriptor = remember {
        BitmapDescriptorFactory.fromBitmap(
            BitmapFactory.decodeResource(
                context.resources,
                R.drawable.icon_red_pin,
            ).scale(widthPx, heightPx)
        )
    }
    mapViewState.ecoMapState.accelerationList.forEach { accState ->
        EcoMarker(
            ecoState = accState,
            titleId = R.string.txt_fast_acceleration,
            iconModeratePinDescriptor = iconModeratePinDescriptor,
            iconStrongPinDescriptor = iconStrongPinDescriptor,
        )
    }
    mapViewState.ecoMapState.brakingList.forEach { brakingState ->
        EcoMarker(
            ecoState = brakingState,
            titleId = R.string.txt_moderate_breaking,
            iconModeratePinDescriptor = iconModeratePinDescriptor,
            iconStrongPinDescriptor = iconStrongPinDescriptor,
        )
    }
    mapViewState.ecoMapState.corneringList.forEach { crnState ->
        EcoMarker(
            ecoState = crnState,
            titleId = R.string.txt_fast_cornering,
            iconModeratePinDescriptor = iconModeratePinDescriptor,
            iconStrongPinDescriptor = iconStrongPinDescriptor,
        )
    }
}

@Composable
@GoogleMapComposable
private fun EcoMarker(
    ecoState: EcoState,
    @StringRes titleId: Int,
    iconModeratePinDescriptor: BitmapDescriptor,
    iconStrongPinDescriptor: BitmapDescriptor,
) {
    MarkerInfoWindow(
        state = ecoState.state,
        icon = if (ecoState.value == 1) {
            iconModeratePinDescriptor
        } else {
            iconStrongPinDescriptor
        },
    ) {
        TextNormal(
            modifier = Modifier
                .background(
                    color = if (ecoState.value == 1) {
                        orange
                    } else {
                        watermelon
                    },
                    shape = RoundedCornerShape(12.dp),
                )
                .border(
                    border = BorderStroke(1.dp, color = white),
                    shape = RoundedCornerShape(12.dp),
                )
                .padding(8.dp),
            text = stringResource(id = titleId),
            fontSize = 9.sp,
            color = white,
        )
    }
}