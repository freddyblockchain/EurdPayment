package com.example.eurdpayment.Utils

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp

fun generateQRCode(content: String, size: Int): Bitmap {
    val writer = QRCodeWriter()
    val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size)
    val width = bitMatrix.width
    val height = bitMatrix.height
    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
    for (x in 0 until width) {
        for (y in 0 until height) {
            bmp.setPixel(x, y, if (bitMatrix[x, y]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt())
        }
    }
    return bmp
}

@Composable
fun QRCodeImage(bitmap: Bitmap) {
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = "QR Code",
    )
}