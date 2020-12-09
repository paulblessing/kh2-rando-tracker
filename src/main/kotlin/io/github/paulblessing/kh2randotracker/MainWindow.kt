@file:OptIn(ExperimentalLayout::class)

package io.github.paulblessing.kh2randotracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.gesture.tapGestureFilter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.imageFromResource
import androidx.compose.ui.input.mouse.MouseScrollUnit
import androidx.compose.ui.input.mouse.mouseScrollFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import java.util.*

@Composable fun MainWindow(state: TrackerState, onSelectHintsFile: () -> Unit) {
  MaterialTheme(colors = darkColors()) {
    Surface(modifier = Modifier.fillMaxSize()) {
      if (state.hintsLoaded) {
        ScrollableColumn {
          FlowRow {
            for (world in state.allImportantCheckLocations.filter { it.enabled }) {
              TrackerImportantCheckLocation(world, state)
            }
          }

          FlowRow {
            for (importantCheck in state.unfoundImportantChecks.filterTo(TreeSet(compareBy { it.id })) { it.enabled }) {
              ImportantCheckIndicator(importantCheck, state)
            }
          }
        }
      } else {
        LoadHintsView(onSelectHintsFile)
      }
    }
  }
}

@Composable fun TrackerImportantCheckLocation(location: ImportantCheckLocation, state: TrackerState) {
  Row(
    Modifier.width(320.dp).border(1.dp, if (location.selected) Color.Blue else Color.Transparent),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(
      Modifier.size(width = 64.dp, height = 64.dp)
        .padding(horizontal = 8.dp, vertical = 8.dp)
        .tapGestureFilter {
          if (location == state.activeLocation) {
            location.selected = false
            state.activeLocation = null
          } else {
            state.activeLocation?.selected = false
            location.selected = true
            state.activeLocation = location
          }
        }
        .mouseScrollFilter { event, _ ->
          when (val delta = event.delta) {
            is MouseScrollUnit.Line -> {
              val adjustment = if (delta.value > 0.0f) -1 else 1
              location.totalImportantChecks = (location.totalImportantChecks + adjustment).coerceIn(-1, 20)
              true
            }
            is MouseScrollUnit.Page -> true
          }
        }
    ) {
      Image(imageFromResource(location.imagePath))
      ImportantCheckCounter(
        showFoundChecks = false,
        foundImportantChecks = location.foundImportantChecks,
        totalImportantChecks = location.totalImportantChecks,
        modifier = Modifier.align(Alignment.BottomEnd)
      )
    }

    Image(
      imageFromResource("images/VerticalBarWhite.png"),
      Modifier.size(width = 2.dp, height = 64.dp),
      contentScale = ContentScale.FillHeight
    )

    FlowRow {
      for (foundCheck in location.foundImportantChecks) {
        ImportantCheckIndicator(foundCheck, state)
      }
    }
  }
}

@Composable fun ImportantCheckIndicator(importantCheck: ImportantCheck, state: TrackerState) {
  Box(
    Modifier.size(width = 36.dp, height = 36.dp)
      .clickable {
        if (importantCheck in state.unfoundImportantChecks) {
          state.activeLocation?.let { location ->
            if (location.foundImportantChecks.size < 20) {
              location.foundImportantChecks += importantCheck
              state.unfoundImportantChecks -= importantCheck
            }
          }
        } else {
          state.allImportantCheckLocations.forEach { it.foundImportantChecks -= importantCheck }
          state.unfoundImportantChecks += importantCheck
        }
      }
  ) {
    val imagePath = when (importantCheck) {
      is AnsemReport -> {
        "images/simple/ansem_report${importantCheck.number}.png"
      }
      is Magic -> {
        "images/simple/${importantCheck.imageName}.png"
      }
      is DriveForm -> {
        "images/simple/${importantCheck.imageName}.png"
      }
      is ImportantAbility -> {
        "images/simple/${importantCheck.imageName}.png"
      }
      is TornPage -> {
        "images/old/torn_page.png"
      }
      is Summon -> {
        "images/simple/${importantCheck.imageName}.png"
      }
      is Proof -> {
        "images/simple/proof_of_${importantCheck.imageName}.png"
      }
      is PromiseCharm -> {
        "images/simple/promise_charm.png"
      }
    }
    Image(imageFromResource(imagePath))
  }

}
