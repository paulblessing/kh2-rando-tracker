package io.github.paulblessing.kh2randotracker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed class ImportantCheck(val id: Int, defaultEnabled: Boolean) {

  var enabled: Boolean by mutableStateOf(defaultEnabled)

}

class AnsemReport(
  id: Int,
  val number: Int,
  defaultEnabled: Boolean = true
) : ImportantCheck(id, defaultEnabled) {

}

class Magic(
  id: Int,
  val imageName: String,
  defaultEnabled: Boolean = true
) : ImportantCheck(id, defaultEnabled) {

}

class DriveForm(
  id: Int,
  val imageName: String,
  defaultEnabled: Boolean = true
) : ImportantCheck(id, defaultEnabled) {

}

class ImportantAbility(
  id: Int,
  val imageName: String,
  defaultEnabled: Boolean = true
) : ImportantCheck(id, defaultEnabled) {

}

class TornPage(
  id: Int,
  val number: Int,
  defaultEnabled: Boolean = true
) : ImportantCheck(id, defaultEnabled) {

}

class Summon(
  id: Int,
  val imageName: String,
  defaultEnabled: Boolean = true
) : ImportantCheck(id, defaultEnabled) {

}

class Proof(
  id: Int,
  val imageName: String,
  defaultEnabled: Boolean = true
) : ImportantCheck(id, defaultEnabled) {

}

class PromiseCharm(id: Int, defaultEnabled: Boolean = true) : ImportantCheck(id, defaultEnabled) {

}
