package io.dolphin.move.android.features.tripdetails.presentation.components.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberMarkerState
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.components.TextBold
import io.dolphin.move.android.features.tripdetails.presentation.MapViewState
import io.dolphin.move.android.ui.theme.orange
import io.dolphin.move.android.ui.theme.watermelon
import io.dolphin.move.android.ui.theme.white

@Composable
@GoogleMapComposable
fun DetailsMapSpeed(
    mapViewState: MapViewState,
) {
    mapViewState.yellowPolylinePoints.forEach { yellowSpeedState ->
        val markerState = rememberMarkerState()
        if (yellowSpeedState.points.isNotEmpty()) {
            Polyline(
                points = yellowSpeedState.points,
                clickable = true,
                color = orange,
                width = 10f,
                zIndex = 1f,
                onClick = {
                    markerState.showInfoWindow()
                }
            )
            MarkerInfoWindow(
                state = markerState,
            ) {
                TextBold(
                    modifier = Modifier
                        .background(
                            color = watermelon,
                            shape = RoundedCornerShape(10.dp),
                        )
                        .padding(8.dp),
                    text = "${yellowSpeedState.speed} ${stringResource(id = R.string.txt_kmh)}",
                    color = white,
                )
            }
        }
    }
    mapViewState.redPolylinePoints.forEach { redSpeedState ->
        val markerState = rememberMarkerState()
        markerState.position = redSpeedState.middlePoint
        if (redSpeedState.points.isNotEmpty()) {
            Polyline(
                points = redSpeedState.points,
                clickable = true,
                color = watermelon,
                width = 10f,
                zIndex = 1f,
                onClick = {
                    markerState.showInfoWindow()
                }
            )
            MarkerInfoWindow(
                state = markerState,
            ) {
                TextBold(
                    modifier = Modifier
                        .background(
                            color = watermelon,
                            shape = RoundedCornerShape(10.dp),
                        )
                        .padding(8.dp),
                    text = "${redSpeedState.speed} ${stringResource(id = R.string.txt_kmh)}",
                    color = white,
                )
            }
        }
    }
}