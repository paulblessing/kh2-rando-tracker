@file:OptIn(ExperimentalLayout::class)

package io.github.paulblessing.kh2randotracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.imageFromResource
import androidx.compose.ui.unit.dp

@Composable fun BroadcastWindow(state: TrackerState, onSelectHintsFile: () -> Unit) {
  MaterialTheme(colors = darkColors()) {
    Surface(modifier = Modifier.fillMaxSize()) {
      if (state.hintsLoaded) {
        ScrollableColumn {
          FlowRow(mainAxisAlignment = MainAxisAlignment.Center) {
            for (world in state.allImportantCheckLocations.filter { it.enabled && it.broadcast }) {
              BroadcastImportantCheckLocation(world)
            }
          }

          Spacer(Modifier.height(16.dp))

          BroadcastReportAndPageCounter(state)

          Spacer(Modifier.height(16.dp))

          Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            val items = listOf(
              state.ProofOfPeace,
              state.ProofOfNonexistence,
              state.ProofOfConnection,
              state.PromiseCharm,
              state.SecondChance,
              state.OnceMore
            ).filter { it.enabled }
            for (item in items) {
              BroadcastItemGotIndicator(item, state)
            }
          }

          Spacer(Modifier.height(8.dp))

          Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            val items = listOf(
              setOf(state.Fire1, state.Fire2, state.Fire3),
              setOf(state.Blizzard1, state.Blizzard2, state.Blizzard3),
              setOf(state.Thunder1, state.Thunder2, state.Thunder3),
              setOf(state.Cure1, state.Cure2, state.Cure3),
              setOf(state.Reflect1, state.Reflect2, state.Reflect3),
              setOf(state.Magnet1, state.Magnet2, state.Magnet3),
            )
            for (item in items) {
              BroadcastMagicGotIndicator(item, state)
            }
          }
          Spacer(Modifier.height(8.dp))

          Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            val items = listOf(
              state.ValorForm,
              state.WisdomForm,
              state.LimitForm,
              state.MasterForm,
              state.FinalForm
            )
            for (item in items) {
              BroadcastFormGotIndicator(item, state)
            }
          }
          Spacer(Modifier.height(8.dp))

          Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            val items = listOf(
              state.Genie,
              state.Stitch,
              state.ChickenLittle,
              state.PeterPan
            )
            for (item in items) {
              BroadcastSummonGotIndicator(item, state)
            }
          }
        }
      } else {
        LoadHintsView(onSelectHintsFile)
      }
    }
  }
}

@Composable fun BroadcastImportantCheckLocation(location: ImportantCheckLocation) {
  Box(
    Modifier.size(width = 80.dp, height = 64.dp)
//      .border(1.dp, Color.Red)
      .padding(horizontal = 8.dp, vertical = 8.dp)
  ) {
    Image(imageFromResource(location.imagePath))
    ImportantCheckCounter(
      showFoundChecks = true,
      foundImportantChecks = location.foundImportantChecks,
      totalImportantChecks = location.totalImportantChecks,
      modifier = Modifier.align(Alignment.BottomEnd)
    )
  }
}

@Composable fun BroadcastReportAndPageCounter(state: TrackerState) {
  val foundReports = state.allAnsemReports - state.unfoundImportantChecks
  val foundPages = state.allTornPages - state.unfoundImportantChecks
  Row(
    Modifier.height(64.dp)
      .fillMaxWidth()
//      .border(1.dp, Color.Red)
      .padding(horizontal = 8.dp, vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center,
  ) {
    Image(imageFromResource("images/simple/ansem_report.png"))
    ImportantCheckCounter(
      showFoundChecks = true,
      foundImportantChecks = foundReports,
      totalImportantChecks = 13
    )

    Spacer(Modifier.width(48.dp))

    Image(imageFromResource("images/old/torn_page.png"))
    ImportantCheckCounter(
      showFoundChecks = true,
      foundImportantChecks = foundPages,
      totalImportantChecks = 5
    )
  }
}

@Composable fun BroadcastItemGotIndicator(importantCheck: ImportantCheck, state: TrackerState) {
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

  val alpha = if (importantCheck in state.unfoundImportantChecks) 0.25f else 1.0f

  Image(imageFromResource(imagePath), Modifier.height(36.dp), alpha = alpha)
}

@Composable fun BroadcastMagicGotIndicator(magics: Set<Magic>, state: TrackerState) {
  val imagePath = "images/simple/${magics.first().imageName}.png"

  val count = (magics - state.unfoundImportantChecks).size
  val alpha = if (count == 0) 0.25f else 1.0f

  Box(Modifier.height(36.dp)) {
    Image(imageFromResource(imagePath), alpha = alpha)
    if (count > 1) {
      Image(imageFromResource(imagesByNumber[count]), Modifier.height(16.dp).align(Alignment.BottomEnd))
    }
  }
}

@Composable fun BroadcastFormGotIndicator(form: DriveForm, state: TrackerState) {
  val imagePath = "images/simple/${form.imageName}.png"

  val alpha = if (form in state.unfoundImportantChecks) 0.25f else 1.0f

  Image(imageFromResource(imagePath), Modifier.height(36.dp), alpha = alpha)
}

@Composable fun BroadcastSummonGotIndicator(summon: Summon, state: TrackerState) {
  val imagePath = "images/simple/${summon.imageName}.png"

  val alpha = if (summon in state.unfoundImportantChecks) 0.25f else 1.0f

  Image(imageFromResource(imagePath), Modifier.height(36.dp), alpha = alpha)
}
