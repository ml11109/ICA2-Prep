package com.example.ica2_prep.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ica2_prep.ui.theme.ICA2_PrepTheme
import kotlin.math.round

/*
Contents:
- Text, button, image, box, column, row, grid, lazy column, lazy row, lazy grid
- Tab screen
- View pager
- Toolbar
- Rating bar
- Android view
- Canvas
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

// For dropdown menu, showDropdownMenu = true and pass in menuItems, eg:
@Composable
fun menuItems() {
    DropdownMenuItem(text = { Text("Item 1") }, onClick = { /* Handle click */ })
    DropdownMenuItem(text = { Text("Item 2") }, onClick = { /* Handle click */ })
}
CollapsingToolbarScaffold("Title", showDropdownMenu = true, menuItems = { menuItems() })
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
    content: @Composable (nestedScrollConnection: NestedScrollConnection) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    var menuExpanded by remember { mutableStateOf(false) }

    ICA2_PrepTheme {
        Scaffold(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),

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
                        if (!showDropdownMenu) return@TopAppBar

                        Row {
                            IconButton(onClick = { menuExpanded = true }) {
                                Icon(Icons.Default.MoreVert, "More", tint = MaterialTheme.colorScheme.onPrimary)
                            }

                            DropdownMenu(
                                expanded = menuExpanded,
                                onDismissRequest = { menuExpanded = false }
                            ) {
                                menuItems()
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

// For dropdown menu, showDropdownMenu = true and pass in menuItems, eg:
@Composable
fun menuItems() {
    DropdownMenuItem(text = { Text("Item 1") }, onClick = { /* Handle click */ })
    DropdownMenuItem(text = { Text("Item 2") }, onClick = { /* Handle click */ })
}
ToolbarScaffold("Title", showDropdownMenu = true, menuItems = { menuItems() })
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
    content: @Composable () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    ICA2_PrepTheme {
        Scaffold(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),

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
                        if (!showDropdownMenu) return@TopAppBar

                        Row {
                            IconButton(onClick = { menuExpanded = true }) {
                                Icon(Icons.Default.MoreVert, "More", tint = MaterialTheme.colorScheme.onPrimary)
                            }

                            DropdownMenu(
                                expanded = menuExpanded,
                                onDismissRequest = { menuExpanded = false }
                            ) {
                                menuItems()
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


// Rating bar
// Eg. RatingBar(rating, isEditable = true) { rating = it }

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
