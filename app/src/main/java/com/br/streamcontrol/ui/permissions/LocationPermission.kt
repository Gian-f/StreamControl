package com.br.streamcontrol.ui.permissions

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.br.streamcontrol.domain.viewmodel.LocationViewModel
import com.br.streamcontrol.ui.screens.dialogs.ShowErrorSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestPermission(viewModel: LocationViewModel) {
    var isModalSheetVisible by remember { mutableStateOf(false) }
    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val permissionsGranted = permissions.values.reduce { acc, isPermissionGranted ->
                acc && isPermissionGranted
            }
            if (!permissionsGranted) {
                isModalSheetVisible = true
            } else {
                viewModel.requestLocation()
            }
        }
    )
    if (isModalSheetVisible) {
        ShowErrorSheet(
            message = "Permissões negadas podem resultar em funcionalidades indisponíveis.\uD83D\uDE14",
            isVisible = true,
            onDismiss = {
                isModalSheetVisible = false
            }
        )
    }
    DisposableEffect(Unit) {
        locationPermissionLauncher.launch(locationPermissions)
        onDispose { /* Cleanup, if needed */ }
    }
}