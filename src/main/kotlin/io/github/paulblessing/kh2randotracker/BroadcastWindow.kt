package io.github.paulblessing.kh2randotracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.imageFromResource
import androidx.compose.ui.unit.dp

@Composable fun BroadcastWindow(state: TrackerState, growthAbilityMode: GrowthAbilityMode) {
  Column(verticalArrangement = Arrangement.SpaceEvenly) {
    val shownLocationStates = state.importantCheckLocationStates.filter { it.location.broadcast && it.enabled }
    for (rowOfLocationStates in shownLocationStates.chunked(4)) {
      Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        for (locationState in rowOfLocationStates) {
          ImportantCheckLocationIndicator(locationState, state, width = scaledSize(80.dp), showFoundChecks = true)
        }
      }
    }

    CountSummariesRow(state)
    MagicRow(state)
    DriveFormRow(state)
    if (growthAbilityMode != GrowthAbilityMode.Off) {
      GrowthAbilityRow(state)
    }
    SummonRow(state)
    ProofsAndOthersRow(state)
  }
}

@Composable private fun CountSummariesRow(state: TrackerState) {
  val foundReports = AnsemReport.values().count { ansemReport -> state.importantCheckHasBeenFound(ansemReport) }
  val foundPages = TornPage.values().count { tornPage -> state.importantCheckHasBeenFound(tornPage) }
  Row(
    Modifier.height(36.dp).fillMaxWidth().padding(horizontal = 8.dp, vertical = 2.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    val iconSet = AmbientImportantCheckIconSet.current
    Row(verticalAlignment = Alignment.CenterVertically) {
      Image(imageFromResource(iconSet.icons.getValue(AnsemReport.Report1)))
      ReportOrPageCounter(found = foundReports, total = 13)
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
      Image(imageFromResource(iconSet.icons.getValue(TornPage.TornPage1)))
      ReportOrPageCounter(found = foundPages, total = 5)
    }
    ReportOrPageCounter(
      found = state.foundImportantChecksConsideredImportantCount,
      total = state.totalImportantChecksConsideredImportantCount
    )
  }
}

@Composable private fun ProofsAndOthersRow(state: TrackerState) {
  Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
    listOf(
      ImportantAbility.SecondChance,
      ImportantAbility.OnceMore,
      PromiseCharm,
      Proof.ProofOfNonexistence,
      Proof.ProofOfConnection,
      Proof.ProofOfPeace
    ).mapNotNull { importantCheck ->
      importantCheck.takeIf(state::showImportantCheck)
    }.forEach { importantCheck ->
      BroadcastOtherImportantCheckIndicator(importantCheck, state)
    }
  }
}

@Composable private fun MagicRow(state: TrackerState) {
  Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
    val items = listOf(
      Magic.fires,
      Magic.blizzards,
      Magic.thunders,
      Magic.cures,
      Magic.reflects,
      Magic.magnets,
    )
    for (item in items) {
      BroadcastMagicIndicator(item, state)
    }
  }
}

@Composable private fun DriveFormRow(state: TrackerState) {
  Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
    for (item in DriveForm.values()) {
      BroadcastDriveFormIndicator(item, state)
    }
  }
}

@Composable private fun GrowthAbilityRow(state: TrackerState) {
  Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
    val items = listOf(
      GrowthAbility.highJumps,
      GrowthAbility.quickRuns,
      GrowthAbility.dodgeRolls,
      GrowthAbility.aerialDodges,
      GrowthAbility.glides
    )
    for (item in items) {
      BroadcastGrowthAbilityIndicator(item, state)
    }
  }
}

@Composable private fun SummonRow(state: TrackerState) {
  Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
    for (item in Summon.values()) {
      BroadcastOtherImportantCheckIndicator(item, state)
    }
  }
}

@Composable private fun BroadcastMagicIndicator(magics: Set<Magic>, state: TrackerState) {
  val magicLevel = magics.count { magic -> state.importantCheckHasBeenFound(magic) }
  MagicIndicator(
    magic = magics.first(),
    magicLevel = magicLevel,
    imageAlpha = if (magicLevel > 0) 1.0f else 0.25f
  )
}

@Composable private fun BroadcastDriveFormIndicator(driveForm: DriveForm, state: TrackerState) {
  val driveFormState = state[driveForm]
  val driveAcquired = state.importantCheckHasBeenFound(driveForm)
  DriveFormIndicator(
    driveForm = driveForm,
    driveFormLevel = driveFormState.driveFormLevel,
    displayLevelZero = driveAcquired,
    imageAlpha = if (driveAcquired) 1.0f else 0.25f
  )
}

@Composable private fun BroadcastGrowthAbilityIndicator(growthAbilities: Set<GrowthAbility>, state: TrackerState) {
  val growthAbilityLevel = growthAbilities.count { growthAbility -> state.importantCheckHasBeenFound(growthAbility) }
  GrowthAbilityIndicator(
    growthAbility = growthAbilities.first(),
    growthAbilityLevel = growthAbilityLevel,
    displayLevelZero = false,
    imageAlpha = if (growthAbilityLevel > 0) 1.0f else 0.25f
  )
}

@Composable private fun BroadcastOtherImportantCheckIndicator(importantCheck: ImportantCheck, state: TrackerState) {
  OtherImportantCheckIndicator(
    importantCheck = importantCheck,
    imageAlpha = if (state.importantCheckHasBeenFound(importantCheck)) 1.0f else 0.25f
  )
}
