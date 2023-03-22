package io.dolphin.move.android.features.tripdetails.presentation

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MarkerState
import io.dolphin.move.android.R
import io.dolphin.move.android.basedata.network.responses.ApiColour
import io.dolphin.move.android.basedata.network.responses.ApiDBEType
import io.dolphin.move.android.basedata.network.responses.ApiDistractionEventType
import io.dolphin.move.android.basedata.network.responses.ApiMoveDistractionEvent
import io.dolphin.move.android.basedata.network.responses.ApiMoveDrivingEvent
import io.dolphin.move.android.basedata.network.responses.ApiMoveTimelineItemDetail
import io.dolphin.move.android.basedata.network.responses.ApiMoveTripPoint
import io.dolphin.move.android.basepresentation.maps.ElementType
import io.dolphin.move.android.basepresentation.maps.FeatureType
import io.dolphin.move.android.basepresentation.maps.GoogleMapOption
import io.dolphin.move.android.basepresentation.maps.StylerOption
import io.dolphin.move.android.basepresentation.maps.Stylers
import io.dolphin.move.android.basepresentation.maps.getGoogleMapStyleJson
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import timber.log.Timber

private const val HH_MM_FORMAT_PATTERN = "HH:mm"
private const val DAY_OF_WEEK_FORMAT_PATTERN = "EEE"
private const val DAY_AND_MONTH_FORMAT_PATTERN = "dd.MM."
private const val DAY_MONTH_YEAR_FORMAT_PATTERN = "dd.MM.\nyy"
private val timeHHMMFormat = DateTimeFormatter.ofPattern(HH_MM_FORMAT_PATTERN)
private val weekFormat = DateTimeFormatter.ofPattern(DAY_OF_WEEK_FORMAT_PATTERN)
private val dayAndMonthFormat = DateTimeFormatter.ofPattern(DAY_AND_MONTH_FORMAT_PATTERN)
private val dayMonthYearFormat = DateTimeFormatter.ofPattern(DAY_MONTH_YEAR_FORMAT_PATTERN)

fun ApiMoveTimelineItemDetail.mapToTripDetailsHeader(): TripDetailsHeaderState {
    val currentYear = LocalDate.now().year
    val day = weekFormat.format(startTs)
    val dayAndMonth = if (currentYear > (startTs?.year ?: 0)) {
        dayMonthYearFormat.format(startTs)
    } else {
        dayAndMonthFormat.format(startTs)
    }
    return TripDetailsHeaderState(
        dayOfWeak = day,
        dayAndMonth = dayAndMonth,
        tripDuration = TripDuration(
            start = startTs?.format(timeHHMMFormat),
            end = endTs?.format(timeHHMMFormat),
            durationMinutes = durationMinutes ?: 0L,
        ),
        from = startAddress,
        to = endAddress,
        score = scores?.total,
    )
}

fun ApiMoveTimelineItemDetail.mapToSelectedTeb(tabSelected: TripDetailsTabItem): TripDetailsTabState {
    return when (tabSelected) {
        TripDetailsTabItem.OVERVIEW -> getOverviewTabState()
        TripDetailsTabItem.DISTRACTION -> getDistractionTabState()
        TripDetailsTabItem.SAFENESS -> getSafenessTabState()
        TripDetailsTabItem.SPEED -> getSpeedTabState()
    }
}

