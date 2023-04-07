package com.example.dollarschat.ui.theme.uiComponents

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dollarschat.ui.theme.DollarsTheme
import com.example.dollarschat.R
import com.example.dollarschat.ui.theme.Shapes

@Composable
fun Test(packs:ArrayList<String>,avatarEntity:ArrayList<Int>) {
//    LazyColumn() {
//        items(packs.size) {
//            Text(text = packs[it], color = DollarsTheme.color.textColor, style = DollarsTheme.typography.system)
//            LazyRow(modifier = Modifier.fillMaxWidth()) {
//                items(10) {
//                        Image(
//                            painter = painterResource(id = avatarEntity[0]),
//                            contentDescription = "dd", modifier = Modifier
//                                .padding(5.dp)
//                                .size(50.dp)
//                        )
//                    }
//
//            }
            val path = LocalContext.current.filesDir.toString()
            val bitmap  = BitmapFactory.decodeFile(path+"/"+"blueUser.png")





    Image(painter = BitmapPainter(bitmap.asImageBitmap()), contentDescription = "d", colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) }))

        }
//    }
//}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun testPrew(){
    DollarsTheme {
        val packs = ArrayList<String>()
        packs.add("pack1")
        packs.add("pack2")
        val image = ArrayList<Int>()
        image.add(R.drawable.ic_blueuser)
        image.add(R.drawable.ic_blueuser)
        image.add(R.drawable.ic_blueuser)
        image.add(R.drawable.ic_blueuser)


    Test(packs,image)
    }
}