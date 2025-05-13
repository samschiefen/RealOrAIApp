package com.example.realorai.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.realorai.R
import com.example.realorai.data.runModelOnImage
import com.example.realorai.data.uriToBitmap
import com.example.realorai.ui.theme.RealOrAITheme
import com.example.realorai.ui.navigation.NavigationDestination

object TestImageDestination : NavigationDestination {
    override val route = "testImage"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestImagePage(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var resultText by remember { mutableStateOf<String?>(null) }

    // Image Picker Launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri // Store the selected image URI
        uri?.let {u ->
            // Process the image when selected
            val bitmap = uriToBitmap(context, u)
            bitmap?.let {
                resultText = runModelOnImage(context, it)
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            RealOrAiTopAppBar(
                title = stringResource(R.string.app_name),
                canNavigateBack = false,
            )

        }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.Black),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(vertical = 18.dp, horizontal = 8.dp),
                text = stringResource(R.string.test_image_text),
                fontSize = 24.sp,
                lineHeight = 32.sp,
                color = Color.White
            )
            // Display selected image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp), // Fixed space reserved for the image
                contentAlignment = Alignment.Center
            ) {
                imageUri?.let { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Uploaded Image",
                        contentScale = ContentScale.Fit, // Maintain aspect ratio
                        modifier = Modifier
                            .fillMaxWidth() // Now it will properly fill the Box without shrinking layout
                            .clip(RoundedCornerShape(10.dp))
                            .border(2.dp, Color.Gray, RoundedCornerShape(10.dp)),
                    )
                }
            }

            resultText?.let {
                Text(
                    text = "Prediction: $it",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = modifier.fillMaxWidth()
                )
            }
            Button(
                onClick = { imagePickerLauncher.launch("image/*") },
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Box(
                    modifier = Modifier
                        .background(brush = gradientBrush, shape = RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = stringResource(R.string.upload_image_button),
                        color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TestImagePagePreview() {
    RealOrAITheme {
        TestImagePage()
    }
}