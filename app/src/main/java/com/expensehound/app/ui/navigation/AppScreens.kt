package com.expensehound.app.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.outlined.SsidChart
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.ui.graphics.vector.ImageVector
import com.expensehound.app.R

sealed class AppScreens(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object HomeNav : AppScreens("home", R.string.nav_home, Icons.Outlined.AccountBalance)
    object Stats : AppScreens("stats", R.string.stats, Icons.Outlined.SsidChart)
    object Detail : AppScreens("details", R.string.nav_detail, Icons.Filled.Details)
    object FutureDetail : AppScreens("future_details", R.string.nav_detail, Icons.Filled.Details)
}
