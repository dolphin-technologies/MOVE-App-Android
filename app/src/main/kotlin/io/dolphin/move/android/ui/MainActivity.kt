package io.dolphin.move.android.ui

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import io.dolphin.move.android.sdk.MoveSdkManager
import io.dolphin.move.android.ui.navigation.MainNavigation
import io.dolphin.move.android.ui.theme.MyApplicationTheme
import io.dolphin.move.android.ui.theme.white
import javax.inject.Inject

@ExperimentalMaterial3Api
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var moveSdkManager: MoveSdkManager
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = white,
                ) {
                    MainNavigation(viewModel)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.reuqestMessages()
    }

    val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            moveSdkManager.getMoveSdk()?.resolveError()
        }
    }
}
