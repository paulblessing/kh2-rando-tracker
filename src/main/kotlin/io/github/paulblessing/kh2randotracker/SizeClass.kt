package io.github.paulblessing.kh2randotracker

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

enum class SizeClass(val scale: Float) {

  Small(scale = 0.75f),
  Normal(scale = 1.0f),
  Large(scale = 1.25f)

}

@Composable fun scaledSize(baseSize: Dp): Dp = baseSize * AmbientIconScale.current
