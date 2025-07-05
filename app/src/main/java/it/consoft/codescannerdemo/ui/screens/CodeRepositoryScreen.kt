package it.consoft.codescannerdemo.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import it.consoft.codescannerdemo.database.entities.CodeEntity
import it.consoft.codescannerdemo.models.ScannedCode
import it.consoft.codescannerdemo.utils.ImageUtils
import it.consoft.codescannerdemo.viewModels.CodeRepositoryViewModel
import java.io.File

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun CodeRepositoryScreen(modifier: Modifier, viewModel: CodeRepositoryViewModel) {
    val codeRepositoryItems = viewModel.codeRepositoryItems.collectAsLazyPagingItems()

    val isCodeRepositoryEmpty = codeRepositoryItems.itemCount == 0 &&
            codeRepositoryItems.loadState.refresh !is LoadState.Loading

    var showDetailDialog by remember { mutableStateOf(false) }
    var selectedCodeItem by remember { mutableStateOf(null as CodeEntity?) }

    if (showDetailDialog && selectedCodeItem != null) {
        CodeRepositoryDetailScreen(
            onDismissRequest = {
                showDetailDialog = false
                selectedCodeItem = null
            },
            codeEntity = selectedCodeItem!!,
        )
    }

    Scaffold(
        modifier = modifier.padding(0.dp),
        content = { innerPadding ->

            if (!isCodeRepositoryEmpty) {
                LazyColumn(modifier = Modifier) {
                    items(codeRepositoryItems.itemCount) { index ->
                        val codeItem = codeRepositoryItems[index]
                        if (codeItem != null) {
                            CodeRepositoryListItem(codeItem) { item ->
                                showDetailDialog = true
                                selectedCodeItem = item
                            }
                            if (index < codeRepositoryItems.itemCount - 1) {
                                HorizontalDivider(
                                    Modifier,
                                    DividerDefaults.Thickness,
                                    DividerDefaults.color
                                )
                            }
                        }
                    }
                    // Optionally, add a loading indicator at the end
                    codeRepositoryItems.apply {
                        when {
                            loadState.append is LoadState.Loading -> {
                                item { CircularProgressIndicator() }
                            }

                            loadState.append is LoadState.Error -> {
                                item { Text("Load error") }
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "You haven't saved any code yet",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    )
}

@Composable
fun CodeRepositoryListItem(
    codeRepositoryItem: CodeEntity,
    onShowCodeItemDetails: (CodeEntity) -> Unit
) {

    val context = LocalContext.current
    var imageModel: Any = "https://placehold.co/600x400"
    codeRepositoryItem.imageUrl?.let { imageUrl ->
        if (imageUrl.startsWith("http")) {
            imageModel = imageUrl
        } else {
            imageModel = ImageUtils.getFromFilesDir(context, codeRepositoryItem.imageUrl)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onShowCodeItemDetails(codeRepositoryItem)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = imageModel,
            contentDescription = "Room Image",
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = codeRepositoryItem.code,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 2,
            overflow = TextOverflow.MiddleEllipsis
        )
    }
}