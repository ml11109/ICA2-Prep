package com.example.ica2_prep.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ica2_prep.R

@Composable
fun OnboardingScreen(navController: NavHostController) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val pageModifier = Modifier.padding(32.dp).fillMaxSize()

    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> Page1(pageModifier)
            // Add more pages here, copy the Page1 code for each page
            1 -> LastPage(pageModifier, navController)
        }
    }

    PageIndicator(pagerState)
}

@Composable
fun PageIndicator(pagerState: PagerState) {
    Box(Modifier.fillMaxSize()) {
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(bottom = 64.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(16.dp)
                )
            }
        }
    }
}

@Composable
fun Page1(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painterResource(R.drawable.sample_image), contentDescription = null)
        Spacer(Modifier.size(32.dp))
        Text(
            text = "", // Insert text here
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LastPage(modifier: Modifier, navController: NavHostController) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "", // Insert text here
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.size(24.dp))
        Button(
            onClick = {
                navController.navigate("main") {
                    popUpTo("onboarding") { inclusive = true }
                }
            }
        ) { Text("Continue") }
    }
}