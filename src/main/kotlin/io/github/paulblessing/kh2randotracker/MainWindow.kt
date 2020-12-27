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

@Composable fun MainWindow(state: TrackerState, growthAbilityMode: GrowthAbilityMode) {
  ScrollableColumn(verticalArrangement = Arrangement.SpaceEvenly) {
    FlowRow {
      for (locationState in state.importantCheckLocationStates.filter { it.enabled }) {
        ImportantCheckLocationSection(locationState, state, growthAbilityMode = growthAbilityMode)
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

    val fires = Magic.fires
    val blizzards = Magic.blizzards
    val thunders = Magic.thunders
    AvailableCheckRow(DriveForm.values().toList() + fires + blizzards + thunders, state)

    val cures = Magic.cures
    val reflects = Magic.reflects
    val magnets = Magic.magnets

    if (growthAbilityMode == GrowthAbilityMode.Off) {
      AvailableCheckRow(TornPage.values().toList() + cures + reflects + magnets, state)
      AvailableCheckRow(Summon.values().toList() + ImportantAbility.values() + PromiseCharm + Proof.values(), state)
    } else {
      AvailableCheckRow(GrowthAbility.values().toList() + cures + reflects + magnets, state)
      AvailableCheckRow(
        TornPage.values().toList() + Summon.values() + ImportantAbility.values() + PromiseCharm + Proof.values(),
        state
      )
    }
  }
}

@Composable fun ImportantCheckLocationSection(
  locationState: ImportantCheckLocationState,
  state: TrackerState,
  growthAbilityMode: GrowthAbilityMode
) {
  val location = locationState.location
  val activeLocation = location == state.activeLocation
  Box(Modifier.width(scaledSize(320.dp))) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      ImportantCheckLocationIndicator(
        locationState,
        state,
        width = scaledSize(80.dp),
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
          if (adjustment > 0 && currentProgression < location.progressionImages.size + 1) {
            locationState.progression += adjustment
          } else if (adjustment < 0 && currentProgression > 0) {
            locationState.progression += adjustment
          }
        }
      )

      Image(
        imageFromResource("images/VerticalBarWhite.png"),
        Modifier.size(width = 2.dp, height = scaledSize(64.dp)),
        contentScale = ContentScale.FillHeight
      )

      FlowRow {
        for (foundCheck in locationState.foundImportantChecks) {
          when (foundCheck) {
            is AnsemReport -> FoundAnsemReportIndicator(foundCheck, state)
            is DriveForm -> FoundDriveFormIndicator(foundCheck, state)
            is GrowthAbility -> {
              if (growthAbilityMode == GrowthAbilityMode.TrackedToLocation) {
                FoundOtherImportantCheckIndicator(foundCheck, state)
              }
            }
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
          is GrowthAbility -> {
            when (importantCheck) {
              GrowthAbility.HighJump1 -> AvailableGrowthAbilityIndicator(GrowthAbility.highJumps, state)
              GrowthAbility.QuickRun1 -> AvailableGrowthAbilityIndicator(GrowthAbility.quickRuns, state)
              GrowthAbility.DodgeRoll1 -> AvailableGrowthAbilityIndicator(GrowthAbility.dodgeRolls, state)
              GrowthAbility.AerialDodge1 -> AvailableGrowthAbilityIndicator(GrowthAbility.aerialDodges, state)
              GrowthAbility.Glide1 -> AvailableGrowthAbilityIndicator(GrowthAbility.glides, state)
            }
          }
          is TornPage -> AvailableTornPageIndicator(importantCheck, state)
          else -> AvailableOtherImportantCheckIndicator(importantCheck, state)
        }
      }
    }
  }
}

@Composable private fun AvailableAnsemReportIndicator(ansemReport: AnsemReport, state: TrackerState) {
  val ansemReportState = state[ansemReport]
  val found = state.importantCheckHasBeenFound(ansemReport)
  AnsemReportIndicator(
    ansemReport = ansemReport,
    hintedLocation = ansemReportState.hintedLocation.takeIf { found },
    imageAlpha = if (found) 0.25f else 1.0f,
    failedAttemptCount = ansemReportState.failedAttempts,
    displayHintedIcon = ansemReportState.hinted,
    onClick = {
      state.attemptToAddImportantCheck(
        ansemReport,
        onAllowed = {
          val hintedLocation = ansemReportState.hintedLocation
          val hintedLocationState = state[hintedLocation]
          hintedLocationState.hintRevealed = true
          for (importantCheck in hintedLocationState.foundImportantChecks) {
            state[importantCheck].hinted = true
          }
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
    displayHintedIcon = driveFormState.hinted,
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

@Composable private fun AvailableGrowthAbilityIndicator(growthAbilities: Set<GrowthAbility>, state: TrackerState) {
  val growthAbilityStates = growthAbilities.map { growthAbility -> state[growthAbility] }
  val growthAbilityLevel = growthAbilityStates.count { it.found }
  GrowthAbilityIndicator(
    growthAbility = growthAbilities.first(),
    growthAbilityLevel = growthAbilityLevel,
    imageAlpha = if (growthAbilityLevel == 4) 0.25f else 1.0f,
    displayLevelZero = true,
    onClick = {
      val firstUnfound = growthAbilityStates.firstOrNull { !it.found }
      if (firstUnfound != null) {
        state.attemptToAddImportantCheck(firstUnfound.importantCheck)
      }
    }
  )
}

@Composable private fun AvailableTornPageIndicator(tornPage: TornPage, state: TrackerState) {
  val tornPageState = state[tornPage]
  OtherImportantCheckIndicator(
    importantCheck = tornPage,
    imageAlpha = if (state.importantCheckHasBeenFound(tornPage)) 0.25f else 1.0f,
    displayHintedIcon = tornPageState.hinted,
    onClick = { state.attemptToAddImportantCheck(tornPage) }
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
    hintedLocation = ansemReportState.hintedLocation,
    displayHintedIcon = ansemReportState.hinted,
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
