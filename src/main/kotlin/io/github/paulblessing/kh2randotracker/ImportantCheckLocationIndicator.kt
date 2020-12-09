package io.github.paulblessing.kh2randotracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.imageFromResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable fun ImportantCheckLocationIndicator(
  locationState: ImportantCheckLocationState,
  state: TrackerState,
  width: Dp = 64.dp,
  showFoundChecks: Boolean = false,
  onClick: (() -> Unit)? = null,
  onScrollWheel: ((adjustment: Int) -> Unit)? = null
) {
  val location = locationState.location
  val staticProgression = location.staticProgression
  Box(
    Modifier.size(width = width, height = 64.dp)
      .let { modifier ->
        if (onClick == null) modifier else modifier.clickable(indication = null) { onClick() }
      }
      .let { modifier ->
        if (onScrollWheel == null || staticProgression) {
          modifier
        } else {
          modifier.scrollWheelAdjustable { adjustment -> onScrollWheel(adjustment) }
        }
      }
      .padding(8.dp)
  ) {
    val iconSet = AmbientImportantCheckLocationIconSet.current
    Image(
      imageFromResource(iconSet.icons.getValue(location)),
      Modifier.size(48.dp),
      alpha = when {
        staticProgression -> 1.0f
        locationState.progression == 0 -> 0.25f
        else -> 1.0f
      }
    )

    ProgressionIndicator(locationState, Modifier.align(Alignment.TopStart))

    if (location != ImportantCheckLocation.GardenOfAssemblage) {
      ImportantCheckCounter(
        state,
        locationState,
        showFoundChecks = showFoundChecks,
        modifier = Modifier.align(Alignment.BottomEnd)
      )
    }
  }
}
