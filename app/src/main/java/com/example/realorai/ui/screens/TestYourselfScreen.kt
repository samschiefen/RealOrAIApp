package com.example.realorai.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.realorai.R
import com.example.realorai.ui.navigation.NavigationDestination
import com.example.realorai.ui.theme.RealOrAITheme

object TestSelfDestination : NavigationDestination {
    override val route = "testSelf"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestYourselfPage(takeTest: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        topBar = {
            RealOrAiTopAppBar(
                title = stringResource(R.string.app_name),
                canNavigateBack = false,
            )

        }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color.Black),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(vertical = 18.dp, horizontal = 8.dp),
                text = stringResource(R.string.test_self_text),
                fontSize = 24.sp,
                lineHeight = 32.sp,
                color = Color.White
            )
            Button(
                onClick = takeTest,
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
                    Text(text = stringResource(R.string.start_test_button),
                        color = Color.White)
                }
            }
        }
    }
}

@Composable
fun QuestionScreen(
    imageList: List<Pair<String, Boolean>>,
    onFinish: (correct: Int, total: Int) -> Unit
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var correctAnswers by remember { mutableIntStateOf(0) }

    if (currentIndex >= imageList.size) {
        onFinish(correctAnswers, imageList.size)
        return
    }

    val (imagePath, isReal) = imageList[currentIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Show current question number out of total
        Text(
            text = "Question ${currentIndex + 1} of ${imageList.size}",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 36.dp),
            color = Color.White
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
            ) {
                LoadImageFromAssets(imagePath)
            }

            Row(modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        if (!isReal) correctAnswers++
                        currentIndex++
                    },
                    modifier = Modifier
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .background(brush = gradientBrush, shape = RoundedCornerShape(12.dp))
                            .padding(horizontal = 24.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Fake",
                            color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        if (!isReal) correctAnswers++
                        currentIndex++
                    },
                    modifier = Modifier
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .background(brush = gradientBrush, shape = RoundedCornerShape(12.dp))
                            .padding(horizontal = 24.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Real",
                            color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun LoadImageFromAssets(imagePath: String) {
    val context = LocalContext.current
    val assetManager = context.assets
    val bitmap = remember(imagePath) {
        assetManager.open(imagePath).use { BitmapFactory.decodeStream(it) }
    }

    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = "Test Image",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun ResultScreen(goHome: () -> Unit, correctAnswers: Int, totalQuestions: Int) {
    val percentage = (correctAnswers.toFloat() / totalQuestions * 100).toInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Test Completed!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = "You got $correctAnswers out of $totalQuestions correct!",
            fontSize = 24.sp,
            color = Color.White
        )
        Text(
            text = "Score: $percentage%",
            fontSize = 24.sp,
            color = if (percentage >= 50) Color(0xFF1D9103) else Color(0xFFAF1D04)
        )
        Text(
            text = "The current people average is 79%!",
            fontSize = 24.sp,
            color = Color(0xFFE78206),
        )
        Text(
            text = "The current model score is 74%!",
            fontSize = 24.sp,
            color = Color(0xFF06C5E7),
        )
        Button(
            onClick = goHome,
            modifier = Modifier
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )
        ) {
            Box(
                modifier = Modifier
                    .background(brush = gradientBrush, shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Go Back",
                    color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TestYourselfPagePreview() {
    RealOrAITheme {
        TestYourselfPage({})
    }
}