@file:JvmName("Tracker")

package io.github.paulblessing.kh2randotracker

import androidx.compose.desktop.AppManager
import androidx.compose.desktop.AppWindow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.Menu
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.MenuItem
import java.awt.Desktop
import java.awt.datatransfer.DataFlavor
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import javax.swing.SwingUtilities
import javax.swing.TransferHandler
import kotlin.math.roundToInt
import kotlin.system.exitProcess

val AmbientImportantCheckLocationIconSet = staticAmbientOf<ImportantCheckLocationIconSet> {
  error("No important check icon set has been set")
}
val AmbientImportantCheckIconSet = staticAmbientOf<ImportantCheckIconSet> {
  error("No important check icon set has been set")
}
val AmbientIconScale = staticAmbientOf<Float> {
  error("No icon scale has been set")
}

fun main() {
  val savedState = try {
    SavedState.load()
  } catch (e: Exception) {
    null
  }

  val classLoader = TrackerState::class.java.classLoader
  val trackerVersion = try {
    classLoader.getResourceAsStream("build.properties").use { stream ->
      val properties = Properties()
      properties.load(stream)
      properties.getProperty("tracker.version", "<Unknown>")
    }
  } catch (e: Exception) {
    "<Unknown>"
  }

  val trackerState = savedState?.trackerState
  val savedLocationIconSet = ImportantCheckLocationIconSet.byName(savedState?.importantCheckLocationIconSet)
  val savedCheckIconSet = ImportantCheckIconSet.byName(savedState?.importantCheckIconSet)
  val savedUiState = savedState?.uiState ?: UiState()

  SwingUtilities.invokeLater {
    val stateHolder = mutableStateOf(trackerState)
    val hintLoadingErrorHolder = mutableStateOf<Throwable?>(null)
    var growthAbilityMode: GrowthAbilityMode by mutableStateOf(savedUiState.growthAbilityMode)
    var importantCheckLocationIconSet: ImportantCheckLocationIconSet by mutableStateOf(savedLocationIconSet)
    var importantCheckIconSet: ImportantCheckIconSet by mutableStateOf(savedCheckIconSet)
    var trackerWindowSize: SizeClass by mutableStateOf(savedUiState.trackerWindowSizeClass)
    var broadcastWindowSize: SizeClass by mutableStateOf(savedUiState.broadcastWindowSizeClass)
    var settingsDialogShowing: Boolean by mutableStateOf(false)
    var aboutDialogShowing: Boolean by mutableStateOf(false)
    var confirmResetDialogShowing: Boolean by mutableStateOf(false)

    val dummy = MenuItem("Version $trackerVersion", onClick = { })
    val reset = MenuItem("Reset", onClick = {
      confirmResetDialogShowing = true
    })
    val restoreFromAutoSave = MenuItem("Restore from auto save", onClick = {
      val autoSavedState = SavedState.loadFromAutoSave()
      if (autoSavedState != null) {
        val autoSavedTrackerState = autoSavedState.trackerState
        if (autoSavedTrackerState != null) {
          stateHolder.value = trackerState
        }
      }
    })
    val fileMenu = Menu("File", dummy, reset, restoreFromAutoSave)

    val options = MenuItem("Options", onClick = {
      settingsDialogShowing = true
    })
    val optionsMenu = Menu("Options", options)

    val about = MenuItem("About tracker", onClick = {
      aboutDialogShowing = true
    })
    val aboutMenu = Menu("About", about)

    val menuBar = MenuBar(fileMenu, optionsMenu, aboutMenu)

    val trackerWindowMetrics = savedUiState.trackerWindowMetrics
    val trackerWindow = AppWindow(
      title = "KH2 Randomizer Tracker",
      centered = false,
      location = IntOffset(x = trackerWindowMetrics.x, y = trackerWindowMetrics.y),
      size = IntSize(width = trackerWindowMetrics.width, height = trackerWindowMetrics.height),
      icon = getIcon(),
      menuBar = menuBar
    )

    val broadcastWindowMetrics = savedUiState.broadcastWindowMetrics
    val broadcastWindow = AppWindow(
      title = "KH2 Randomizer Broadcast",
      centered = false,
      location = IntOffset(x = broadcastWindowMetrics.x, y = broadcastWindowMetrics.y),
      size = IntSize(width = broadcastWindowMetrics.width, height = broadcastWindowMetrics.height),
      menuBar = menuBar
    )

    val getUiState = {
      UiState(
        growthAbilityMode = growthAbilityMode,
        trackerWindowSizeClass = trackerWindowSize,
        trackerWindowMetrics = WindowSizeAndPosition(
          x = trackerWindow.x,
          y = trackerWindow.y,
          width = trackerWindow.width,
          height = trackerWindow.height
        ),
        broadcastWindowSizeClass = broadcastWindowSize,
        broadcastWindowMetrics = WindowSizeAndPosition(
          x = broadcastWindow.x,
          y = broadcastWindow.y,
          width = broadcastWindow.width,
          height = broadcastWindow.height
        )
      )
    }

    val saveState = {
      val uiState = getUiState()
      // TODO: Dialog or something?
      SavedState.save(stateHolder.value, importantCheckLocationIconSet, importantCheckIconSet, uiState)
    }

    val desktop = Desktop.getDesktop()
    if (desktop.isSupported(Desktop.Action.APP_QUIT_HANDLER)) {
      desktop.setQuitHandler { _, response ->
        saveState()
        response.performQuit()
      }
    }

    AppManager.setEvents(onWindowsEmpty = {
      saveState()
      exitProcess(0)
    })

    trackerWindow.show {
      TrackerTheme(importantCheckLocationIconSet, importantCheckIconSet, trackerWindowSize) {
        Surface(modifier = Modifier.fillMaxSize()) {
          val state = stateHolder.value
          if (state == null) {
            LoadHintsView(
              hintLoadingError = hintLoadingErrorHolder.value,
              onSelectHintFile = { selectAndLoadHintFile(stateHolder, hintLoadingErrorHolder) }
            )
          } else {
            MainWindow(state, growthAbilityMode = growthAbilityMode)
          }
        }
      }

      if (settingsDialogShowing) {
        SettingsDialog(
          growthAbilityMode = growthAbilityMode,
          onGrowthAbilityModeSelected = { mode ->
            growthAbilityMode = mode
          },
          importantCheckLocationIconSet = importantCheckLocationIconSet.name,
          onImportantCheckLocationIconSetSelected = { iconSetName ->
            importantCheckLocationIconSet = ImportantCheckLocationIconSet.byName(iconSetName)
          },
          importantCheckIconSet = importantCheckIconSet.name,
          onImportantCheckIconSetSelected = { iconSetName ->
            importantCheckIconSet = ImportantCheckIconSet.byName(iconSetName)
          },
          trackerWindowSizeClass = trackerWindowSize,
          onTrackerWindowSizeClassSelected = { size ->
            val scale = size.scale
            trackerWindowSize = size
            trackerWindow.setSize(
              width = (DEFAULT_TRACKER_WINDOW_WIDTH.toFloat() * scale).roundToInt(),
              height = (DEFAULT_TRACKER_WINDOW_HEIGHT.toFloat() * scale).roundToInt()
            )
          },
          broadcastWindowSizeClass = broadcastWindowSize,
          onBroadcastWindowSizeClassSelected = { size ->
            val scale = size.scale
            broadcastWindowSize = size
            broadcastWindow.setSize(
              width = (DEFAULT_BROADCAST_WINDOW_WIDTH.toFloat() * scale).roundToInt(),
              height = (DEFAULT_BROADCAST_WINDOW_HEIGHT.toFloat() * scale).roundToInt()
            )
          },
          onDismissRequest = { settingsDialogShowing = false }
        )
      }

      if (aboutDialogShowing) {
        AboutDialog(trackerVersion = trackerVersion, onDismissRequest = { aboutDialogShowing = false })
      }

      if (confirmResetDialogShowing) {
        ConfirmResetDialog(
          onDismissRequest = { confirmResetDialogShowing = false },
          onResetConfirmed = {
            val uiState = getUiState()
            SavedState.autoSave(trackerState, importantCheckLocationIconSet, importantCheckIconSet, uiState)
            stateHolder.value = null
            confirmResetDialogShowing = false
          }
        )
      }
    }

    broadcastWindow.show {
      TrackerTheme(importantCheckLocationIconSet, importantCheckIconSet, broadcastWindowSize) {
        Surface(modifier = Modifier.fillMaxSize()) {
          val state = stateHolder.value
          if (state == null) {
            LoadHintsView(
              hintLoadingError = hintLoadingErrorHolder.value,
              onSelectHintFile = { selectAndLoadHintFile(stateHolder, hintLoadingErrorHolder) }
            )
          } else {
            BroadcastWindow(state, growthAbilityMode = growthAbilityMode)
          }
        }
      }
    }

    val transferHandler = object : TransferHandler() {
      override fun canImport(support: TransferSupport): Boolean {
        return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)
      }

      override fun importData(support: TransferSupport): Boolean {
        if (stateHolder.value != null) {
          return false
        }

        if (!support.isDrop) {
          return false
        }

        if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
          return false
        }

        val transferable = support.transferable

        @Suppress("UNCHECKED_CAST")
        val fileList = transferable.getTransferData(DataFlavor.javaFileListFlavor) as? List<File> ?: return false

        val hintFile = fileList.singleOrNull() ?: return false
        loadHints(hintFile, stateHolder, hintLoadingErrorHolder)

        return true
      }
    }
    trackerWindow.window.transferHandler = transferHandler
    broadcastWindow.window.transferHandler = transferHandler
  }
}

private fun getIcon(): BufferedImage? {
  val classLoader = Thread.currentThread().contextClassLoader
  return classLoader.getResourceAsStream("images/classic/replica_data.png")?.let(ImageIO::read)
}

@Composable private fun TrackerTheme(
  importantCheckLocationIconSet: ImportantCheckLocationIconSet,
  importantCheckIconSet: ImportantCheckIconSet,
  sizeClass: SizeClass,
  content: @Composable () -> Unit
) {
  Providers(
    AmbientImportantCheckLocationIconSet provides importantCheckLocationIconSet,
    AmbientImportantCheckIconSet provides importantCheckIconSet,
    AmbientIconScale provides sizeClass.scale
  ) {
    MaterialTheme(colors = darkColors()) {
      content()
    }
  }
}
