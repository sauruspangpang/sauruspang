package com.ksj.sauruspang

import android.Manifest
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
import androidx.compose.runtime.setValue
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

    private val hasPermission = mutableStateOf(false)
    val hasPermissionState: State<Boolean> = hasPermission


    fun updatePermissionState(isGranted: Boolean) {
        _micPermissionGranted.value = isGranted
    }

    fun updateCamPermissionState(isGranted: Boolean){
        hasPermission.value = isGranted
    }
}

@Composable
fun RequestPermissions(permissionViewModel: PermissionViewModel) {
    val context = LocalContext.current

    val camPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    val micPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        camPermission.launch(Manifest.permission.CAMERA)
        if (!it) {
            Toast.makeText(context, "마이크 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        micPermission.launch(Manifest.permission.RECORD_AUDIO)
    }
}
