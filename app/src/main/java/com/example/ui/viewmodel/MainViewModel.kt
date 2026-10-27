package com.example.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.AppUsageInfo
import com.example.data.model.DailyUsagePoint
import com.example.data.model.DataUsageSummary
import com.example.data.model.DateRangeSelection
import com.example.data.model.HourlyUsagePoint
import com.example.data.model.NetworkType
import com.example.data.model.TimePeriod
import com.example.data.repository.NetworkStatsRepository
import com.example.data.util.DataFormatter
import com.example.data.util.NetworkStatsHelper
import com.example.ui.components.NavDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.util.Calendar

enum class SubScreen {
    NONE,
    DATE_PICKER,
    APP_DETAILS,
    PERMISSIONS,
    ABOUT
}

data class MainUiState(
    val selectedDestination: NavDestination = NavDestination.HOME,
    val activeSubScreen: SubScreen = SubScreen.NONE,
    val selectedApp: AppUsageInfo? = null,
    val selectedPeriod: TimePeriod = TimePeriod.MONTH,
    val dateRange: DateRangeSelection = DateRangeSelection(
        startMillis = System.currentTimeMillis() - 30L * 24 * 3600 * 1000,
        endMillis = System.currentTimeMillis(),
        displayText = "أكتوبر 2024",
        period = TimePeriod.MONTH
    ),
    val usageSummary: DataUsageSummary = DataUsageSummary(),
    val apps: List<AppUsageInfo> = emptyList(),
    val filteredApps: List<AppUsageInfo> = emptyList(),
    val searchQuery: String = "",
    val networkFilter: NetworkType = NetworkType.ALL,
    val reportsTab: String = "يومي",
    val hourlyPoints: List<HourlyUsagePoint> = emptyList(),
    val appDailyPoints: List<DailyUsagePoint> = emptyList(),
    val isDarkTheme: Boolean = true,
    val hasUsagePermission: Boolean = false,
    val isLoading: Boolean = false,
    val userMessage: String? = null
)

