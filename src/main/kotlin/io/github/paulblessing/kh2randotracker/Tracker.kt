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
import javax.imageio.ImageIO
import javax.swing.SwingUtilities
import javax.swing.TransferHandler
import kotlin.system.exitProcess

val AmbientImportantCheckLocationIconSet = staticAmbientOf<ImportantCheckLocationIconSet> {
  error("No important check icon set has been set")
}
val AmbientImportantCheckIconSet = staticAmbientOf<ImportantCheckIconSet> {
  error("No important check icon set has been set")
}

fun main() {
  val savedState = SavedState.load()
  val trackerState = savedState?.trackerState
  val savedLocationIconSet = when (savedState?.importantCheckLocationIconSet) {
    "classic" -> ImportantCheckLocationIconSet.classic
    else -> ImportantCheckLocationIconSet.simple
  }
  val savedCheckIconSet = when (savedState?.importantCheckIconSet) {
    "classic" -> ImportantCheckIconSet.classic
    else -> ImportantCheckIconSet.simple
  }

  SwingUtilities.invokeLater {
    val stateHolder = mutableStateOf(trackerState)
    var importantCheckLocationIconSet: ImportantCheckLocationIconSet by mutableStateOf(savedLocationIconSet)
    var importantCheckIconSet: ImportantCheckIconSet by mutableStateOf(savedCheckIconSet)
    var aboutDialogShowing: Boolean by mutableStateOf(false)

    Desktop.getDesktop().setQuitHandler { _, response ->
      // TODO: Dialog or something?
      SavedState.save(stateHolder.value, importantCheckLocationIconSet, importantCheckIconSet)
      response.performQuit()
    }

    AppManager.setEvents(onWindowsEmpty = {
      // TODO: Dialog or something?
      SavedState.save(stateHolder.value, importantCheckLocationIconSet, importantCheckIconSet)
      exitProcess(0)
    })

    val reset = MenuItem("Reset", onClick = {
      stateHolder.value = null
    })
    val fileMenu = Menu("File", reset)

    val simpleLocationIcons = MenuItem("Use minimal location icons", onClick = {
      importantCheckLocationIconSet = ImportantCheckLocationIconSet.simple
    })
    val classicLocationIcons = MenuItem("Use classic location icons", onClick = {
      importantCheckLocationIconSet = ImportantCheckLocationIconSet.classic
    })
    val simpleItemIcons = MenuItem("Use minimal item icons", onClick = {
      importantCheckIconSet = ImportantCheckIconSet.simple
    })
    val classicItemIcons = MenuItem("Use classic item icons", onClick = {
      importantCheckIconSet = ImportantCheckIconSet.classic
    })
    val optionsMenu = Menu("Options", simpleLocationIcons, classicLocationIcons, simpleItemIcons, classicItemIcons)

    val about = MenuItem("About tracker", onClick = {
      aboutDialogShowing = true
    })
    val aboutMenu = Menu("About", about)

    val menuBar = MenuBar(fileMenu, optionsMenu, aboutMenu)

    AppWindow(
      title = "KH2 Randomizer Tracker",
      centered = false,
      location = IntOffset(100, 50),
      size = IntSize(640, 800),
      icon = getIcon(),
      menuBar = menuBar
    ).show {
      val state = stateHolder.value
      Providers(
        AmbientImportantCheckLocationIconSet provides importantCheckLocationIconSet,
        AmbientImportantCheckIconSet provides importantCheckIconSet
      ) {
        MaterialTheme(colors = darkColors()) {
          Surface(modifier = Modifier.fillMaxSize()) {
            if (state == null) {
              LoadHintsView(onSelectHintsFile = { selectHintsFile(stateHolder) })
            } else {
              MainWindow(state)
            }
          }
        }
      }

      if (aboutDialogShowing) {
        AboutDialog(onDismissRequest = { aboutDialogShowing = false })
      }
    }

    AppWindow(
      title = "KH2 Randomizer Broadcast",
      centered = false,
      location = IntOffset(900, 200),
      size = IntSize(320, 500),
      menuBar = menuBar
    ).show {
      val state = stateHolder.value
      Providers(
        AmbientImportantCheckLocationIconSet provides importantCheckLocationIconSet,
        AmbientImportantCheckIconSet provides importantCheckIconSet
      ) {
        MaterialTheme(colors = darkColors()) {
          Surface(modifier = Modifier.fillMaxSize()) {
            if (state == null) {
              LoadHintsView(onSelectHintsFile = { selectHintsFile(stateHolder) })
            } else {
              BroadcastWindow(state)
            }
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

        val hintsFile = fileList.singleOrNull() ?: return false
        loadHints(stateHolder, hintsFile)

        return true
      }
    }
    for (window in AppManager.windows) {
      window.window.transferHandler = transferHandler
    }
  }
}

private fun getIcon(): BufferedImage? {
  val classLoader = Thread.currentThread().contextClassLoader
  return classLoader.getResourceAsStream("images/classic/replica_data.png")?.let(ImageIO::read)
}
