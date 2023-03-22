package io.dolphin.move.android.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import io.dolphin.move.android.R

sealed class MoveScreens(
    val route: String,
    @StringRes val titleId: Int,
    @StringRes val startScreenTitleId: Int,
    @DrawableRes val iconId: Int,
) {
    object Status : MoveScreens(
        route = Routes.Dashboard.route,
        titleId = R.string.tab_status,
        startScreenTitleId = R.string.tit_move,
        iconId = R.drawable.icon_tab_status,
    )

    object Timeline : MoveScreens(
        route = Routes.TimelineRoot.route,
        titleId = R.string.tab_timeline,
        startScreenTitleId = R.string.tit_timeline,
        iconId = R.drawable.icon_tab_timeline,
    )

    object More : MoveScreens(
        route = Routes.More.route,
        titleId = R.string.tab_more,
        startScreenTitleId = R.string.tit_move,
        iconId = R.drawable.icon_tab_more,
    )
}
