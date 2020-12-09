@file:OptIn(ExperimentalLayout::class)

package io.github.paulblessing.kh2randotracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.imageFromResource
import androidx.compose.ui.unit.dp

@Composable fun ImportantCheckCounter(
  showFoundChecks: Boolean,
  foundImportantChecks: Set<ImportantCheck>,
  totalImportantChecks: Int,
  modifier: Modifier = Modifier
) {
  Row(modifier.height(24.dp), verticalAlignment = Alignment.CenterVertically) {
    if (showFoundChecks) {
      Image(imageFromResource(imagesByNumber[foundImportantChecks.size]))
      Spacer(Modifier.width(4.dp))
      Image(imageFromResource("images/VerticalBar.png"), modifier = Modifier.rotate(20.0f))
      Spacer(Modifier.width(4.dp))
    }
    when {
      totalImportantChecks == 0 -> {
        Image(imageFromResource("images/QuestionMark.png"))
      }
      totalImportantChecks < 0 -> {
        Image(imageFromResource("images/Heartless.png"))
      }
      totalImportantChecks == foundImportantChecks.size -> {
        Image(imageFromResource("images/Mickey_Head.png"))
      }
      else -> {
        Image(imageFromResource(imagesByNumber[totalImportantChecks]))
      }
    }
  }
}