class MainViewModel(
    private val repository: NetworkStatsRepository,
    private val appContext: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.initializeDatabaseIfEmpty()

            val permGranted = NetworkStatsHelper.hasUsageStatsPermission(appContext)
            val initialRange = repository.calculatePeriodRange(TimePeriod.MONTH)
            val summary = repository.getUsageSummary(initialRange)
            val apps = repository.getAppUsageList(initialRange)
            val hourly = repository.getHourlyPointsForToday()

            _uiState.update { state ->
                state.copy(
                    hasUsagePermission = permGranted,
                    dateRange = initialRange,
                    usageSummary = summary,
                    apps = apps,
                    filteredApps = apps,
                    hourlyPoints = hourly,
                    isLoading = false
                )
            }
        }
    }

    fun checkPermissions() {
        val permGranted = NetworkStatsHelper.hasUsageStatsPermission(appContext)
        _uiState.update { it.copy(hasUsagePermission = permGranted) }
        if (permGranted) {
            refreshUsageStats()
        }
    }

    fun requestUsagePermission() {
        NetworkStatsHelper.openUsageAccessSettings(appContext)
    }

    fun selectDestination(destination: NavDestination) {
        _uiState.update {
            it.copy(
                selectedDestination = destination,
                activeSubScreen = SubScreen.NONE
            )
        }
    }

    fun openSubScreen(subScreen: SubScreen) {
        _uiState.update { it.copy(activeSubScreen = subScreen) }
    }

    fun openAppDetails(app: AppUsageInfo) {
        val dailyPoints = repository.getDailyPointsForApp(app.packageName)
        _uiState.update {
            it.copy(
                selectedApp = app,
                appDailyPoints = dailyPoints,
                activeSubScreen = SubScreen.APP_DETAILS
            )
        }
    }

    fun navigateBack(): Boolean {
        return if (_uiState.value.activeSubScreen != SubScreen.NONE) {
            _uiState.update { it.copy(activeSubScreen = SubScreen.NONE, selectedApp = null) }
            true
        } else if (_uiState.value.selectedDestination != NavDestination.HOME) {
            _uiState.update { it.copy(selectedDestination = NavDestination.HOME) }
            true
        } else {
            false
        }
    }

    fun applyDateSelection(period: TimePeriod, customStart: Long? = null, customEnd: Long? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val range = repository.calculatePeriodRange(period, customStart, customEnd)
            val summary = repository.getUsageSummary(range)
            val apps = repository.getAppUsageList(range)

            _uiState.update { state ->
                state.copy(
                    selectedPeriod = period,
                    dateRange = range,
                    usageSummary = summary,
                    apps = apps,
                    filteredApps = filterAppsInternal(apps, state.searchQuery, state.networkFilter),
                    activeSubScreen = SubScreen.NONE,
                    isLoading = false
                )
            }
        }
    }

    fun selectSpecificDay(day: Int, month: Int, year: Int) {
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        val startMillis = cal.timeInMillis
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        val endMillis = cal.timeInMillis

        val monthName = DataFormatter.getArabicMonthName(month)
        val displayText = "$day $monthName $year"

        val range = DateRangeSelection(
            startMillis = startMillis,
            endMillis = endMillis,
            displayText = displayText,
            period = TimePeriod.CUSTOM
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val summary = repository.getUsageSummary(range)
            val apps = repository.getAppUsageList(range)

            _uiState.update { state ->
                state.copy(
                    selectedPeriod = TimePeriod.CUSTOM,
                    dateRange = range,
                    usageSummary = summary,
                    apps = apps,
                    filteredApps = filterAppsInternal(apps, state.searchQuery, state.networkFilter),
                    activeSubScreen = SubScreen.NONE,
                    isLoading = false
                )
            }
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { state ->
            state.copy(
                searchQuery = query,
                filteredApps = filterAppsInternal(state.apps, query, state.networkFilter)
            )
        }
    }

    fun setNetworkFilter(filter: NetworkType) {
        _uiState.update { state ->
            state.copy(
                networkFilter = filter,
                filteredApps = filterAppsInternal(state.apps, state.searchQuery, filter)
            )
        }
    }

    private fun filterAppsInternal(
        apps: List<AppUsageInfo>,
        query: String,
        filter: NetworkType
    ): List<AppUsageInfo> {
        return apps.filter { app ->
            val matchesQuery = query.isBlank() ||
                    app.appName.contains(query, ignoreCase = true) ||
                    app.packageName.contains(query, ignoreCase = true)

            val matchesType = when (filter) {
                NetworkType.ALL -> true
                NetworkType.MOBILE -> app.mobileBytes > 0
                NetworkType.WIFI -> app.wifiBytes > 0
            }

            matchesQuery && matchesType
        }
    }

    fun setReportsTab(tab: String) {
        _uiState.update { it.copy(reportsTab = tab) }
    }

    fun toggleDarkTheme() {
        _uiState.update { it.copy(isDarkTheme = !it.isDarkTheme) }
    }

    fun refreshUsageStats() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val summary = repository.getUsageSummary(_uiState.value.dateRange)
            val apps = repository.getAppUsageList(_uiState.value.dateRange)
            _uiState.update { state ->
                state.copy(
                    usageSummary = summary,
                    apps = apps,
                    filteredApps = filterAppsInternal(apps, state.searchQuery, state.networkFilter),
                    isLoading = false
                )
            }
        }
    }

    fun exportCsvReport(context: Context) {
        try {
            val csvContent = DataFormatter.generateCsvReport(
                summary = _uiState.value.usageSummary,
                apps = _uiState.value.apps,
                dateRangeLabel = _uiState.value.dateRange.displayText
            )

            val fileName = "data_guard_report_${System.currentTimeMillis()}.csv"
            val file = File(context.cacheDir, fileName)
            file.writeText(csvContent)

            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "تقرير Data Guard Pro")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(Intent.createChooser(shareIntent, "مشاركة تقرير البيانات").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })

            _uiState.update { it.copy(userMessage = "تم إنشاء التقرير بنجاح") }
        } catch (e: Exception) {
            _uiState.update { it.copy(userMessage = "فشل في تصدير التقرير: ${e.message}") }
        }
    }

    fun clearUserMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }
}

class MainViewModelFactory(
    private val repository: NetworkStatsRepository,
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
