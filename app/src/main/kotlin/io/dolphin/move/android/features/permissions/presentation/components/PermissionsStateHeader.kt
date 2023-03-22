package io.dolphin.move.android.features.permissions.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.components.TextBold
import io.dolphin.move.android.features.permissions.presentation.PermissionsUiHeaderState
import io.dolphin.move.android.ui.theme.*

@ExperimentalPermissionsApi
@Composable
fun PermissionStateHeader(
    permissionsStateHeaderView: PermissionsStateHeaderView,
    permissionsUiHeaderState: PermissionsUiHeaderState,
) {
    PermissionsStateHeaderContent(
        permissionsStateHeaderView = permissionsStateHeaderView,
        permissionsUiHeaderState = permissionsUiHeaderState,
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionsStateHeaderContent(
    permissionsStateHeaderView: PermissionsStateHeaderView,
    permissionsUiHeaderState: PermissionsUiHeaderState,
) {
    Column {
        Spacer(modifier = Modifier.height(1.dp))
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        permissionsUiHeaderState.currentStateBackground
                    )
                )
                .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            TextBold(
                text = stringResource(id = R.string.subtit_current_state),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Start,
                color = white,
                fontSize = 14.sp,
            )
            Card (
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(pale_grey)
                ) {
                    TextBold(
                        text = stringResource(id = permissionsUiHeaderState.currentStateText),
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .weight(1f),
                        textAlign = TextAlign.Start,
                        fontSize = 14.sp,
                    )
                    Switch(
                        modifier = Modifier
                            .padding(end = 12.dp),
                        checked = permissionsUiHeaderState.currentStateChecked,
                        colors = SwitchDefaults.colors(
                            uncheckedTrackColor = white,
                            checkedTrackColor = color_current_state_on_start,
                        ),
                        onCheckedChange = {
                            if (it)
                                permissionsStateHeaderView.turnMoveSdkOn()
                            else
                                permissionsStateHeaderView.turnMoveSdkOff()
                        }
                    )
                }
            }
        }
    }
}

@ExperimentalPermissionsApi
@Preview(showBackground = true)
@Composable
fun PermissionStateHeaderPreview() {
    PermissionsStateHeaderContent(
        permissionsStateHeaderView = PermissionsStateHeaderAdapter,
        permissionsUiHeaderState = PermissionsUiHeaderState(
            currentStateText = R.string.lbl_not_recording,
            currentStateBackground = listOf(
                color_current_state_off_start,
                color_current_state_off_middle,
                color_current_state_off_end
            ),
            currentStateChecked = false
        ),
    )
}

@ExperimentalPermissionsApi
object PermissionsStateHeaderAdapter : PermissionsStateHeaderView {
    override fun turnMoveSdkOn() {}
    override fun turnMoveSdkOff() {}
}
