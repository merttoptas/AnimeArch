package com.halilozcan.animearch.ui.detail

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.halilozcan.animearch.ui.AnimeDetailUiData
import com.halilozcan.animearch.ui.ScreenState

@Composable
fun DetailScreen(viewModel: DetailViewModel = hiltViewModel()) {
    val uiState by viewModel.screenState.collectAsState(initial = ScreenState.Loading)

    Surface(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (uiState) {
                is ScreenState.Error -> {
                    Toast.makeText(
                        LocalContext.current,
                        (uiState as ScreenState.Error).message,
                        Toast.LENGTH_LONG
                    ).show()
                }
                ScreenState.Loading -> Unit
                is ScreenState.Success -> {
                    SuccessScreen(
                        (uiState as ScreenState.Success).uiData, modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SuccessScreen(uiData: AnimeDetailUiData, modifier: Modifier = Modifier) {
    var isExpanded by remember { mutableStateOf(false) }

    var progress by remember { mutableStateOf(0f) }

    val scrollState = rememberScrollState()

    Column(modifier = modifier) {
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            CircleImage(
                imageUrl = uiData.imageUrl,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            NameSection(
                name = uiData.name,
                kanjiName = uiData.nameKanji,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
        ) {
            Row(modifier = Modifier.padding(16.dp)) {
                Description(
                    description = uiData.description,
                    isExpanded = isExpanded,
                    scrollState = scrollState,
                    modifier = Modifier.weight(1f)
                )
                ExpandProgress(
                    isExpanded = isExpanded,
                    progress = progress,
                    scrollState = scrollState,
                    onExpandClicked = { isExpanded = isExpanded.not() },
                    onProgressChanged = {
                        progress = it
                    }
                )
            }
        }
    }
}