package io.dolphin.move.android.features.launch.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.components.TextBold
import io.dolphin.move.android.ui.navigation.StartRouter
import io.dolphin.move.android.ui.theme.black

@Composable
fun LaunchScreen(
    viewModel: LaunchScreenViewModel = hiltViewModel(),
    startRouter: StartRouter
) {
    LaunchedEffect(key1 = true) {
        val isUserLoggedIn = viewModel.getUser() != null
        startRouter.showStartScreen(isUserLoggedIn)
    }
    LaunchScreenContent()
}

@Composable
fun LaunchScreenContent(
) {
    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(black)
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.background),
            contentDescription = "",
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(
                    id = R.drawable.dolphin_logo_rgb_white
                ),
                contentDescription = "dolphin logo",
            )

            TextBold(
                text = "MOVE",
                textAlign = TextAlign.Center,
                fontSize = 60.sp,
                color = Color.White,
            )
        }
    }
}

@Composable
@Preview
fun LaunchScreenPreview() {
    LaunchScreenContent()
}