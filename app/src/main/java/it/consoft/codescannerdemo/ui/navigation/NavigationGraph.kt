package it.consoft.codescannerdemo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import it.consoft.codescannerdemo.models.enums.CodeGeneratorTypeEnum

import it.consoft.codescannerdemo.ui.screens.CodeGeneratorScreen
import it.consoft.codescannerdemo.ui.screens.CodeGeneratorTypeSelectionScreen
import it.consoft.codescannerdemo.ui.screens.CodeRepositoryScreen
import it.consoft.codescannerdemo.ui.screens.CodeScannerScreen
import it.consoft.codescannerdemo.viewModels.CodeGeneratorViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationGraph(navController: NavHostController, modifier: Modifier) {
    val codeGeneratorViewModel: CodeGeneratorViewModel = koinViewModel()

    NavHost(navController, startDestination = NavItem.codeScanner.screen_route) {
        /*composable(
            BottomNavItem.codeScanner.screen_route,
            arguments = listOf(
                navArgument("brand") { defaultValue = "" },
                navArgument("type") { defaultValue = 0 })
        ) {
            backStackEntry ->
            WallpapersScreen(
                wallpapersViewModel = wallpapersViewModel,
                args = backStackEntry.arguments,
                modifier
            )
        }*/

        composable(
            NavItem.codeScanner.screen_route,
        ) { backStackEntry ->
            CodeScannerScreen(modifier, koinViewModel())
        }

        composable(
            NavItem.codeGenerator.screen_route,

        ) { backStackEntry ->

            CodeGeneratorTypeSelectionScreen(
                modifier = modifier,
                navController = navController,
            )
        }

        composable(
            NavItem.codeRepository.screen_route,
        ) { backStackEntry ->
            CodeRepositoryScreen(modifier, koinViewModel())
        }

        composable(
            NavItem.codeGeneratorForm.screen_route,
            arguments = listOf(navArgument("type") { type = NavType.StringType })
        ) { backStackEntry ->

            val type = CodeGeneratorTypeEnum.valueOf(
                backStackEntry.arguments?.getString("type") ?: CodeGeneratorTypeEnum.TEXT.name
            )

            codeGeneratorViewModel.setGeneratedCode(null) //restore generated code data
            codeGeneratorViewModel.setSelectedType(type)

            CodeGeneratorScreen(
                modifier, codeGeneratorViewModel
            )

        }
    }
}