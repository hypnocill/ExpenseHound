package com.expensehound.app.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.LibraryAdd
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.expensehound.app.R
import com.expensehound.app.ui.components.NewIncomeScreenAnimated
import com.expensehound.app.ui.components.NewPurchaseScreenAnimated
import com.expensehound.app.ui.screens.desires.DesiresScreen
import com.expensehound.app.ui.screens.income.IncomeScreen
import com.expensehound.app.ui.screens.purchases.PurchasesScreen
import com.expensehound.app.ui.viewmodel.MainViewModel
import com.expensehound.app.utils.getStartOfMonthAsTimestamp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@Composable
fun HomeTopAppBar(viewModel: MainViewModel, navController: NavController) {
    val pagerState = rememberPagerState(initialPage = 0)
    val shouldHideTabs =
        viewModel.newPurchaseIntent.value || viewModel.newFuturePurchaseIntent.value || viewModel.newIncomeIntent.value

    LaunchedEffect(key1 = pagerState.currentPage) {
        viewModel.setSelectedHomeTopAppBarTab(pagerState.currentPage)
    }

    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {

        NewPurchaseScreenAnimated(
            isVisible = viewModel.newPurchaseIntent.value,
            input = viewModel.newPurchaseInput,
            purchaseIntent = viewModel.newPurchaseIntent
        )
        NewPurchaseScreenAnimated(
            isVisible = viewModel.newFuturePurchaseIntent.value,
            input = viewModel.newFuturePurchaseInput,
            purchaseIntent = viewModel.newFuturePurchaseIntent
        )
        NewIncomeScreenAnimated(
            isVisible = viewModel.newIncomeIntent.value,
            input = viewModel.newIncomeInput,
            incomeIntent = viewModel.newIncomeIntent
        )

        if (!shouldHideTabs) {
            Tabs(pagerState = pagerState)
            TabsContent(
                pagerState = pagerState,
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState) {
    val list = listOf(
        stringResource(id = R.string.nav_purchases) to Icons.Outlined.Checklist,
        stringResource(id = R.string.nav_nava) to Icons.Outlined.BookmarkAdd,
        stringResource(id = R.string.nav_income) to Icons.Outlined.LibraryAdd
    )
    val scope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        contentColor = MaterialTheme.colorScheme.primary,
        backgroundColor = MaterialTheme.colorScheme.background,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                height = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    ) {
        list.forEachIndexed { index, _ ->
            Tab(
                icon = {
                    Icon(imageVector = list[index].second, contentDescription = null)
                },
                text = {
                    Text(
                        list[index].first,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}

@ExperimentalPagerApi
@Composable
fun TabsContent(pagerState: PagerState, viewModel: MainViewModel, navController: NavController) {
    HorizontalPager(state = pagerState, count = 3) { page ->
        when (page) {

            0 -> PurchasesScreen(viewModel) { item, index ->
                navController.navigate("${AppScreens.Detail.route}/${item.uid}")
            }

            1 -> DesiresScreen(viewModel) { item, index ->
                navController.navigate("${AppScreens.FutureDetail.route}/${item.uid}")
            }

            2 -> IncomeScreen(viewModel)
        }
    }
}
