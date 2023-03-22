package io.dolphin.move.android.features.tripdetails.presentation

import androidx.annotation.StringRes
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MarkerState
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.components.ErrorState

data class TripDetailsViewState(
    val tripDetailsHeader: TripDetailsHeaderState? = null,
    val previousTripId: Long? = null,
    val nextTripId: Long? = null,
    val selectedTeb: TripDetailsTabState? = null,
    val mapViewState: MapViewState? = null,
    val error: ErrorState? = null,
)

data class MapViewState(
    val mapProperties: MapProperties,
    val polylinePoints: List<LatLng>,
    val latLngBounds: LatLngBounds,
    val yellowPolylinePoints: List<SpeedViewState> = emptyList(),
    val redPolylinePoints: List<SpeedViewState> = emptyList(),
    val ecoMapState: EcoMapViewState = EcoMapViewState(),
    val distractionMapList: List<DistractionMapViewState> = emptyList(),
)

data class SpeedViewState(
    val speed: Long,
    val middlePoint: LatLng,
    val points: List<LatLng>,
)

data class EcoMapViewState(
    val accelerationList: List<EcoState> = emptyList(),
    val brakingList: List<EcoState> = emptyList(),
    val corneringList: List<EcoState> = emptyList(),
)

data class DistractionMapViewState(
    val distraction: DistractionItemState,
    val state: MarkerState,
    val latLngList: List<LatLng> = emptyList(),
)

data class EcoState(
    val value: Int,
    val state: MarkerState,
)

data class TripDetailsHeaderState(
    val dayOfWeak: String,
    val dayAndMonth: String,
    val tripDuration: TripDuration?,
    val from: String?,
    val to: String?,
    val score: Long?,
)

data class TripDuration(
    val start: String?,
    val end: String?,
    val durationMinutes: Long,
)

sealed interface TripDetailsTabState {
    val tabItem: TripDetailsTabItem

    data class Overview(
        override val tabItem: TripDetailsTabItem = TripDetailsTabItem.OVERVIEW,
        val overviewList: List<OverviewScore> = emptyList()
    ) : TripDetailsTabState

    data class Distraction(
        override val tabItem: TripDetailsTabItem = TripDetailsTabItem.DISTRACTION,
        val score: Long,
        val distractionMinutes: Long = 0,
        val distractionFreeMinutes: Long = 0,
        val distractionItems: List<DistractionItemState> = emptyList(),
    ) : TripDetailsTabState

    data class Safeness(
        override val tabItem: TripDetailsTabItem = TripDetailsTabItem.SAFENESS,
        val score: Long,
        val safenessItems: List<SafenessItem> = emptyList(),
    ) : TripDetailsTabState

    data class Speed(
        override val tabItem: TripDetailsTabItem = TripDetailsTabItem.SPEED,
        val score: Long,
        val withinLimits: String,
        val less10Percents: String,
        val over10Percents: String,
    ) : TripDetailsTabState
}

data class OverviewScore(
    val score: Long,
    @StringRes val title: Int,
)

sealed interface SafenessItem {
    val moderate: Int
    val strong: Int
    val extreme: Int

    data class Acceleration(
        override val moderate: Int,
        override val strong: Int,
        override val extreme: Int,
    ) : SafenessItem

    data class Braking(
        override val moderate: Int,
        override val strong: Int,
        override val extreme: Int,
    ) : SafenessItem

    data class Cornering(
        override val moderate: Int,
        override val strong: Int,
        override val extreme: Int,
    ) : SafenessItem
}

sealed interface DistractionItemState {
    val start: Float
    val end: Float

    data class Touch(
        override val start: Float,
        override val end: Float
    ) : DistractionItemState

    data class Phone(
        override val start: Float,
        override val end: Float
    ) : DistractionItemState
}

enum class TripDetailsTabItem(@StringRes val titleRes: Int) {
    OVERVIEW(R.string.btn_overview),
    DISTRACTION(R.string.btn_distraction),
    SAFENESS(R.string.txt_eco_score),
    SPEED(R.string.btn_speed)
}