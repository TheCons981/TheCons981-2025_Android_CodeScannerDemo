package it.consoft.codescannerdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import it.consoft.codescannerdemo.ui.components.AppBar
import it.consoft.codescannerdemo.ui.navigation.BottomNavigationScreen
import it.consoft.codescannerdemo.ui.navigation.NavigationGraph
import it.consoft.codescannerdemo.ui.theme.CodeScannerDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CodeScannerDemoTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    //val context = LocalContext.current.applicationContext
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            AppBar(navController = navController)
        },
        bottomBar = { BottomNavigationScreen(navController = navController) },

        ) {
        NavigationGraph(navController = navController, modifier = Modifier.padding(it))
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CodeScannerDemoTheme {
        Greeting("Android")
    }
}