package io.dolphin.move.android.features.messages.presentation.messagedetails

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults.iconButtonColors
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.components.ProgressDialog
import io.dolphin.move.android.basepresentation.components.TextBold
import io.dolphin.move.android.basepresentation.components.TextNormal
import io.dolphin.move.android.basepresentation.components.spacers.HorizontalSpacer8
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer16
import io.dolphin.move.android.ui.theme.MyApplicationTheme
import io.dolphin.move.android.ui.theme.battleship_grey
import io.dolphin.move.android.ui.theme.dark_indigo
import io.dolphin.move.android.ui.theme.light_blue_grey
import io.dolphin.move.android.ui.theme.pale_grey
import io.dolphin.move.android.ui.theme.white

@ExperimentalMaterial3Api
@Composable
fun MessageDetailsScreen(
    viewModel: MessageDetailsViewModel = hiltViewModel(),
    onNoMessages: () -> Unit,
) {

    val viewState by viewModel.messageDetailsState.observeAsState(MessageDetailsViewState())
    val showProgress by viewModel.showProgress.observeAsState(false)
    val event by viewModel.events.collectAsStateWithLifecycle(initialValue = MessageDetailsEvent.None)

    when (event) {
        MessageDetailsEvent.NoMessages -> onNoMessages()
        MessageDetailsEvent.None -> { /* do nothing */
        }
    }

    if (showProgress) ProgressDialog()

    MessageDetailsScreenContent(
        viewState = viewState,
        messageDetailsView = viewModel,
    )
}

@SuppressLint("SetJavaScriptEnabled")
@ExperimentalMaterial3Api
@Composable
fun MessageDetailsScreenContent(
    viewState: MessageDetailsViewState,
    messageDetailsView: MessageDetailsView,
) {
    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = pale_grey)
                .padding(16.dp),
        ) {
            TextNormal(
                text = viewState.date,
                color = battleship_grey,
                fontSize = 14.sp,
            )
            VerticalSpacer16()
            TextBold(
                text = viewState.title,
                color = dark_indigo,
                fontSize = 16.sp,
            )
        }
        AndroidView(
            modifier = Modifier.weight(1f),
            factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                    isVerticalScrollBarEnabled = true
                }
            },
            update = {
                it.loadUrl(viewState.contentUrl)
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = white),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row {
                IconButton(
                    onClick = {
                        messageDetailsView.showMessage(viewState.prevIndex)
                    },
                    colors = iconButtonColors(
                        contentColor = dark_indigo,
                        disabledContentColor = light_blue_grey,
                    ),
                    enabled = viewState.prevIndex != null,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_arrow_back_colored),
                        contentDescription = null,
                    )
                }
                IconButton(
                    onClick = {
                        messageDetailsView.showMessage(viewState.nextIndex)
                    },
                    colors = iconButtonColors(
                        contentColor = dark_indigo,
                        disabledContentColor = light_blue_grey,
                    ),
                    enabled = viewState.nextIndex != null,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_arrow_front_colored),
                        contentDescription = null,
                    )
                }
            }
            IconButton(
                onClick = { messageDetailsView.removeMessage(viewState.id) },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_trash_outline),
                    contentDescription = null,
                )
                HorizontalSpacer8()
            }
        }
    }
}

@Preview(backgroundColor = 0xffffff)
@ExperimentalMaterial3Api
@Composable
fun MessageDetailsScreenContentPreview() {
    MyApplicationTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = white,
        ) {
            MessageDetailsScreenContent(
                viewState = MessageDetailsViewState(
                    date = "June 12th 2021, 16:30",
                    title = "Boost your Travel Insurance!",
                    contentUrl = "google.com",
                    prevIndex = 1,
                    nextIndex = null,
                ),
                messageDetailsView = MessageDetailsViewAdapter,
            )
        }
    }
}

private object MessageDetailsViewAdapter : MessageDetailsView {
    override fun showMessage(index: Int?) {}

    override fun removeMessage(id: Long) {}
}