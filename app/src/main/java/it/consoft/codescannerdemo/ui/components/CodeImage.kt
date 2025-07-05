package it.consoft.codescannerdemo.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import it.consoft.codescannerdemo.models.ScannedCode


@Composable fun CodeDetails(textModifier: Modifier, code: ScannedCode){
    code.image?.let {
        CodeImage(Modifier, it)
    }
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = code.rawValue,
        modifier = textModifier,
        maxLines = 2,
        overflow = TextOverflow.MiddleEllipsis
    )
    Text(
        text = (code.format).description,
        modifier = textModifier
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun CodeImage(modifier: Modifier, image: Bitmap){
    Image(
        bitmap = image.asImageBitmap(),
        contentDescription = "Barcode",
        contentScale = ContentScale.Fit,
        modifier = Modifier
    )
}