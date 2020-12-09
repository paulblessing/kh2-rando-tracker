package io.github.paulblessing.kh2randotracker

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.mouse.MouseScrollUnit
import androidx.compose.ui.input.mouse.mouseScrollFilter

fun Modifier.scrollWheelAdjustable(onScrollWheel: (adjustment: Int) -> Unit): Modifier {
  return mouseScrollFilter { event, _ ->
    when (val delta = event.delta) {
      is MouseScrollUnit.Line -> {
        val adjustment = if (delta.value > 0.0f) -1 else 1
        onScrollWheel(adjustment)
        true
      }
      is MouseScrollUnit.Page -> false
    }
  }
}
