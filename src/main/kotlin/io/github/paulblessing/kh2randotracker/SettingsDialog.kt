package io.github.paulblessing.kh2randotracker

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DesktopDialogProperties
import androidx.compose.ui.window.Dialog

@Composable fun SettingsDialog(
  growthAbilityMode: GrowthAbilityMode,
  onGrowthAbilityModeSelected: (GrowthAbilityMode) -> Unit,
  importantCheckLocationIconSet: String,
  onImportantCheckLocationIconSetSelected: (String) -> Unit,
  importantCheckIconSet: String,
  onImportantCheckIconSetSelected: (String) -> Unit,
  trackerWindowSizeClass: SizeClass,
  onTrackerWindowSizeClassSelected: (SizeClass) -> Unit,
  broadcastWindowSizeClass: SizeClass,
  onBroadcastWindowSizeClassSelected: (SizeClass) -> Unit,
  onDismissRequest: () -> Unit
) {
  Dialog(
    onDismissRequest,
    properties = DesktopDialogProperties(title = "Options", size = IntSize(600, 600))
  ) {
    MaterialTheme(colors = darkColors()) {
      Surface(Modifier.fillMaxSize()) {
        Column(Modifier.padding(16.dp)) {
          GrowthAbilityModeSetting(growthAbilityMode, onGrowthAbilityModeSelected)
          Spacer(Modifier.height(16.dp))
          IconSetSetting("Location icons", importantCheckLocationIconSet, onImportantCheckLocationIconSetSelected)
          Spacer(Modifier.height(16.dp))
          IconSetSetting("Item icons", importantCheckIconSet, onImportantCheckIconSetSelected)
          Spacer(Modifier.height(16.dp))
          WindowSizeSetting("Tracker window scale", trackerWindowSizeClass, onTrackerWindowSizeClassSelected)
          Spacer(Modifier.height(16.dp))
          WindowSizeSetting("Broadcast window scale", broadcastWindowSizeClass, onBroadcastWindowSizeClassSelected)
        }
      }
    }
  }
}

@Composable private fun GrowthAbilityModeSetting(
  growthAbilityMode: GrowthAbilityMode,
  onGrowthAbilityModeSelected: (GrowthAbilityMode) -> Unit
) {
  Text("Growth ability mode")
  Spacer(Modifier.height(8.dp))
  Row(verticalAlignment = Alignment.CenterVertically) {
    RadioButton(
      selected = growthAbilityMode == GrowthAbilityMode.TrackedToLocation,
      onClick = { onGrowthAbilityModeSelected(GrowthAbilityMode.TrackedToLocation) }
    )
    Text("Tracked to location", style = MaterialTheme.typography.caption)

    Spacer(Modifier.width(16.dp))

    RadioButton(
      selected = growthAbilityMode == GrowthAbilityMode.LevelOnly,
      onClick = { onGrowthAbilityModeSelected(GrowthAbilityMode.LevelOnly) }
    )
    Text("Level only", style = MaterialTheme.typography.caption)

    Spacer(Modifier.width(16.dp))

    RadioButton(
      selected = growthAbilityMode == GrowthAbilityMode.Off,
      onClick = { onGrowthAbilityModeSelected(GrowthAbilityMode.Off) }
    )
    Text("Off", style = MaterialTheme.typography.caption)
  }
}

@Composable private fun IconSetSetting(
  label: String,
  iconSetName: String,
  onIconSetNameSelected: (String) -> Unit,
) {
  Text(label)
  Spacer(Modifier.height(8.dp))
  Row(verticalAlignment = Alignment.CenterVertically) {
    RadioButton(
      selected = iconSetName == ICON_SET_SIMPLE,
      onClick = { onIconSetNameSelected(ICON_SET_SIMPLE) }
    )
    Text("Minimal", style = MaterialTheme.typography.caption)

    Spacer(Modifier.width(16.dp))

    RadioButton(
      selected = iconSetName == ICON_SET_CLASSIC,
      onClick = { onIconSetNameSelected(ICON_SET_CLASSIC) }
    )
    Text("Classic", style = MaterialTheme.typography.caption)
  }
}

@Composable private fun WindowSizeSetting(
  label: String,
  windowSizeClass: SizeClass,
  onWindowSizeClassSelected: (SizeClass) -> Unit,
) {
  Text(label)
  Spacer(Modifier.height(8.dp))

  Row(verticalAlignment = Alignment.CenterVertically) {
    RadioButton(
      selected = windowSizeClass == SizeClass.Small,
      onClick = { onWindowSizeClassSelected(SizeClass.Small) }
    )
    Text("Small", style = MaterialTheme.typography.caption)

    Spacer(Modifier.width(16.dp))

    RadioButton(
      selected = windowSizeClass == SizeClass.Normal,
      onClick = { onWindowSizeClassSelected(SizeClass.Normal) }
    )
    Text("Normal", style = MaterialTheme.typography.caption)

    Spacer(Modifier.width(16.dp))

    RadioButton(
      selected = windowSizeClass == SizeClass.Large,
      onClick = { onWindowSizeClassSelected(SizeClass.Large) }
    )
    Text("Large", style = MaterialTheme.typography.caption)
  }
}
