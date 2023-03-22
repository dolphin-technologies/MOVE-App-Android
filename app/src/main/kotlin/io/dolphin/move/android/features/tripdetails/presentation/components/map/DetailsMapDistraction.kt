package io.dolphin.move.android.features.tripdetails.presentation.components.map

import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.graphics.scale
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import io.dolphin.move.android.R
import io.dolphin.move.android.features.tripdetails.presentation.DistractionItemState
import io.dolphin.move.android.features.tripdetails.presentation.MapViewState
import io.dolphin.move.android.ui.theme.watermelon

@Composable
@GoogleMapComposable
fun DetailsMapDistraction(
    mapViewState: MapViewState,
) {
    val context = LocalContext.current
    val heightPx = with(LocalDensity.current) { 24.dp.toPx().toInt() }
    val widthPx = with(LocalDensity.current) { 20.dp.toPx().toInt() }
    val iconTouchDescriptor = remember {
        BitmapDescriptorFactory.fromBitmap(
            BitmapFactory.decodeResource(
                context.resources,
                R.drawable.icon_touch,
            ).scale(widthPx, heightPx)
        )
    }
    val iconPhoneDescriptor = remember {
        BitmapDescriptorFactory.fromBitmap(
            BitmapFactory.decodeResource(
                context.resources,
                R.drawable.icon_phone,
            ).scale(widthPx, heightPx)
        )
    }
    mapViewState.distractionMapList.forEach { distractionState ->
        if (distractionState.latLngList.isNotEmpty()) {
            Polyline(
                points = distractionState.latLngList,
                color = watermelon,
                width = 10f,
                zIndex = 1f,
            )
        }
        Marker(
            state = distractionState.state,
            icon = when (distractionState.distraction) {
                is DistractionItemState.Phone -> iconPhoneDescriptor
                is DistractionItemState.Touch -> iconTouchDescriptor
            },
        )
    }
}