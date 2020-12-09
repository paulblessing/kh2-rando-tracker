package io.github.paulblessing.kh2randotracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.imageFromResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable fun MainWindow(state: TrackerState) {
  ScrollableColumn(verticalArrangement = Arrangement.SpaceEvenly) {
    FlowRow {
      for (locationState in state.importantCheckLocationStates.filter { it.enabled }) {
        ImportantCheckLocationSection(locationState, state)
      }
    }

    Row(
      Modifier.fillMaxWidth().padding(horizontal = 16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      val lastRevealedAnsemReport = state.lastRevealedAnsemReport
      if (lastRevealedAnsemReport == null) {
        Spacer(Modifier.weight(1.0f))
      } else {
        Text(lastRevealedAnsemReport.hintText, Modifier.weight(1.0f))
      }

      ReportOrPageCounter(
        found = state.foundImportantChecksConsideredImportantCount,
        total = state.totalImportantChecksConsideredImportantCount
      )
    }

    AvailableCheckRow(AnsemReport.values().toList(), state)

    val fires = listOf(Magic.Fire1, Magic.Fire2, Magic.Fire3)
    val blizzards = listOf(Magic.Blizzard1, Magic.Blizzard2, Magic.Blizzard3)
    val thunders = listOf(Magic.Thunder1, Magic.Thunder2, Magic.Thunder3)
    AvailableCheckRow(DriveForm.values().toList() + fires + blizzards + thunders, state)

    val cures = listOf(Magic.Cure1, Magic.Cure2, Magic.Cure3)
    val reflects = listOf(Magic.Reflect1, Magic.Reflect2, Magic.Reflect3)
    val magnets = listOf(Magic.Magnet1, Magic.Magnet2, Magic.Magnet3)
    AvailableCheckRow(cures + reflects + magnets + Summon.values(), state)

    AvailableCheckRow(TornPage.values().toList() + ImportantAbility.values() + PromiseCharm + Proof.values(), state)
  }
}

@Composable fun ImportantCheckLocationSection(locationState: ImportantCheckLocationState, state: TrackerState) {
  val location = locationState.location
  val activeLocation = location == state.activeLocation
  Box(Modifier.width(320.dp)) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      ImportantCheckLocationIndicator(
        locationState,
        state,
        width = 80.dp,
        showFoundChecks = true,
        onClick = {
          if (activeLocation) {
            state.activeLocation = null
          } else {
            if (locationState.progression == 0) {
              locationState.progression = 1
            }
            state.activeLocation = location
          }
          state.activeLocation = if (activeLocation) null else location
        },
        onScrollWheel = { adjustment ->
          val currentProgression = locationState.progression
          // TODO: Use the progression images
//          if (adjustment > 0 && currentProgression < location.progressionImages.size) {
//            locationState.progression += adjustment
//          } else if (adjustment < 0 && currentProgression > 0) {
//            locationState.progression += adjustment
//          }
          if (adjustment > 0 && currentProgression == 0) {
            locationState.progression = 1
          } else if (adjustment < 0 && currentProgression == 1) {
            locationState.progression = 0
          }
        }
      )

      Image(
        imageFromResource("images/VerticalBarWhite.png"),
        Modifier.size(width = 2.dp, height = 64.dp),
        contentScale = ContentScale.FillHeight
      )

      FlowRow {
        for (foundCheck in locationState.foundImportantChecks) {
          when (foundCheck) {
            is AnsemReport -> FoundAnsemReportIndicator(foundCheck, state)
            is DriveForm -> FoundDriveFormIndicator(foundCheck, state)
            else -> FoundOtherImportantCheckIndicator(foundCheck, state)
          }
        }
      }
    }

    if (activeLocation) {
      Box(
        Modifier.matchParentSize().background(
          MaterialTheme.colors.secondary.copy(alpha = 0.1f),
          shape = RoundedCornerShape(CornerSize(16.dp))
        )
      )
    }
  }
}

