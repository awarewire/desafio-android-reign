@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.desafio.presentation.hits

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.desafio.R
import com.example.desafio.presentation.fake.FakeData
import com.example.desafio.ui.theme.DesafioTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun HitsScreen(
    modifier: Modifier = Modifier,
    viewModel: HitsViewModel = viewModel()
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
                    items(
                        items = (uiState as MainStateUi.DisplayHits).hist,
                        itemContent = { item ->
                            ItemView(item)
                            Spacer(modifier = Modifier.size(4.dp))
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
fun ItemView(item: HitStateUi) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            Modifier
                .padding(5.dp)
                .fillMaxWidth()
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
        ItemView(item = FakeData.getFakeHitStateUi())
    }
}