package it.consoft.codescannerdemo.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.QrCode2
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItem(var title:String, var icon:ImageVector, var screen_route:String){

    object codeScanner : NavItem("Code Scanner", Icons.Outlined.QrCodeScanner,"codeScanner")
    object codeGenerator: NavItem("Code Generator", Icons.Outlined.QrCode2,"codeGenerator")
    object codeRepository: NavItem("Code Repository", Icons.Outlined.PhotoLibrary ,"codeRepository")
    object codeGeneratorForm: NavItem("Code Generator", Icons.Outlined.QrCode2 ,"codeGenerator/{type}")

}