fun ApiMoveTimelineItemDetail.mapToMapState(): MapViewState {
    val latLngList =
        tripPoints?.map { LatLng(it.roadLat?.toDouble() ?: 0.0, it.roadLon?.toDouble() ?: 0.0) }
            .orEmpty()
    val yellowItemsList = mutableListOf<SpeedViewState>()
    val yellowList = mutableListOf<LatLng>()
    val redItemsList = mutableListOf<SpeedViewState>()
    val redList = mutableListOf<LatLng>()
    var biggestYellowSpeed = 0L
    var biggestRedSpeed = 0L
    tripPoints?.forEachIndexed { index, apiMoveTripPoint ->
        if (index > 0 && apiMoveTripPoint.colour != tripPoints[index - 1].colour) {
            val previousPoint = tripPoints[index - 1]
            if (previousPoint.colour == ApiColour.yELLOW && yellowList.isNotEmpty()) {
                yellowItemsList.add(
                    SpeedViewState(
                        speed = biggestYellowSpeed,
                        middlePoint = yellowList.getMiddlePoint(),
                        points = yellowList.toList(),
                    )
                )
                biggestYellowSpeed = 0L
                yellowList.clear()
            } else if (previousPoint.colour == ApiColour.rED && redList.isNotEmpty()) {
                redItemsList.add(
                    SpeedViewState(
                        speed = biggestRedSpeed,
                        middlePoint = redList.getMiddlePoint(),
                        points = redList.toList(),
                    )
                )
                biggestRedSpeed = 0L
                redList.clear()
            }
        }
        if (apiMoveTripPoint.colour == ApiColour.yELLOW) {
            if (biggestYellowSpeed < (apiMoveTripPoint.speed ?: 0)) biggestYellowSpeed =
                apiMoveTripPoint.speed ?: 0
            if (index > 0 && (tripPoints[index - 1].colour == ApiColour.gREEN || tripPoints[index - 1].colour == ApiColour.rED)) {
                val previousPoint = tripPoints[index - 1]
                yellowList.add(
                    previousPoint.getRoadLatLngPoint()
                )
            }
            yellowList.add(
                apiMoveTripPoint.getRoadLatLngPoint()
            )
        }
        if (apiMoveTripPoint.colour == ApiColour.rED) {
            if (biggestRedSpeed < (apiMoveTripPoint.speed ?: 0)) biggestRedSpeed =
                apiMoveTripPoint.speed ?: 0
            if (index > 0 && (tripPoints[index - 1].colour == ApiColour.gREEN || tripPoints[index - 1].colour == ApiColour.yELLOW)) {
                val previousPoint = tripPoints[index - 1]
                redList.add(
                    previousPoint.getRoadLatLngPoint()
                )
            }
            redList.add(
                apiMoveTripPoint.getRoadLatLngPoint()
            )
        }
    }
    val latLngBoundsBuilder = LatLngBounds.builder()
    latLngList.forEach {
        latLngBoundsBuilder.include(it)
    }
    val accelerationList = mutableListOf<EcoState>()
    val brakingList = mutableListOf<EcoState>()
    val corneringList = mutableListOf<EcoState>()

    drivingEvents?.groupBy { it.type }?.forEach { (type, event) ->
        when (type) {
            ApiDBEType.cRN -> {
                event.forEach { crnEvent ->
                    corneringList.add(
                        crnEvent.getEcoState()
                    )
                }
            }
            ApiDBEType.aCC -> {
                event.forEach { accEvent ->
                    accelerationList.add(
                        accEvent.getEcoState()
                    )
                }
            }
            ApiDBEType.bRK -> {
                event.forEach { brkEvent ->
                    brakingList.add(
                        brkEvent.getEcoState()
                    )
                }
            }
            null -> { /* ignore on null*/
            }
        }
    }
    return MapViewState(
        mapProperties = MapProperties(
            latLngBoundsForCameraTarget = latLngBoundsBuilder.build(),
            mapStyleOptions = getGoogleMapStyleJson(
                GoogleMapOption(
                    featureType = FeatureType.POI_ALL,
                    elementType = ElementType.ALL,
                    stylers = listOf(
                        Stylers(
                            visibility = StylerOption.VISIBILITY_OFF,
                        )
                    ),
                ),
            )?.let { MapStyleOptions(it) },
        ),
        polylinePoints = latLngList,
        latLngBounds = latLngBoundsBuilder.build(),
        yellowPolylinePoints = yellowItemsList,
        redPolylinePoints = redItemsList,
        ecoMapState = EcoMapViewState(
            accelerationList = accelerationList,
            brakingList = brakingList,
            corneringList = corneringList,
        )
    )
}

fun TripDetailsTabState.Distraction.mapToDistractionMapStateList(tripDetails: ApiMoveTimelineItemDetail?): List<DistractionMapViewState> {
    val pointList = tripDetails?.tripPoints ?: return emptyList()
    val distractionMapStateList = mutableListOf<DistractionMapViewState>()
    val pointsListSize = pointList.size - 1
    this.distractionItems.forEach { tabItemState ->
        val pointsSubList = pointList.subList(
            fromIndex = (tabItemState.start * pointsListSize).toInt(),
            toIndex = (tabItemState.end * pointsListSize).toInt(),
        ).map {
            LatLng(
                it.lat?.toDouble() ?: 0.0,
                it.lon?.toDouble() ?: 0.0,
            )
        }
        val singlePoint = pointsSubList.getMiddlePoint()
        distractionMapStateList.add(
            DistractionMapViewState(
                distraction = tabItemState,
                latLngList = pointsSubList,
                state = MarkerState(
                    position = singlePoint,
                ),
            )
        )
    }
    Timber.v(distractionMapStateList.toString())
    return distractionMapStateList
}

private fun List<LatLng>.getMiddlePoint(): LatLng {
    return if (size == 2) {
        // lets calculate an average value between two points
        val first = first()
        val second = last()
        LatLng(
            (first.latitude + second.latitude) / 2,
            (first.longitude + second.longitude) / 2,
        )
    } else {
        // otherwise take middle or just single point
        this[size / 2]
    }
}

