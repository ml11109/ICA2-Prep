package com.example.ica2_prep.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ica2_prep.data.AppViewModel
import com.example.ica2_prep.ui.theme.ICA2_PrepTheme
import kotlin.math.round

/*
Contents:
- CollapsingToolbarScaffold and ToolbarScaffold
- TabScreen
- PagerScreen
- InputDialog
- RatingBar
 */


/*
// Scaffold with collapsing toolbar
CollapsingToolbarScaffold("Title") { nestedScrollConnection ->
    Column(Modifier.nestedScroll(nestedScrollConnection) {
        // Content
    }
}

// For back button, showBackButton = true and pass in navController, eg:
CollapsingToolbarScaffold("Title", showBackButton = true, navController = navController)

// For dropdown menu, showDropdownMenu = true and pass in dropdownMenuItems, eg:
@Composable
fun DropdownMenuItems() {
    DropdownMenuItem(text = { Text("Item 1") }, onClick = { /* Handle click */ })
    DropdownMenuItem(text = { Text("Item 2") }, onClick = { /* Handle click */ })
}
CollapsingToolbarScaffold("Title", showDropdownMenu = true, dropdownMenuItems = { DropdownMenuItems() })

// Can also set showAppBar to false, pass in floatingActionButton, or pass in other menuItems
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingToolbarScaffold(
    title: String = "",
    showAppBar: Boolean = true,
    showBackButton: Boolean = false,
    navController: NavController? = null,
    showDropdownMenu: Boolean = false,
    menuItems: @Composable () -> Unit = {},
    dropdownMenuItems: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (nestedScrollConnection: NestedScrollConnection) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    var menuExpanded by remember { mutableStateOf(false) }

    ICA2_PrepTheme {
        Scaffold(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),

            floatingActionButton = floatingActionButton,

            topBar = {
                if (!showAppBar) return@Scaffold

                TopAppBar(
                    title = {
                        val modifier = if (showBackButton) Modifier else Modifier.padding(start = 8.dp)
                        Text(title, color = MaterialTheme.colorScheme.onPrimary, modifier = modifier)
                    },

                    navigationIcon = {
                        if (!showBackButton) return@TopAppBar

                        IconButton(onClick = {
                            navController?.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },

                    actions = {
                        if (showDropdownMenu) {
                            Row {
                                menuItems()

                                IconButton(onClick = { menuExpanded = true }) {
                                    Icon(Icons.Default.MoreVert, "More", tint = MaterialTheme.colorScheme.onPrimary)
                                }

                                DropdownMenu(
                                    expanded = menuExpanded,
                                    onDismissRequest = { menuExpanded = false }
                                ) {
                                    dropdownMenuItems()
                                }
                            }
                        }
                    },

                    colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary),

                    scrollBehavior = scrollBehavior
                )
            }
        ) { paddingValues ->
            Box(Modifier.padding(paddingValues)) {
                content(scrollBehavior.nestedScrollConnection)
            }
        }
    }
}

/*
// Scaffold with toolbar
ToolbarScaffold("Title") {
    // Content
}

// For back button, showBackButton = true and pass in navController, eg:
ToolbarScaffold("Title", showBackButton = true, navController = navController)

// For dropdown menu, showDropdownMenu = true and pass in dropdownMenuItems, eg:
@Composable
fun DropdownMenuItems() {
    DropdownMenuItem(text = { Text("Item 1") }, onClick = { /* Handle click */ })
    DropdownMenuItem(text = { Text("Item 2") }, onClick = { /* Handle click */ })
}
ToolbarScaffold("Title", showDropdownMenu = true, dropdownMenuItems = { DropdownMenuItems() })

// Can also set showAppBar to false, pass in floatingActionButton, or pass in other menuItems
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolbarScaffold(
    title: String = "",
    showAppBar: Boolean = true,
    showBackButton: Boolean = false,
    navController: NavController? = null,
    showDropdownMenu: Boolean = false,
    menuItems: @Composable () -> Unit = {},
    dropdownMenuItems: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    ICA2_PrepTheme {
        Scaffold(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),

            floatingActionButton = floatingActionButton,

            topBar = {
                if (!showAppBar) return@Scaffold

                TopAppBar(
                    title = {
                        val modifier = if (showBackButton) Modifier else Modifier.padding(start = 8.dp)
                        Text(title, color = MaterialTheme.colorScheme.onPrimary, modifier = modifier)
                    },

                    navigationIcon = {
                        if (!showBackButton) return@TopAppBar

                        IconButton(onClick = {
                            navController?.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },

                    actions = {
                        if (showDropdownMenu) {
                            Row {
                                menuItems()

                                IconButton(onClick = { menuExpanded = true }) {
                                    Icon(Icons.Default.MoreVert, "More", tint = MaterialTheme.colorScheme.onPrimary)
                                }

                                DropdownMenu(
                                    expanded = menuExpanded,
                                    onDismissRequest = { menuExpanded = false }
                                ) {
                                    dropdownMenuItems()
                                }
                            }
                        }
                    },

                    colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary),
                )
            }
        ) { paddingValues ->
            Box(Modifier.padding(paddingValues)) {
                content()
            }
        }
    }
}