@Composable private fun AvailableCheckRow(importantChecks: List<ImportantCheck>, state: TrackerState) {
  Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
    importantChecks.forEach { importantCheck ->
      if (state.showImportantCheck(importantCheck)) {
        when (importantCheck) {
          is AnsemReport -> AvailableAnsemReportIndicator(importantCheck, state)
          is DriveForm -> AvailableDriveFormIndicator(importantCheck, state)
          else -> AvailableOtherImportantCheckIndicator(importantCheck, state)
        }
      }
    }
  }
}

@Composable private fun AvailableAnsemReportIndicator(ansemReport: AnsemReport, state: TrackerState) {
  val ansemReportState = state[ansemReport]
  AnsemReportIndicator(
    ansemReport = ansemReport,
    imageAlpha = if (state.importantCheckHasBeenFound(ansemReport)) 0.25f else 1.0f,
    failedAttemptCount = ansemReportState.failedAttempts,
    onClick = {
      state.attemptToAddImportantCheck(
        ansemReport,
        onAllowed = {
          val hintedLocation = ansemReportState.hintedLocation
          val hintedLocationState = state[hintedLocation]
          hintedLocationState.hintRevealed = true
          state.lastRevealedAnsemReport = ansemReportState
        },
        onDisallowed = { reason ->
          if (reason == "incorrect location") {
            ansemReportState.failedAttempts++
          }
        },
        checkLocation = { locationState ->
          if (ansemReportState.lockedOut) {
            "locked out"
          } else {
            if (ansemReportState.reportLocation == locationState.location) {
              null
            } else {
              "incorrect location"
            }
          }
        }
      )
    }
  )
}

@Composable private fun AvailableDriveFormIndicator(driveForm: DriveForm, state: TrackerState) {
  val driveFormState = state[driveForm]
  DriveFormIndicator(
    driveForm = driveForm,
    driveFormLevel = driveFormState.driveFormLevel,
    imageAlpha = if (state.importantCheckHasBeenFound(driveForm)) 0.25f else 1.0f,
    displayLevelZero = true,
    onClick = {
      state.attemptToAddImportantCheck(
        driveForm,
        onAllowed = {
          if (driveFormState.driveFormLevel == 0) {
            driveFormState.driveFormLevel = 1
          }
        }
      )
    },
    onAdjustDriveFormLevel = { adjustment ->
      driveFormState.driveFormLevel = (driveFormState.driveFormLevel + adjustment).coerceIn(0, 7)
    }
  )
}

@Composable private fun AvailableOtherImportantCheckIndicator(importantCheck: ImportantCheck, state: TrackerState) {
  OtherImportantCheckIndicator(
    importantCheck = importantCheck,
    imageAlpha = if (state.importantCheckHasBeenFound(importantCheck)) 0.25f else 1.0f,
    onClick = { state.attemptToAddImportantCheck(importantCheck) }
  )
}

@Composable private fun FoundAnsemReportIndicator(ansemReport: AnsemReport, state: TrackerState) {
  val ansemReportState = state[ansemReport]
  AnsemReportIndicator(
    ansemReport,
    onClick = { state.lastRevealedAnsemReport = ansemReportState }
  )
}

@Composable private fun FoundDriveFormIndicator(driveForm: DriveForm, state: TrackerState) {
  val driveFormState = state[driveForm]
  DriveFormIndicator(
    driveForm = driveForm,
    driveFormLevel = driveFormState.driveFormLevel,
    displayLevelZero = true,
    onClick = { state.removeImportantCheck(driveForm) },
    onAdjustDriveFormLevel = { adjustment ->
      driveFormState.driveFormLevel = (driveFormState.driveFormLevel + adjustment).coerceIn(0, 7)
    }
  )
}

@Composable private fun FoundOtherImportantCheckIndicator(importantCheck: ImportantCheck, state: TrackerState) {
  OtherImportantCheckIndicator(
    importantCheck = importantCheck,
    onClick = { state.removeImportantCheck(importantCheck) }
  )
}
