package com.example.dollarschat.screens.UserProfileScreen.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dollarschat.R
import com.example.dollarschat.data.Avatars
import com.example.dollarschat.data.UserProfile
import com.example.dollarschat.data.localBase.AvatarDatabase.AvatarEntity
import com.example.dollarschat.screens.UserProfileScreen.models.ProfileState
import com.example.dollarschat.ui.theme.DollarsTheme
import com.example.dollarschat.ui.theme.uiComponents.DollarsButton
import com.example.dollarschat.ui.theme.uiComponents.DollarsOutlineTextField
import com.example.dollarschat.ui.theme.uiComponents.DollarsThemeAlertDialog

@Composable
fun ChangeUserProfile(state:ProfileState.ChangeUserProfile,
                      backButtonClick:()->Unit,
                      localUser:UserProfile,
                      newLoginChange:(String)->Unit,
                      profileChangeClick:(Pair<Avatars,String>)->Unit){
    val thisImage = remember { mutableStateOf(localUser.avatar) }
    val alertDialogState = remember{ mutableStateOf(false) }


Column(modifier = Modifier.padding(DollarsTheme.shapes.padding)) {
     Row(modifier = Modifier
         .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
         Image(painter = BitmapPainter(Bitmap.createScaledBitmap(
             BitmapFactory.decodeFile(
                 LocalContext.current.filesDir.toString()+"/"+thisImage.value?.image_name),
             500,
             500,false).asImageBitmap()),
             contentDescription = "thisImage")

     }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(DollarsTheme.shapes.padding),
        horizontalArrangement = Arrangement.Center
    ) {
        DollarsOutlineTextField(
            label = stringResource(id = R.string.user_login),
            value = state.newUserLogin,
            isError = !state.newUserLoginCheck,
            onValueChange = newLoginChange)
    }


    LazyColumn(modifier = Modifier
        .background(color = DollarsTheme.color.backgroundItem)
        .fillMaxWidth()
        .height(270.dp)
        .padding(DollarsTheme.shapes.padding)) {
        items(state.packs.size) {
            Text(text = state.packs[it].first, color = DollarsTheme.color.textColor, style = DollarsTheme.typography.system)
            val pack = state.packs[it]
            LazyRow() {
                items(state.avatars.size) {
                    if (state.avatars[it].iventPackageName == pack.first) {
                        Image(
                            painter = BitmapPainter(
                                BitmapFactory.decodeFile(LocalContext.current.filesDir.toString() + "/" + state.avatars[it].image_name)
                                    .asImageBitmap()),
                            contentDescription = "dd",
                            modifier = Modifier
                                .size(100.dp)
                                .padding(5.dp)
                                .clickable { when(pack.second){
                                    false ->{alertDialogState.value = true}
                                    true->{thisImage.value = state.avatars[it].toAvatars()}
                                } },
                            colorFilter = when(pack.second){
                                false -> ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
                                true -> null
                            }
                        )
                    }
                }
            }

        }
    }
    if (alertDialogState.value){
        DollarsThemeAlertDialog(text = stringResource(id = R.string.acces_failed_message), onClick = {alertDialogState.value=false})
    }
    
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(DollarsTheme.shapes.padding),
        horizontalArrangement = Arrangement.Center) {
        DollarsButton(onClick = { profileChangeClick(Pair(thisImage.value!!,
            when(state.newUserLoginCheck){
                true -> state.newUserLogin
                false -> ""
            })) },
            content = { Text(text = stringResource(id = R.string.change_user_profile),
                color = DollarsTheme.color.textInButtonColor,
                style = DollarsTheme.typography.system) })

    }


}
   // navController.saveState()


        BackHandler(onBack = backButtonClick)



}