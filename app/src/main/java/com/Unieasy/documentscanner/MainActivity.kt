package com.tonyxlh.documentscanner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.dynamsoft.license.LicenseManager
import com.tonyxlh.documentscanner.ui.theme.DocumentScannerTheme
import com.tonyxlh.documentscanner.ui.theme.lBlue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var documentTimestamps by mutableStateOf(emptyList<Long>())
        setContent {
            DocumentScannerTheme {
                val context = LocalContext.current
                val lifecycleOwner = LocalLifecycleOwner.current
                val manager = DocumentManager(context)
                LaunchedEffect(key1 = true){
                    initLicense(context)
                }
                DisposableEffect(lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_START) {
                            Log.d("DM","on start")
                            documentTimestamps = manager.listDocuments()
                        }
                    }

                    lifecycleOwner.lifecycle.addObserver(observer)

                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            TopAppBar(
                                title = {
                                    Text("Uploaded Book", color = Color.White)
                                },
                                colors = TopAppBarDefaults.mediumTopAppBarColors(lBlue),
                                navigationIcon = {
                                    IconButton(onClick = { /*TODO*/ }) {
                                        Icon(imageVector = Icons.Default.Menu, contentDescription = "", tint = Color.White)
                                    }
                                },
                                actions = {
                                    IconButton(onClick = {
                                        context.startActivity(Intent(context, ScannerActivity::class.java))
                                        Log.d("DBR","clicked");

                                    }) {
                                        Icon(imageVector = Icons.Default.Add, contentDescription = "", tint = Color.White)
                                    }
                                }
                            )
                           Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                               documentTimestamps.forEach { timestamp ->
                                   DocumentItem(timestamp,manager,{
                                       documentTimestamps = manager.listDocuments()
                                   })
                               }
                           }

                        }
                           }
                }
            }
        }
    }

    private fun initLicense(context: Context){
        LicenseManager.initLicense(
            "DLS2eyJoYW5kc2hha2VDb2RlIjoiMjAwMDAxLTE2NDk4Mjk3OTI2MzUiLCJvcmdhbml6YXRpb25JRCI6IjIwMDAwMSIsInNlc3Npb25QYXNzd29yZCI6IndTcGR6Vm05WDJrcEQ5YUoifQ==",
            context
        ) { isSuccess, error ->
            if (!isSuccess) {
                Looper.prepare();
                Toast.makeText(applicationContext,"License invalid: $error",Toast.LENGTH_LONG).show()
                Looper.loop();
                Log.e("DYM", "InitLicense Error: $error")
            }else{
                Log.e("DYM", "InitLicense success")
            }
        }
    }
}

@Composable
fun DocumentItem(date:Long,manager: DocumentManager,onDeleted: (date:Long) -> Unit) {
    val context = LocalContext.current
    var deleteConfirmationAlertDialog by remember {  mutableStateOf(false)}
    Column(modifier = Modifier
        .padding(all = 8.dp)
        .height(300.dp)
        .fillMaxWidth()
        .clickable(onClick = {
            Log.d("DBR", "item clicked");
            var intent = Intent(context, ScannerActivity::class.java)
            intent.putExtra("date", date)
            context.startActivity(intent)
        })) {


        Box(modifier = Modifier
            .fillMaxWidth()
            .height(200.dp), contentAlignment = Alignment.Center){
            Image(
                bitmap = manager.getFirstDocumentImage(date),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()

            )
        }


        Spacer(modifier = Modifier.width(8.dp))


        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(start = 100.dp)) {

            Text(text = "Date and Time", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 20.sp)

            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = formattedDate(date),
                color = MaterialTheme.colorScheme.secondary
            )
        }
        //https://stackoverflow.com/questions/71594277/how-to-set-component-at-end-of-row-in-jetpack-compose
        Spacer(
            Modifier
                .weight(1f)
                .fillMaxHeight())

        Button(
            onClick = {
                deleteConfirmationAlertDialog = true
                Toast.makeText(context,"Deleted book successfully",Toast.LENGTH_SHORT).show()

            },
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(lBlue),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, end = 40.dp)
        ) {
            Text(text = "Delete",
                color = Color.White)
        }

        when {
            deleteConfirmationAlertDialog -> {
                ConfirmationAlertDialog(
                    {
                        deleteConfirmationAlertDialog = false
                    },
                    {
                        deleteConfirmationAlertDialog = false
                        manager.removeDocument(date)
                        Log.d("DM","delete")
                        onDeleted(date)
                    },"Alert","Delete this?")
            }
        }
    }
}

fun formattedDate(timestamp:Long):String{
    var date = Date(timestamp)
    val f1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val s2 = f1.format(date)
    return s2
}
