package io.dolphin.move.android.features.messages.presentation.messages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.components.CommonError
import io.dolphin.move.android.basepresentation.components.ProgressDialog
import io.dolphin.move.android.basepresentation.components.TextBold
import io.dolphin.move.android.basepresentation.components.spacers.HorizontalSpacer16
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer32
import io.dolphin.move.android.features.messages.presentation.MessagesRouter
import io.dolphin.move.android.features.messages.presentation.messages.components.MessageItem
import io.dolphin.move.android.ui.theme.dark_indigo
import io.dolphin.move.android.ui.theme.orangey_red
import io.dolphin.move.android.ui.theme.white

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun MessagesScreen(
    viewModel: MessagesViewModel = hiltViewModel(),
    messagesRouter: MessagesRouter,
) {

    val scrollState = rememberLazyListState()
    val viewState by viewModel.messagesState.observeAsState(MessagesViewState())
    val showProgress by viewModel.showProgress.observeAsState(false)

    if (showProgress) ProgressDialog()

    MessagesScreenContent(
        viewState = viewState,
        isLoading = showProgress,
        scrollState = scrollState,
        onItemClick = { id ->
            viewModel.markAsRead(id)
            messagesRouter.showMessageDetailsScreen(id)
        },
        messagesView = viewModel,
    )
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun MessagesScreenContent(
    viewState: MessagesViewState,
    isLoading: Boolean,
    scrollState: LazyListState,
    onItemClick: (Long) -> Unit,
    messagesView: MessagesView,
) {
    if (viewState.error != null) {
        CommonError(viewState = viewState.error, messagesView::retryOnError)
    }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = scrollState,
        contentPadding = PaddingValues(16.dp),
    ) {
        viewState.messages.forEach { itemState ->
            item {

                val dismissState = rememberDismissState(
                    confirmStateChange = {
                        messagesView.onRemoveItem(itemState.id)
                        true
                    }
                )
                if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                    LaunchedEffect(key1 = Unit) {
                        dismissState.reset()
                    }
                }
                SwipeToDismiss(
                    modifier = Modifier.animateItemPlacement(),
                    state = dismissState,
                    directions = setOf(DismissDirection.EndToStart),
                    dismissThresholds = { FractionalThreshold(0.3f) },
                    background = {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    color = orangey_red,
                                    shape = RoundedCornerShape(10.dp),
                                ),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = CenterVertically,
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_trash_outline),
                                tint = white,
                                contentDescription = null,
                            )
                            HorizontalSpacer16()
                        }
                    },
                    dismissContent = {
                        MessageItem(
                            state = itemState,
                            onItemClick = onItemClick,
                        )
                    },
                )
            }
        }
        if (viewState.messages.isEmpty() && !isLoading) {
            item {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    VerticalSpacer32()
                    TextBold(
                        text = stringResource(id = R.string.tit_no_messages_available_yet),
                        fontSize = 14.sp,
                        color = dark_indigo,
                    )
                }
            }
        }
    }
}

@Preview
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun MessagesScreenContentPreview() {
    MessagesScreenContent(
        viewState = MessagesViewState(
            buildList {
                repeat(3) {
                    add(
                        MessageViewState(
                            id = it.toLong(),
                            url = "",
                            isNew = true,
                            title = "Super, Sie haben Ihren Bonus erhalten!",
                            date = "10 Jun",
                            description = "Wussten Sie, dass Sie Ihre Reise unkompliziert und flexibel direkt von Ihrem Smartphone. Wussten Sie, dass Sie Ihre Reise unkompliziert und flexibel direkt von Ihrem Smartphone."
                        )
                    )
                }
            }
        ),
        isLoading = false,
        scrollState = LazyListState(),
        onItemClick = {},
        messagesView = MessagesViewAdapter,
    )
}

private object MessagesViewAdapter : MessagesView {
    override fun onRemoveItem(id: Long) {}
    override fun markAsRead(id: Long) {}
    override fun retryOnError() {}
}