/*
// Screen with row of tabs above
TabScreen(
    numTabs = 2,
    tabTitles = listOf("Tab 1", "Tab 2"),
    tabIcons = listOf(Icons.AutoMirrored.Filled.List, Icons.Default.Person)
) { tabIndex ->
    when (tabIndex) {
        0 -> Tab1(viewModel, navController)
        1 -> Tab2(viewModel, navController)
        // Add more tabs here
    }
}
 */

@Composable
fun TabScreen(numTabs: Int, tabTitles: List<String> = emptyList(), tabIcons: List<ImageVector> = emptyList(), getTab: @Composable (Int) -> Unit) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val filledTabTitles = tabTitles + List((numTabs - tabTitles.size).coerceAtLeast(0)) { "Tab ${it + 1}" }
    val filledTabIcons = tabIcons + List((numTabs - tabIcons.size).coerceAtLeast(0)) { Icons.Default.Info }

    Column {
        TabRow(tabIndex) {
            for (index in 0 until numTabs) {
                if (tabTitles.isEmpty() && tabIcons.isEmpty()) {
                    Tab(
                        selected = tabIndex == index,
                        onClick = { tabIndex = index }
                    )
                } else if (tabTitles.isEmpty()) {
                    Tab(
                        icon = { Icon(filledTabIcons[index], null) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index }
                    )
                } else if (tabIcons.isEmpty()) {
                    Tab(
                        text = { Text(filledTabTitles[index]) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index }
                    )
                } else {
                    Tab(
                        text = { Text(filledTabTitles[index]) },
                        icon = { Icon(filledTabIcons[index], null) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index }
                    )
                }
            }
        }

        getTab(tabIndex)
    }
}


/*
// HorizontalPager with indicator
PagerScreen(2) { page ->
    when (page) {
        0 -> { Page1() }
        1 -> { Page2(navController) }
        // Add more pages here
    }
}
// And then define each page individually (see Onboarding.kt)
 */

@Composable
fun PagerScreen(numPages: Int, getPage: @Composable (page: Int) -> Unit) {
    val pagerState = rememberPagerState(pageCount = { numPages })
    HorizontalPager(state = pagerState) { page -> getPage(page) }
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


/*
// AlertDialog with TextField

var showDialog by remember { mutableStateOf(false) }

if (showDialog) {
    InputDialog(
        title = "Title",
        onValueSet = { text ->
            // Handle input
        },
        onDismiss = { showDialog = false }
    )
}

// Set showDialog to true to show the dialog
 */

@Composable
fun InputDialog(title: String, onValueSet: (String) -> Unit, onDismiss: () -> Unit) {
    var text by remember { mutableStateOf("") }
    // Add more variables as needed (remember to add the parameters in onValueSet)

    AlertDialog(
        onDismissRequest = onDismiss,

        title = { Text(title) },

        text = {
            Column {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Enter text") }
                )
                // Add more input fields as needed
            }
        },

        confirmButton = {
            Button(
                onClick = {
                    onValueSet(text)
                    onDismiss()
                }
            ) {
                Text("Save")
            }
        },

        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}


/*
// Rating bar like the one in xml
var rating by remember { mutableFloatStateOf(0f) }
RatingBar(rating, isEditable = true) { rating = it } // Editable
RatingBar(3.5f) // Non-editable
 */

@Composable
fun RatingBar(
    rating: Float = 5f,
    maxRating: Int = 5,
    step: Float = 0.5f,
    size: Dp = 24.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    isEditable: Boolean = false,
    onRatingChanged: (Float) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .pointerInput(Unit) {
                if (!isEditable) return@pointerInput
                detectTapGestures { offset ->
                    val temp = offset.x / size.toPx()
                    val newRating = round(temp / step) * step
                    onRatingChanged(newRating)
                }
            }
            .pointerInput(Unit) {
                if (!isEditable) return@pointerInput
                detectDragGestures { change, _ ->
                    val temp = change.position.x / size.toPx()
                    val newRating = round(temp / step) * step
                    onRatingChanged(newRating)
                }
            }
    ) {
        for (i in 1..maxRating) {
            if (i <= rating.toInt()) {
                // Full stars
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(size)
                )
            } else if (i == rating.toInt() + 1 && rating % 1 != 0f) {
                // Partial star
                PartialStar(rating % 1, size, color)
            } else {
                // Empty stars
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(size)
                )
            }
        }
    }
}

@Composable
private fun PartialStar(fraction: Float, size: Dp, color: Color) {
    val customShape = FractionalClipShape(fraction)

    Box {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(size)
        )
        Box(
            modifier = Modifier
                .graphicsLayer(
                    clip = true,
                    shape = customShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(size)
            )
        }
    }
}

private class FractionalClipShape(private val fraction: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(
            rect = Rect(
                left = 0f,
                top = 0f,
                right = size.width * fraction,
                bottom = size.height
            )
        )
    }
}
