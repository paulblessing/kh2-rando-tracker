package io.github.paulblessing.kh2randotracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.imageFromResource
import androidx.compose.ui.unit.dp

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

@Composable fun ReportOrPageCounter(found: Int, total: Int) {
  Row(Modifier.height(16.dp), verticalAlignment = Alignment.CenterVertically) {
    val foundTens = found / 10
    val foundOnes = found - (foundTens * 10)
    if (foundTens > 0) {
      Image(imageFromResource(imagesByNumber[foundTens]))
    }
    Image(imageFromResource(imagesByNumber[foundOnes]))
    Spacer(Modifier.width(4.dp))
    Image(imageFromResource("images/VerticalBar.png"), modifier = Modifier.rotate(20.0f))
    Spacer(Modifier.width(4.dp))
    val totalTens = total / 10
    val totalOnes = total - (totalTens * 10)
    if (totalTens > 0) {
      Image(imageFromResource(imagesByNumber[totalTens]))
    }
    Image(imageFromResource(imagesByNumber[totalOnes]))
  }
}

@Composable fun ImportantCheckCounter(
  state: TrackerState,
  locationState: ImportantCheckLocationState,
  showFoundChecks: Boolean,
  modifier: Modifier = Modifier
) {
  ImportantCheckCounter(
    foundImportantChecks = locationState.foundImportantChecks.count { importantCheck ->
      state[importantCheck].consideredImportant
    },
    totalImportantChecks = locationState.totalImportantChecks,
    showFoundChecks = showFoundChecks,
    hintRevealed = locationState.hintRevealed,
    modifier = modifier
  )
}

@Composable fun ImportantCheckCounter(
  foundImportantChecks: Int,
  totalImportantChecks: Int,
  showFoundChecks: Boolean,
  hintRevealed: Boolean,
  modifier: Modifier = Modifier
) {
  Row(modifier.height(20.dp), verticalAlignment = Alignment.CenterVertically) {
    if (showFoundChecks) {
      if (hintRevealed && totalImportantChecks == 0) {
        // Nothing - want to just show the heartless symbol
      } else {
        Image(imageFromResource(imagesByNumber[foundImportantChecks]))
        Spacer(Modifier.width(4.dp))
        Image(imageFromResource("images/VerticalBar.png"), modifier = Modifier.rotate(20.0f))
        Spacer(Modifier.width(4.dp))
      }
    }

    if (hintRevealed) {
      when (totalImportantChecks) {
        0 -> Image(imageFromResource("images/Heartless.png"))
        foundImportantChecks -> Image(imageFromResource("images/Mickey_Head.png"))
        else -> Image(imageFromResource(imagesByNumber[totalImportantChecks]))
      }
    } else {
      Image(imageFromResource("images/QuestionMark.png"))
    }
  }
}
