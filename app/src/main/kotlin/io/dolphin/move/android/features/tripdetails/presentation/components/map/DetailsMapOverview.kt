package io.dolphin.move.android.features.tripdetails.presentation.components.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.GroundOverlay
import com.google.maps.android.compose.GroundOverlayPosition
import com.google.maps.android.compose.Polyline
import io.dolphin.move.android.R
import io.dolphin.move.android.features.tripdetails.presentation.MapViewState
import io.dolphin.move.android.ui.theme.dusty_teal_two

@Composable
@GoogleMapComposable
fun DetailsMapOverview(
    mapViewState: MapViewState,
) {
    val iconStopDescriptor = remember {
        BitmapDescriptorFactory.fromResource(R.drawable.icon_stop)
    }
    val iconStartDescriptor = remember {
        BitmapDescriptorFactory.fromResource(R.drawable.icon_play)
    }
    Polyline(
        points = mapViewState.polylinePoints,
        color = dusty_teal_two,
        width = 10f,
    )
    mapViewState.polylinePoints.firstOrNull()?.let { latLng ->
        Circle(
            center = latLng,
            radius = 18.0,
            fillColor = dusty_teal_two,
            strokeColor = Color.Transparent,
            strokeWidth = 0.0f,
            zIndex = 1f,
        )
        GroundOverlay(
            position = GroundOverlayPosition.create(latLng, 18f, 18f),
            image = iconStartDescriptor,
            zIndex = 1f,
            anchor = Offset(0.3f, 0.5f),
        )
    }
    mapViewState.polylinePoints.lastOrNull()?.let { latLng ->
        Circle(
            center = latLng,
            radius = 18.0,
            fillColor = dusty_teal_two,
            strokeColor = Color.Transparent,
            strokeWidth = 0.0f,
            zIndex = 1f,
        )
        GroundOverlay(
            position = GroundOverlayPosition.create(latLng, 14f, 14f),
            image = iconStopDescriptor,
            zIndex = 1f,
        )
    }
}