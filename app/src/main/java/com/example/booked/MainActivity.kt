package com.example.booked

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.booked.ui.screens.HomeRoute
import com.example.booked.ui.screens.LoginScreen
import com.example.booked.ui.screens.SplashScreen
import com.example.booked.ui.viewmodels.AppUiState
import com.example.booked.ui.viewmodels.AppViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppRoute()
        }
    }
}

@Composable
fun AppRoute(appViewModel: AppViewModel = hiltViewModel()) {
    val uiState by appViewModel.uiState.collectAsStateWithLifecycle()
    App(uiState = uiState, onSignInClick = {appViewModel.onSignInClick() }, homeContent = { HomeRoute() })
}

@Composable
fun App(uiState: AppUiState, onSignInClick: () -> Unit, homeContent: @Composable () -> Unit) {
    when {
        uiState.showSplash -> SplashScreen()
        uiState.showHome -> homeContent()
        else -> LoginScreen(onSignInClick = onSignInClick)
    }
}
