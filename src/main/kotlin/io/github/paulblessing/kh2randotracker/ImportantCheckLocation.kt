package io.github.paulblessing.kh2randotracker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed class ImportantCheckLocation(val imagePath: String, val broadcast: Boolean, defaultEnabled: Boolean) {

  var enabled: Boolean by mutableStateOf(defaultEnabled)
  var selected: Boolean by mutableStateOf(false)

  var totalImportantChecks: Int by mutableStateOf(0)
  var foundImportantChecks: Set<ImportantCheck> by mutableStateOf(emptySet())

}

class World(
  imagePath: String,
  defaultEnabled: Boolean = true
) : ImportantCheckLocation(imagePath, broadcast = true, defaultEnabled = defaultEnabled) {

}

class Other(
  imagePath: String,
  broadcast: Boolean = true,
  defaultEnabled: Boolean = true
) : ImportantCheckLocation(imagePath, broadcast = broadcast, defaultEnabled = defaultEnabled) {

}
