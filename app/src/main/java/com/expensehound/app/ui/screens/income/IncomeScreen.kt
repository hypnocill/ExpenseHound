package com.expensehound.app.ui.screens.income

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.expensehound.app.R
import com.expensehound.app.data.entity.Income
import com.expensehound.app.ui.components.AppFilterChip
import com.expensehound.app.ui.components.AppItemCard
import com.expensehound.app.ui.components.EmptyListText
import com.expensehound.app.ui.components.NewIncomeScreenAnimated
import com.expensehound.app.ui.theme.margin_half
import com.expensehound.app.ui.viewmodel.MainViewModel
import com.expensehound.app.utils.getStartOfMonthAsTimestamp
import com.expensehound.app.utils.loadIncomeInputInState

@Composable
fun IncomeScreen(viewModel: MainViewModel, onItemClicked: (Income, Int) -> Unit) {
    var list = remember { mutableStateListOf<Income>() }

    LaunchedEffect(key1 = viewModel.incomeFiltersMonth.value) {
        var from: Long? = null

        if (viewModel.incomeFiltersMonth.value) {
            from = getStartOfMonthAsTimestamp()
        }

        viewModel.getAllIncome(list, from)
    }

    Surface(color = MaterialTheme.colorScheme.background) {
        Column {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                AppFilterChip(
                    stringResource(id = R.string.filters_current_month),
                    viewModel.incomeFiltersMonth.value
                ) {
                    viewModel.setIncomeFilterMonth(!viewModel.incomeFiltersMonth.value)
                }
            }

            if (list.isEmpty()) {
                EmptyListText()
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(top = margin_half)
            ) {
                itemsIndexed(items = list) { index, item ->
                    AppItemCard(
                        title = item.name,
                        subtitle = item.comment,
                        onClick = { /*TODO*/ },
                        onEdit = {
                            viewModel.newIncomeIntent.value = true
                            loadIncomeInputInState(viewModel.newIncomeInput, item)
                        },
                        onDelete = { viewModel.deleteIncome(item.uid) },
                        isCreatedAutomatically = item.createdAutomatically
                    )
                }
            }
        }

        NewIncomeScreenAnimated(
            isVisible = viewModel.newIncomeIntent.value,
            input = viewModel.newIncomeInput,
            incomeIntent = viewModel.newIncomeIntent
        )
    }

}