private fun ApiMoveDrivingEvent.getEcoState(): EcoState {
    val state = MarkerState(
        position = LatLng(lat ?: 0.0, lon ?: 0.0),
    )
    state.showInfoWindow()
    return EcoState(
        value = value,
        state = state,
    )
}

private fun ApiMoveTripPoint.getRoadLatLngPoint(): LatLng {
    return LatLng(
        roadLat?.toDouble() ?: 0.0,
        roadLon?.toDouble() ?: 0.0,
    )
}

private fun ApiMoveTimelineItemDetail.getOverviewTabState(): TripDetailsTabState {
    return TripDetailsTabState.Overview(
        overviewList = buildList {
            add(
                OverviewScore(
                    score = scores?.total ?: 0,
                    title = R.string.txt_total,
                )
            )
            add(
                OverviewScore(
                    score = scores?.distraction ?: 0,
                    title = R.string.txt_distraction,
                )
            )
            add(
                OverviewScore(
                    score = scores?.safeness ?: 0,
                    title = R.string.txt_eco_score,
                )
            )
            add(
                OverviewScore(
                    score = scores?.speed ?: 0,
                    title = R.string.txt_speed_score,
                )
            )
        }
    )
}

private fun ApiMoveTimelineItemDetail.getDistractionTabState(): TripDetailsTabState {
    val tripStartSeconds = startTs?.toEpochSecond() ?: 0
    val tripEndSeconds = endTs?.toEpochSecond() ?: 0
    return TripDetailsTabState.Distraction(
        score = scores?.distraction ?: 0L,
        distractionMinutes = distractionDetails?.totalDistractedMinutes ?: 0,
        distractionFreeMinutes = distractionDetails?.distractionFreeMinutes ?: 0,
        distractionItems = distractionEvents?.map {
            it.mapToDistractionItemState(
                tripStartSeconds,
                tripEndSeconds,
            )
        }.orEmpty(),
    )
}

private fun ApiMoveDistractionEvent.mapToDistractionItemState(
    tripStartSeconds: Long,
    tripEndSeconds: Long,
): DistractionItemState {
    val tripDuration = tripEndSeconds - tripStartSeconds
    val tripDurationOnePercent = tripDuration / 100f
    val itemStartSeconds = startIsoTime?.toEpochSecond() ?: 0
    val itemEndSeconds = endIsoTime?.toEpochSecond() ?: 0
    val start = (itemStartSeconds - tripStartSeconds) / tripDurationOnePercent // percents
    val end = (itemEndSeconds - tripStartSeconds) / tripDurationOnePercent // percents
    return when (type) {
        ApiDistractionEventType.sWPTYPE -> DistractionItemState.Touch(
            start = start / 100, // representation from 0.0 to 1.0
            end = end / 100,
        )
        ApiDistractionEventType.pHHHELD, null -> DistractionItemState.Phone(
            start = start / 100,
            end = end / 100,
        )
    }
}

private fun ApiMoveTimelineItemDetail.getSafenessTabState(): TripDetailsTabState {
    val safenessMap = drivingEvents?.groupBy { it.type }.orEmpty()
    return TripDetailsTabState.Safeness(
        score = scores?.safeness ?: 0L,
        safenessItems = buildList {
            val accelerations = safenessMap[ApiDBEType.aCC].orEmpty()
            add(
                SafenessItem.Acceleration(
                    moderate = accelerations.count { it.value == 1 },
                    strong = accelerations.count { it.value == 2 },
                    extreme = accelerations.count { it.value == 3 },
                )
            )
            val braking = safenessMap[ApiDBEType.bRK].orEmpty()
            add(
                SafenessItem.Braking(
                    moderate = braking.count { it.value == 1 },
                    strong = braking.count { it.value == 2 },
                    extreme = braking.count { it.value == 3 },
                )
            )
            val cornering = safenessMap[ApiDBEType.cRN].orEmpty()
            add(
                SafenessItem.Cornering(
                    moderate = cornering.count { it.value == 1 },
                    strong = cornering.count { it.value == 2 },
                    extreme = cornering.count { it.value == 3 },
                )
            )
        }
    )
}

private fun ApiMoveTimelineItemDetail.getSpeedTabState(): TripDetailsTabState {
    val greenKm = sectionDistance?.green?.div(1000f) ?: 0f
    val yellowKm = sectionDistance?.yellow?.div(1000f) ?: 0f
    val redKm = sectionDistance?.red?.div(1000f) ?: 0f
    val twoDigitsFormat = "%.1f"

    return TripDetailsTabState.Speed(
        score = scores?.speed ?: 0L,
        withinLimits = twoDigitsFormat.format(greenKm),
        less10Percents = twoDigitsFormat.format(yellowKm),
        over10Percents = twoDigitsFormat.format(redKm),
    )
}