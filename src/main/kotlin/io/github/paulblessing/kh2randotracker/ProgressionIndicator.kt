package io.github.paulblessing.kh2randotracker

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable fun ProgressionIndicator(locationState: ImportantCheckLocationState, modifier: Modifier) {
  Row(modifier.height(24.dp)) {
    // TODO: Use the progression images
//    val progression = locationState.progression
//    if (progression > 0) {
//      val progressionImage = locationState.location.progressionImages.getOrNull(progression - 1)
//      if (progressionImage != null) {
//        Image(imageFromResource(progressionImage))
//      }
//    }
  }
}