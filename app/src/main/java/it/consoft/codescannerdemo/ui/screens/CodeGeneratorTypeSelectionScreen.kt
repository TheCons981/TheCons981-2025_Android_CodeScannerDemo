package it.consoft.codescannerdemo.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.consoft.codescannerdemo.models.enums.CodeGeneratorTypeEnum
import it.consoft.codescannerdemo.ui.navigation.NavItem
import it.consoft.codescannerdemo.viewModels.CodeGeneratorViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CodeGeneratorTypeSelectionScreen(
    modifier: Modifier,
    navController: NavController
) {
    val types = CodeGeneratorTypeEnum.entries.toTypedArray()

    val onTypeSelected: (CodeGeneratorTypeEnum) -> (Unit) = { selectedType ->
        navController.navigate("codeGenerator/${selectedType.name}")
    }

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = 128.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(types.size) { index ->
            val type = types[index]
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clickable { onTypeSelected(type) }
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(type.icon, contentDescription = null, modifier = Modifier.size(48.dp))
                    Spacer(Modifier.height(8.dp))
                    Text(type.label)
                }
            }
        }
    }
}