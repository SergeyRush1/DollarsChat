package com.example.dollarschat.ui.theme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp


data class DollarsColor(val blueUser: Color,
                        val greenUser: Color,
                        val blackUser:Color,
                        val redUser:Color,
                        val yellowUser:Color,
                        val mainColor: Color,
                        val textMessage:Color,
                        val backround: Color,
                        val buttonColor: Color,
                        val editTextBack: Color,
                        val textMessegeColor: Color,
                        val errorColor: Color,
                        val textColor: Color,
                        val cursorColor: Color,
                        val textInButtonColor: Color,
                        val focusColor: Color,
                        val unFocusColor: Color,
                        val tintColor: Color,
                        val backgroundItem:Color,
                        val menuIconColor:Color,
                        val navigationElementColor:Color



)
data class DollarsTypography(val heading: TextStyle,
                             val body:TextStyle,
                             val toolbar:TextStyle,
                             val caption:TextStyle,
                             val system:TextStyle)

data class DollarsImage(val backgroundImage:Int)

data class DollarsShape(val padding: Dp,
                        val cornerShape:Shape)

object DollarsTheme{
    val color:DollarsColor
    @Composable
    get()= localDollarsColor.current


    val typography:DollarsTypography
    @Composable
    get() = localDollarsTypography.current

    val shapes:DollarsShape
    @Composable
    get() = localDollarsShape.current

    val image:DollarsImage
    @Composable
    get() = localDollarsImage.current
}
val localDollarsColor = staticCompositionLocalOf<DollarsColor> {
    error("No colors provided")
}
val localDollarsImage = staticCompositionLocalOf<DollarsImage> {
    error("No image provider")
}

val localDollarsTypography = staticCompositionLocalOf<DollarsTypography> {
    error("No found provided")
}

val localDollarsShape = staticCompositionLocalOf<DollarsShape> {
    error("No shape provided")
}

enum class DollarsSize{
    Small, Medium, Big
}

enum class DollarsCorners{
    Flat,Rounded
}
enum class DollarsStyle{
    Black,Blue,Green,Red,Yellow
}
