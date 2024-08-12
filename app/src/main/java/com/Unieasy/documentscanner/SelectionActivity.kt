package com.tonyxlh.documentscanner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.tonyxlh.documentscanner.ui.theme.lBlue
import com.tonyxlh.documentscanner.ui.theme.newPurple

class SelectionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
         select()
        }
    }
}

@Composable
fun select() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(newPurple)
            .padding(15.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        var checked by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp), contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_1), contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds,

                    )


            }


        }
        Text(
            text = "Login as:",
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp,
            color = Color.White
        )

        val mContext = LocalContext.current




        Text(
            text = "View Suggested books",
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier
                .clickable {
                    mContext.startActivity(Intent(mContext, MainActivity::class.java))
                }
                .padding(10.dp, 10.dp)
                )



        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = {
                      mContext.startActivity(Intent(mContext,ScannerActivity::class.java))
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(lBlue)
        ) {
            Text(text = "Submit Book")


        }

        Text(
            text = "Or",
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.padding(10.dp, 10.dp)
            )



        Text(
            text = "Click to Login As A buyer",
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier
                .clickable {
                    mContext.startActivity(Intent(mContext, LoginActivity::class.java))

                }
                .padding(10.dp, 10.dp))
            }
        }


@Preview(showBackground = true)
@Composable
fun selectPreview(){
    select()
}