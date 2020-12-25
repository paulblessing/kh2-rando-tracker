package io.github.paulblessing.kh2randotracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.imageFromResource
import androidx.compose.ui.unit.dp

@Composable fun AnsemReportIndicator(
  ansemReport: AnsemReport,
  imageAlpha: Float = 1.0f,
  failedAttemptCount: Int = 0,
  onClick: (() -> Unit)? = null
) {
  ImportantCheckIndicator(
    importantCheck = ansemReport,
    imageAlpha = imageAlpha,
    onClick = onClick,
    topStart = {
      Row(Modifier.height(scaledSize(16.dp))) {
        repeat(failedAttemptCount) { Image(imageFromResource("images/FailX.png")) }
      }
    },
    bottomEnd = {
      Image(imageFromResource(imagesByNumber[ansemReport.number]), Modifier.size(scaledSize(16.dp)))
    }
  )
}

@Composable fun MagicIndicator(
  magic: Magic,
  magicLevel: Int,
  imageAlpha: Float = 1.0f
) {
  ImportantCheckIndicator(
    importantCheck = magic,
    imageAlpha = imageAlpha,
    bottomEnd = {
      if (magicLevel > 0) {
        Image(imageFromResource(imagesByNumber[magicLevel]), Modifier.size(scaledSize(16.dp)))
      }
    }
  )
}

@Composable fun DriveFormIndicator(
  driveForm: DriveForm,
  driveFormLevel: Int = 0,
  displayLevelZero: Boolean = false,
  imageAlpha: Float = 1.0f,
  displayHintedIcon: Boolean = false,
  onClick: (() -> Unit)? = null,
  onAdjustDriveFormLevel: ((adjustment: Int) -> Unit)? = null
) {
  ImportantCheckIndicator(
    importantCheck = driveForm,
    imageAlpha = imageAlpha,
    onClick = onClick,
    onScrollWheel = onAdjustDriveFormLevel,
    topStart = {
      if (displayHintedIcon) {
        Image(
          imageFromResource(AmbientImportantCheckIconSet.current.icons.getValue(AnsemReport.Report1)),
          Modifier.size(scaledSize(16.dp))
        )
      }
    },
    bottomEnd = {
      if (driveFormLevel > 0 || displayLevelZero) {
        Image(imageFromResource(imagesByNumber[driveFormLevel]), Modifier.size(scaledSize(16.dp)))
      }
    }
  )
}

@Composable fun GrowthAbilityIndicator(
  growthAbility: GrowthAbility,
  growthAbilityLevel: Int = 0,
  displayLevelZero: Boolean = false,
  imageAlpha: Float = 1.0f,
  onClick: (() -> Unit)? = null
) {
  ImportantCheckIndicator(
    importantCheck = growthAbility,
    imageAlpha = imageAlpha,
    onClick = onClick,
    bottomEnd = {
      if (growthAbilityLevel > 0 || displayLevelZero) {
        Image(imageFromResource(imagesByNumber[growthAbilityLevel]), Modifier.size(scaledSize(16.dp)))
      }
    }
  )
}

@Composable fun OtherImportantCheckIndicator(
  importantCheck: ImportantCheck,
  imageAlpha: Float = 1.0f,
  displayHintedIcon: Boolean = false,
  onClick: (() -> Unit)? = null
) {
  ImportantCheckIndicator(
    importantCheck = importantCheck,
    imageAlpha = imageAlpha,
    onClick = onClick,
    topStart = {
      if (displayHintedIcon) {
        Image(
          imageFromResource(AmbientImportantCheckIconSet.current.icons.getValue(AnsemReport.Report1)),
          Modifier.size(scaledSize(16.dp))
        )
      }
    },
  )
}

@Composable private fun ImportantCheckIndicator(
  importantCheck: ImportantCheck,
  imageAlpha: Float = 1.0f,
  onClick: (() -> Unit)? = null,
  onScrollWheel: ((adjustment: Int) -> Unit)? = null,
  topStart: @Composable (() -> Unit)? = null,
  bottomEnd: @Composable (() -> Unit)? = null
) {
  Box(
    Modifier.size(scaledSize(36.dp))
      .let { modifier ->
        if (onClick == null) modifier else modifier.clickable { onClick() }
      }
      .let { modifier ->
        if (onScrollWheel == null) {
          modifier
        } else {
          modifier.scrollWheelAdjustable { adjustment -> onScrollWheel(adjustment) }
        }
      }
      .padding(2.dp)
  ) {
    val iconSet = AmbientImportantCheckIconSet.current
    Image(imageFromResource(iconSet.icons.getValue(importantCheck)), Modifier.fillMaxSize(), alpha = imageAlpha)
    if (topStart != null) {
      Box(Modifier.align(Alignment.TopStart)) { topStart() }
    }
    if (bottomEnd != null) {
      Box(Modifier.align(Alignment.BottomEnd)) { bottomEnd() }
    }
  }

}
