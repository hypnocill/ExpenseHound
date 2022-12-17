package com.expensehound.app.ui.screens.purchases

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.expensehound.app.ui.components.NewIncomeScreenAnimated
import com.expensehound.app.ui.components.NewPurchaseScreenAnimated
import com.expensehound.app.ui.navigation.HomeTopAppBar
import com.expensehound.app.ui.viewmodel.MainViewModel
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalPagerApi::class)
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun HomeScreen(viewModel: MainViewModel, navController: NavController) {

    Surface(color = MaterialTheme.colorScheme.background) {
        HomeTopAppBar(viewModel, navController)
    }
}

