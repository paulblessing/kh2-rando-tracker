package io.github.paulblessing.kh2randotracker

import androidx.compose.desktop.AppManager
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*
import javax.swing.JFileChooser

data class Hint(
  val reportLocation: ImportantCheckLocation,
  val hintedLocation: ImportantCheckLocation,
  val importantCheckCount: Int
)

enum class HintSetting(val hintFileText: String) {

  PromiseCharm("Promise Charm"),
  SecondChanceOnceMore("Second Chance & Once More"),
  TornPages("Torn Pages"),
  AnsemReports("Secret Ansem Reports"),
  Cure("Cure"),
  FinalForm("Final Form"),
  SorasHeart("Sora's Heart"),
  SimulatedTwilightTown("Simulated Twilight Town"),
  HundredAcreWood("100 Acre Wood"),
  Atlantica("Atlantica")

}

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

fun selectHintsFile(stateHolder: MutableState<TrackerState?>) {
  val fileChooser = JFileChooser()
  if (fileChooser.showOpenDialog(AppManager.focusedWindow?.window) == JFileChooser.APPROVE_OPTION) {
    val file = fileChooser.selectedFile
    if (file != null && file.isFile) {
      loadHints(stateHolder, file)
    }
  }
}

fun loadHints(stateHolder: MutableState<TrackerState?>, file: File) {
  GlobalScope.launch(Dispatchers.IO) {
    val (hints, hintSettings) = readHintFile(file)
    withContext(Dispatchers.Main) {
      stateHolder.value = TrackerState.fromHints(hints, hintSettings)
    }
  }
}

fun readHintFile(file: File): Pair<List<Hint>, Set<HintSetting>> {
  val lines = file.readLines()
  val reportValues = lines[0].split(".")
  val reportLocationAddresses = lines[1].trimEnd('.').split(".")
  val hints = reportLocationAddresses.mapIndexed { index, reportLocationAddress ->
    val temp = reportValues[index].split(",")
    Hint(
      reportLocation = ImportantCheckLocation.byAddress(Address(reportLocationAddress)),
      hintedLocation = ImportantCheckLocation.byAddress(Address(temp[0])),
      importantCheckCount = temp[1].toInt() - 32
    )
  }

  val hintSettingsStrings = lines[2].split("-").map(String::trim)
  val hintSettings = hintSettingsStrings.mapNotNullTo(EnumSet.noneOf(HintSetting::class.java)) { hintSettingString ->
    HintSetting.values().firstOrNull { it.hintFileText == hintSettingString }
  }

  return hints to hintSettings
}
