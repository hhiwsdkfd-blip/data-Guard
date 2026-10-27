package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.data.local.DataGuardDatabase
import com.example.data.repository.NetworkStatsRepository
import com.example.ui.components.BottomNavBar
import com.example.ui.components.NavDestination
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.AppDetailScreen
import com.example.ui.screens.AppsStatsScreen
import com.example.ui.screens.ChartsReportsScreen
import com.example.ui.screens.DateSelectionScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PermissionsPrivacyScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.DataGuardTheme
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.MainViewModelFactory
import com.example.ui.viewmodel.SubScreen

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels {
        val database = DataGuardDatabase.getInstance(applicationContext)
        val repo = NetworkStatsRepository(
            context = applicationContext,
            networkUsageDao = database.networkUsageDao(),
            appUsageDao = database.appUsageDao()
        )
        MainViewModelFactory(repo, applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsState()

            DataGuardTheme(darkTheme = uiState.isDarkTheme) {
                // Ensure full Right-to-Left (RTL) layout direction across all screens
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    MainAppScaffold(viewModel = viewModel)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkPermissions()
    }
}

@Composable
fun MainAppScaffold(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    // Intercept Back button
    BackHandler(enabled = uiState.activeSubScreen != SubScreen.NONE || uiState.selectedDestination != NavDestination.HOME) {
        viewModel.navigateBack()
    }

    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearUserMessage()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (uiState.activeSubScreen == SubScreen.NONE) {
                BottomNavBar(
                    selectedDestination = uiState.selectedDestination,
                    onDestinationSelect = { dest -> viewModel.selectDestination(dest) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            // Determine active screen
            when (uiState.activeSubScreen) {
                SubScreen.DATE_PICKER -> {
                    DateSelectionScreen(
                        currentPeriod = uiState.selectedPeriod,
                        onBackClick = { viewModel.navigateBack() },
                        onApplyPeriod = { period -> viewModel.applyDateSelection(period) },
                        onApplySpecificDay = { day, month, year ->
                            viewModel.selectSpecificDay(day, month, year)
                        }
                    )
                }

                SubScreen.APP_DETAILS -> {
                    uiState.selectedApp?.let { app ->
                        AppDetailScreen(
                            app = app,
                            dailyPoints = uiState.appDailyPoints,
                            onBackClick = { viewModel.navigateBack() }
                        )
                    } ?: run {
                        viewModel.navigateBack()
                    }
                }

                SubScreen.PERMISSIONS -> {
                    PermissionsPrivacyScreen(
                        hasUsagePermission = uiState.hasUsagePermission,
                        onRequestPermission = { viewModel.requestUsagePermission() },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }

                SubScreen.ABOUT -> {
                    AboutScreen(
                        onBackClick = { viewModel.navigateBack() }
                    )
                }

                SubScreen.NONE -> {
                    when (uiState.selectedDestination) {
                        NavDestination.HOME -> {
                            HomeScreen(
                                uiState = uiState,
                                onOpenDatePicker = { viewModel.openSubScreen(SubScreen.DATE_PICKER) },
                                onAppClick = { app -> viewModel.openAppDetails(app) },
                                onPeriodClick = { periodStr ->
                                    val period = when (periodStr) {
                                        "TODAY" -> com.example.data.model.TimePeriod.TODAY
                                        "WEEK" -> com.example.data.model.TimePeriod.WEEK
                                        else -> com.example.data.model.TimePeriod.MONTH
                                    }
                                    viewModel.applyDateSelection(period)
                                },
                                onNotificationsClick = {
                                    Toast.makeText(context, "لا توجد تنبيهات جديدة", Toast.LENGTH_SHORT).show()
                                },
                                onMenuClick = {
                                    viewModel.openSubScreen(SubScreen.ABOUT)
                                }
                            )
                        }

                        NavDestination.REPORTS -> {
                            ChartsReportsScreen(
                                hourlyPoints = uiState.hourlyPoints,
                                topApps = uiState.apps,
                                selectedTab = uiState.reportsTab,
                                onTabSelected = { tab -> viewModel.setReportsTab(tab) },
                                onExportCsv = { viewModel.exportCsvReport(context) }
                            )
                        }

                        NavDestination.APPS -> {
                            AppsStatsScreen(
                                apps = uiState.filteredApps,
                                searchQuery = uiState.searchQuery,
                                onSearchChange = { q -> viewModel.setSearchQuery(q) },
                                selectedNetworkFilter = uiState.networkFilter,
                                onNetworkFilterChange = { filter -> viewModel.setNetworkFilter(filter) },
                                onAppClick = { app -> viewModel.openAppDetails(app) }
                            )
                        }

                        NavDestination.SETTINGS -> {
                            SettingsScreen(
                                isDarkTheme = uiState.isDarkTheme,
                                onToggleDarkTheme = { viewModel.toggleDarkTheme() },
                                onOpenPermissions = { viewModel.openSubScreen(SubScreen.PERMISSIONS) },
                                onOpenAbout = { viewModel.openSubScreen(SubScreen.ABOUT) }
                            )
                        }
                    }
                }
            }
        }
    }
}
