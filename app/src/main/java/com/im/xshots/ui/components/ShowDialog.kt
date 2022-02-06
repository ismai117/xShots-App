package com.im.xshots.ui.components

import android.Manifest
import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import retrofit2.http.Url




//@Composable
//fun DialogContent() {
//
//    val dialogWidth = 2400.dp
//    val dialogHeight = 180.dp
//
//    Box(
//        modifier = Modifier
//            .width(dialogWidth)
//            .height(dialogHeight)
//            .background(Color.White)
//    ) {
//        Text(
//            text = "Permission Required",
//            style = TextStyle(
//                color = Color.Black,
//                fontSize = 18.sp
//            ),
//            modifier = Modifier
//                .align(Alignment.TopStart)
//                .padding(20.dp)
//        )
//        Text(
//            text = "Permission required to save photo from the Web.",
//            style = TextStyle(
//                color = Color.Black,
//                fontSize = 16.sp
//            ),
//            modifier = Modifier
//                .align(Alignment.TopStart)
//                .padding(start = 20.dp, top = 60.dp)
//        )
//        Row(modifier = Modifier
//            .wrapContentSize()
//            .align(Alignment.BottomEnd)
//            .padding(12.dp)
//        ) {
//            OutlinedButton(
//                onClick = {
//
//                },
//                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
//            ) {
//                Text(
//                    text = "CANCEL",
//                    color = Color.Blue,
//                )
//            }
//            Spacer(modifier = Modifier.padding(8.dp))
//            OutlinedButton(
//                onClick = {
//
//                },
//                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
//            ) {
//                Text(
//                    text = "ALLOW",
//                    color = Color.Blue,
//                )
//            }
//        }
//    }
//}
//
//
//@Preview()
//@Composable
//fun ShowPreview() {
//
//    DialogContent()
//}