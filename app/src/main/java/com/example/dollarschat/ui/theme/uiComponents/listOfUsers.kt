package com.example.dollarschat.ui.theme.uiComponents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dollarschat.R
import com.example.dollarschat.data.UserProfile
import com.example.dollarschat.ui.theme.DollarsTheme

@Composable
fun ListOfUsers(party:ArrayList<UserProfile>,
                cancel:()->Unit,
                removeUserClick:(ArrayList<UserProfile>)->Unit,){
    val deletedItem = remember {
        mutableStateListOf<UserProfile>()
    }
    val alertDialogState = remember {
        mutableStateOf(false)
    }
  Column(modifier = Modifier
      .fillMaxSize()
      .background(color = DollarsTheme.color.backgroundItem), verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally)
  {
    Column( modifier = Modifier
        .padding(DollarsTheme.shapes.padding)
        .size(450.dp)
        .background(color = DollarsTheme.color.backround)) {
        LazyColumn(){
            itemsIndexed(items = party, itemContent = { _, item ->

                AnimatedVisibility(
                    visible = !deletedItem.contains(item),
                    enter = expandVertically(),
                    exit = shrinkVertically(animationSpec = tween(1000))
                ) {
                    Column() {
                        Row(
                            modifier = Modifier.padding(DollarsTheme.shapes.padding),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_blueuser),
                                contentDescription = "avatar",
                                modifier = Modifier.size(50.dp)
                            )
                            Text(
                                text = item.login.toString(),
                                color = DollarsTheme.color.textColor,
                                style = DollarsTheme.typography.system
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = { deletedItem.add(item) }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_person_remove_24),
                                        contentDescription = "remove",
                                        tint = DollarsTheme.color.menuIconColor
                                    )

                                }
                            }

                        }
                    }
                    Divider(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .align(Alignment.End),
                        thickness = 0.5.dp,
                        color = DollarsTheme.color.textColor.copy(
                            alpha = 0.3f
                        )
                    )

                }
            })

        }

        }
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
          DollarsButton(onClick = when(deletedItem.size){
              0 -> { cancel }
              else -> { { alertDialogState.value = true } }}) {
              Text(text = stringResource(id = R.string.confirm_button),
                  color = DollarsTheme.color.textInButtonColor,
                  style = DollarsTheme.typography.system)

          }
          TextButton(onClick = cancel) {
              Text(text = stringResource(id = R.string.cancel),
                  color = DollarsTheme.color.textInButtonColor,
                  style = DollarsTheme.typography.system)

          }
    }
      if (alertDialogState.value){
          DollarsAlertDialog(text = stringResource(id = R.string.deleted_user_confirm),
              onOkClick = { removeUserClick(ArrayList(deletedItem))
                            alertDialogState.value =false
                          }, onCancelClick = { alertDialogState.value = false } )
      }
  }
}
@Preview
@Composable
fun ListOfUsersPrew(){
    DollarsTheme() {
        val user = UserProfile(login = ":Penis", messageQuantity = 4,roomsCreateQuantity = 5)
        val party = arrayListOf<UserProfile>(user,user.copy(login = "xxx"),user.copy(login = "222"),user.copy(login = "4444"))


        ListOfUsers(party,{},{},)

    }
}