package io.github.paulblessing.kh2randotracker

import androidx.compose.desktop.AppManager
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import javax.swing.JFileChooser

data class Hint(
  val hintedWorldId: String,
  val importantCheckCount: Int
)

@Composable fun LoadHintsView(onSelectHintsFile: () -> Unit) {
  Box(Modifier.padding(16.dp)) {
    Column(Modifier.wrapContentSize().align(Alignment.Center)) {
      Text(
        "Drag and drop a hint file onto this window or choose 'Load hints'.",
        Modifier.align(Alignment.CenterHorizontally),
        textAlign = TextAlign.Center
      )
      Spacer(Modifier.height(8.dp))
      Button(onSelectHintsFile, Modifier.align(Alignment.CenterHorizontally)) {
        Text("Load hints")
      }
    }
  }
}

fun selectHintsFile(state: TrackerState) {
  val fileChooser = JFileChooser()
  if (fileChooser.showOpenDialog(AppManager.focusedWindow?.window) == JFileChooser.APPROVE_OPTION) {
    val file = fileChooser.selectedFile
    if (file != null && file.isFile) {
      loadHints(state, file)
    }
  }
}

fun loadHints(state: TrackerState, file: File) {
  GlobalScope.launch(Dispatchers.IO) {
    val hints = readHintFile(file)

    state.hintsLoaded = true
  }
}

fun readHintFile(file: File): List<Hint> {
  val lines = file.readLines()
  val reportValues = lines[0].split(".")
  val reportOrder = lines[1].trimEnd('.').split(".")

  val reportLocations = mutableListOf<String>()
  val hints = mutableListOf<Hint>()
  reportOrder.forEachIndexed { index, value ->
    reportLocations.add(Codes.findCode(value))
    val temp = reportValues[index].split(",")
    hints.add(Hint(Codes.findCode(temp[0]), temp[1].toInt() - 32))
  }

  println(reportLocations)
  println(hints)

  return hints
}

