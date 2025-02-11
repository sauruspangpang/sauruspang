package com.ksj.sauruspang

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel


data class Permission(
    var camera: Boolean = false,
    var mic: Boolean = false

)
class PermissionViewModel : ViewModel() {
    private val _micPermissionGranted = mutableStateOf(false)
    val micPermissionGranted: State<Boolean> = _micPermissionGranted

    fun updatePermissionState(isGranted: Boolean) {
        _micPermissionGranted.value = isGranted
    }
}

@Composable
fun RequestMicPermission(viewModel: PermissionViewModel) {
    val context = LocalContext.current
    val micPermissionGranted by viewModel.micPermissionGranted

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        viewModel.updatePermissionState(isGranted)
        if (isGranted) {
            Toast.makeText(context, "마이크 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "마이크 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context, android.Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            launcher.launch(android.Manifest.permission.RECORD_AUDIO)
        } else {
            viewModel.updatePermissionState(true)
        }
    }
}
