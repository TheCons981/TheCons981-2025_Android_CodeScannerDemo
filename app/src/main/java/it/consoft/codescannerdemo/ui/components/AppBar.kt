package it.consoft.codescannerdemo.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import it.consoft.codescannerdemo.models.enums.CodeGeneratorTypeEnum
import it.consoft.codescannerdemo.ui.navigation.NavItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(navController: NavController) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route

    val title = when (currentDestination) {
        NavItem.codeScanner.screen_route -> NavItem.codeScanner.title
        NavItem.codeRepository.screen_route -> NavItem.codeRepository.title
        NavItem.codeGenerator.screen_route -> NavItem.codeGenerator.title
        NavItem.codeGeneratorForm.screen_route -> when (currentBackStackEntry?.arguments?.getString("type")) {
            CodeGeneratorTypeEnum.TEXT.name -> "Text Generator"
            CodeGeneratorTypeEnum.VCARD.name -> "Contact Generator"
            CodeGeneratorTypeEnum.EMAIL.name -> "Email Generator"
            CodeGeneratorTypeEnum.SMS.name -> "Sms Generator"
            CodeGeneratorTypeEnum.EVENT.name -> "Event Generator"
            CodeGeneratorTypeEnum.PHONE.name -> "Call Generator"
            CodeGeneratorTypeEnum.LOCATION.name -> "Location Generator"
            CodeGeneratorTypeEnum.BARCODE.name -> "Barcode Generator"
            else -> {
                "Code Generator"
            }
        }
        else -> "App"
    }

    TopAppBar(
        title = {
            Text(title)
        }
        //colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme)
    )
}