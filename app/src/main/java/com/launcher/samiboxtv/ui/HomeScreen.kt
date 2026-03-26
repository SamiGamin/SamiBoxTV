package com.launcher.samiboxtv.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.basicMarquee
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Card
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import com.launcher.samiboxtv.MainViewModel
import com.launcher.samiboxtv.R
import com.launcher.samiboxtv.data.AppInfo
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onAppSelected: (AppInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    val apps by viewModel.apps.collectAsState()

    var selectedAppForMenu by remember { mutableStateOf<AppInfo?>(null) }
    var editingApp by remember { mutableStateOf<AppInfo?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    HomeScreenContent(
        apps = apps,
        selectedAppForMenu = selectedAppForMenu,
        editingApp = editingApp,
        onAppSelected = onAppSelected,
        onLongClickApp = { if (editingApp == null) selectedAppForMenu = it },
        onMoveLeft = { viewModel.moveApp(it, -1) },
        onMoveRight = { viewModel.moveApp(it, 1) },
        onMoveUp = { viewModel.moveApp(it, -5) },
        onMoveDown = { viewModel.moveApp(it, 5) },
        onExitEdit = { editingApp = null },
        onShowAddDialog = { showAddDialog = true },
        modifier = modifier
    )

    selectedAppForMenu?.let { app ->
        AppContextMenu(
            appInfo = app,
            onDismiss = { selectedAppForMenu = null },
            onMove = { editingApp = app },
            onHide = { viewModel.hideApp(app) }
        )
    }

    if (showAddDialog) {
        AddAppDialog(
            viewModel = viewModel,
            onDismiss = { showAddDialog = false }
        )
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HomeScreenContent(
    apps: List<AppInfo>,
    selectedAppForMenu: AppInfo?,
    editingApp: AppInfo?,
    onAppSelected: (AppInfo) -> Unit,
    onLongClickApp: (AppInfo) -> Unit,
    onMoveLeft: (AppInfo) -> Unit,
    onMoveRight: (AppInfo) -> Unit,
    onMoveUp: (AppInfo) -> Unit,
    onMoveDown: (AppInfo) -> Unit,
    onExitEdit: () -> Unit,
    onShowAddDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize().background(Color(0xFF121212))) {
        LauncherHeader()

        val allItems = apps.toList() + "ADD_BUTTON"
        val appsPerPage = 10
        val pagedItems = allItems.chunked(appsPerPage)
        val pagerState = rememberPagerState(pageCount = { pagedItems.size })

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) { pageIndex ->
            val itemsOnThisPage = pagedItems[pageIndex]

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(28.dp),
                    verticalArrangement = Arrangement.spacedBy(28.dp),
                    userScrollEnabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 58.dp, end = 58.dp, top = 6.dp, bottom = 36.dp)
                ) {
                    items(
                        items = itemsOnThisPage,
                        key = { item -> if (item is AppInfo) item.packageName else "ADD_BUTTON" }
                    ) { item ->
                        when (item) {
                            is AppInfo -> {
                                val isEditing = editingApp?.packageName == item.packageName
                                AppCard(
                                    appInfo = item,
                                    onClick = {
                                        if (!isEditing) onAppSelected(item)
                                    },
                                    onLongClick = { onLongClickApp(item) },
                                    isEditing = isEditing,
                                    onMoveLeft = { onMoveLeft(item) },
                                    onMoveRight = { onMoveRight(item) },
                                    onMoveUp = { onMoveUp(item) },
                                    onMoveDown = { onMoveDown(item) },
                                    onExitEdit = onExitEdit
                                )
                            }
                            else -> {
                                val addInteractionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                                val addIsFocused by addInteractionSource.collectIsFocusedAsState()

                                Card(
                                    onClick = onShowAddDialog,
                                    interactionSource = addInteractionSource,
                                    shape = androidx.tv.material3.CardDefaults.shape(androidx.compose.foundation.shape.RoundedCornerShape(8.dp)),
                                    colors = androidx.tv.material3.CardDefaults.colors(
                                        containerColor = Color(0xFF0B1426),
                                        focusedContainerColor = Color(0xFF132238)
                                    ),
                                    border = androidx.tv.material3.CardDefaults.border(
                                        focusedBorder = androidx.tv.material3.Border(
                                            border = androidx.compose.foundation.BorderStroke(3.dp, Color(0xFFFF8C00)),
                                            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                                        )
                                    )
                                ) {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.padding(16.dp).fillMaxSize()
                                        ) {
                                            Image(
                                                painter = coil.compose.rememberAsyncImagePainter(model = R.drawable.ic_add_retro),
                                                contentDescription = "Agregar",
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .aspectRatio(1f),
                                                contentScale = androidx.compose.ui.layout.ContentScale.Fit
                                            )
                                            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "Añadir Aplicación",
                                                color = if (addIsFocused) Color(0xFFFF8C00) else Color.White,
                                                fontFamily = FontFamily(Font(R.font.press_start_2p)),
                                                fontWeight = if (addIsFocused) FontWeight.Bold else FontWeight.Normal,
                                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                                maxLines = 1,
                                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                                                modifier = Modifier.then(if (addIsFocused) Modifier.basicMarquee() else Modifier)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LauncherHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 58.dp, vertical = 30.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = coil.compose.rememberAsyncImagePainter(model = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(48.dp).padding(end = 16.dp)
            )
            Text(
                text = "SamiBox TV",
                color = Color(0xFF00E5FF),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.press_start_2p))
            )
        }

        ClockView()
    }
}

@Composable
fun ClockView() {
    var timeText by remember { mutableStateOf(getFormattedTime()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            timeText = getFormattedTime()
        }
    }
    Text(
        text = timeText,
        color = Color(0xFFFF8C00),
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = FontFamily(Font(R.font.press_start_2p))
    )
}

fun getFormattedTime(): String {
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("America/Bogota")
    return sdf.format(Date())
}

@Preview(device = Devices.TV_1080p)
@Composable
fun HomeScreenPreview() {
    HomeScreenContent(
        apps = emptyList(),
        selectedAppForMenu = null,
        editingApp = null,
        onAppSelected = {},
        onLongClickApp = {},
        onMoveLeft = {},
        onMoveRight = {},
        onMoveUp = {},
        onMoveDown = {},
        onExitEdit = {},
        onShowAddDialog = {}
    )
}
