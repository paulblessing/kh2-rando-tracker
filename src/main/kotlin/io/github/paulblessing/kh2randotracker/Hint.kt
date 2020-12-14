package io.github.paulblessing.kh2randotracker

import androidx.compose.desktop.AppManager
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
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
import javax.swing.filechooser.FileNameExtensionFilter

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

@Composable fun LoadHintsView(hintLoadingError: Throwable?, onSelectHintFile: () -> Unit) {
  Box(Modifier.padding(16.dp)) {
    Column(Modifier.wrapContentSize().align(Alignment.Center)) {
      if (hintLoadingError == null) {
        Text(
          "Drag and drop a hint file onto this window or choose 'Load hints'.",
          Modifier.align(Alignment.CenterHorizontally),
          textAlign = TextAlign.Center
        )
      } else {
        Text(
          "Error loading hint file. Try loading the hint file again or try regenerating the hint file.",
          Modifier.align(Alignment.CenterHorizontally),
          textAlign = TextAlign.Center,
          color = MaterialTheme.colors.error
        )
      }
      Spacer(Modifier.height(8.dp))
      Button(onSelectHintFile, Modifier.align(Alignment.CenterHorizontally)) {
        Text("Load hints")
      }
    }
  }
}

fun selectAndLoadHintFile(stateHolder: MutableState<TrackerState?>, errorHolder: MutableState<Throwable?>) {
  val fileChooser = JFileChooser().apply {
    isAcceptAllFileFilterUsed = false
    addChoosableFileFilter(FileNameExtensionFilter("Hint files", "txt"))
  }
  if (fileChooser.showOpenDialog(AppManager.focusedWindow?.window) == JFileChooser.APPROVE_OPTION) {
    val hintFile = fileChooser.selectedFile
    if (hintFile != null && hintFile.isFile) {
      loadHints(hintFile, stateHolder, errorHolder)
    }
  }
}

fun loadHints(hintFile: File, stateHolder: MutableState<TrackerState?>, errorHolder: MutableState<Throwable?>) {
  GlobalScope.launch(Dispatchers.IO) {
    val result = runCatching { readHintFile(hintFile) }
    withContext(Dispatchers.Main) {
      result.fold(
        onSuccess = { (hints, hintSettings) ->
          stateHolder.value = TrackerState.fromHints(hints, hintSettings)
          errorHolder.value = null
        },
        onFailure = { error ->
          stateHolder.value = null
          errorHolder.value = error
        }
      )
    }
  }
}

fun readHintFile(hintFile: File): Pair<List<Hint>, Set<HintSetting>> {
  val lines = hintFile.readLines()
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
