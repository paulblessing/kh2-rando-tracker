@file:OptIn(ExperimentalLayout::class)

package io.github.paulblessing.kh2randotracker

import androidx.compose.desktop.AppManager
import androidx.compose.desktop.AppWindow
import androidx.compose.foundation.layout.ExperimentalLayout
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.Menu
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.MenuItem
import java.awt.datatransfer.DataFlavor
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.SwingUtilities
import javax.swing.TransferHandler

val imagesByNumber = listOf(
  "Zero",
  "One",
  "Two",
  "Three",
  "Four",
  "Five",
  "Six",
  "Seven",
  "Eight",
  "Nine",
  "Ten",
  "Eleven",
  "Twelve",
  "Thirteen",
  "Fourteen",
  "Fifteen",
  "Sixteen",
  "Seventeen",
  "Eighteen",
  "Nineteen",
  "Twenty"
).map { "images/${it}.png" }

fun main() {
  SwingUtilities.invokeLater {
    val state = TrackerState

    val openHintFile = MenuItem("Load hints", onClick = {
      selectHintsFile(state)
    })
    val menu = Menu("File", openHintFile)
    val menuBar = MenuBar(menu)

    AppWindow(
      title = "KH2 Randomizer Tracker",
      location = IntOffset(200, 200),
      size = IntSize(640, 800),
      icon = getIcon(),
      menuBar = menuBar
    ).show {
      MainWindow(state, onSelectHintsFile = { selectHintsFile(state) })
    }

    AppWindow(
      title = "KH2 Randomizer Broadcast",
      location = IntOffset(600, 600),
      size = IntSize(320, 600)
    ).show {
      BroadcastWindow(state, onSelectHintsFile = { selectHintsFile(state) })
    }

    val transferHandler = object : TransferHandler() {
      override fun canImport(support: TransferSupport): Boolean {
        return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)
      }

      override fun importData(support: TransferSupport): Boolean {
        if (state.hintsLoaded) {
          return false
        }

        if (!support.isDrop) {
          return false
        }

        if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
          return false
        }

        val transferable = support.transferable
        val fileList = transferable.getTransferData(DataFlavor.javaFileListFlavor) as? List<File> ?: return false

        val hintsFile = fileList.singleOrNull() ?: return false
        loadHints(state, hintsFile)

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
  return classLoader.getResourceAsStream("images/simple/the_world_that_never_was.png")?.let(ImageIO::read)
}
