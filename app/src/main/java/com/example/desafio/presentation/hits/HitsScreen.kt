@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.desafio.presentation.hits

import DismissBackground
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.desafio.R
import com.example.desafio.presentation.fake.FakeData
import com.example.desafio.ui.theme.DesafioTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HitsScreen(
    modifier: Modifier = Modifier,
    viewModel: HitsViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val uiState: MainStateUi by viewModel.uiState.collectAsState()
    val isRefreshing by viewModel.stateRefresh.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    when (uiState) {
        is MainStateUi.DisplayHits -> {
            SwipeRefresh(state = swipeRefreshState, onRefresh = {
                viewModel.refreshData()
            }) {
                LazyColumn(
                    Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                ) {
                    itemsIndexed(
                        items = (uiState as MainStateUi.DisplayHits).hist,
                        key = { _, item -> item.hashCode() },
                        itemContent = { _, item ->
                            NewsItem(item, navController) { viewModel.removeItem(item.id) }
                        })
                }
            }
        }
        is MainStateUi.Loading -> {
            LoadingView()
        }
        else -> {
            ErrorView(viewModel)
        }
    }
}

@Composable
fun NewsItem(item: HitStateUi, navController: NavHostController, removeItem: (id: String) -> Unit) {
    var show by remember { mutableStateOf(true) }
    val currentItem by rememberUpdatedState(item)
    val dismissState = rememberDismissState(
        confirmValueChange = {
            if (it == DismissValue.DismissedToStart || it == DismissValue.DismissedToEnd) {
                show = false
                removeItem(currentItem.id)
                true
            } else false
        }, positionalThreshold = { 150.dp.toPx() }
    )
    AnimatedVisibility(
        show, exit = fadeOut(spring())
    ) {
        SwipeToDismiss(
            directions = setOf(DismissDirection.StartToEnd),
            state = dismissState,
            modifier = Modifier,
            background = {
                DismissBackground(dismissState)
            },
            dismissContent = {
                ItemView(item) {
                    navController.navigate("details?webpage=${item.retrieveEncodedURL()}")
                }
            }
        )
    }
    Spacer(modifier = Modifier.size(4.dp))
}

@Composable
fun LoadingView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorView(viewModel: HitsViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.text_error)
        )
        Button(onClick = { viewModel.refreshData() }) {
            Text(text = stringResource(id = R.string.text_retry))
        }
    }
}

@Composable
fun ItemView(item: HitStateUi, onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .clickable(enabled = true) {
                    onClick()
                },
        ) {
            Text(
                modifier = Modifier.padding(5.dp),
                text = item.title,
                fontSize = 16.sp
            )
            Text(
                modifier = Modifier.padding(5.dp),
                text = item.author,
                fontSize = 12.sp
            )
        }
        Divider(color = Color.LightGray, thickness = 0.8.dp)
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DesafioTheme {
        //ErrorView(viewModel())
        ItemView(item = FakeData.getFakeHitStateUi(), {})
    }
}