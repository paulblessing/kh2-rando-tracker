package io.github.paulblessing.kh2randotracker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

interface ImportantCheck {

  val id: Int

}

enum class AnsemReport(override val id: Int, val number: Int) : ImportantCheck {

  Report1(id = 101, number = 1),
  Report2(id = 102, number = 2),
  Report3(id = 103, number = 3),
  Report4(id = 104, number = 4),
  Report5(id = 105, number = 5),
  Report6(id = 106, number = 6),
  Report7(id = 107, number = 7),
  Report8(id = 108, number = 8),
  Report9(id = 109, number = 9),
  Report10(id = 110, number = 10),
  Report11(id = 111, number = 11),
  Report12(id = 112, number = 12),
  Report13(id = 113, number = 13)

}

enum class Magic(override val id: Int) : ImportantCheck {

  Fire1(id = 200),
  Fire2(id = 201),
  Fire3(id = 202),
  Blizzard1(id = 210),
  Blizzard2(id = 211),
  Blizzard3(id = 212),
  Thunder1(id = 220),
  Thunder2(id = 221),
  Thunder3(id = 222),
  Cure1(id = 230),
  Cure2(id = 231),
  Cure3(id = 232),
  Reflect1(id = 240),
  Reflect2(id = 241),
  Reflect3(id = 242),
  Magnet1(id = 250),
  Magnet2(id = 251),
  Magnet3(id = 252);

  companion object {

    val fires: Set<Magic>
      get() = setOf(Fire1, Fire2, Fire3)

    val blizzards: Set<Magic>
      get() = setOf(Blizzard1, Blizzard2, Blizzard3)

    val thunders: Set<Magic>
      get() = setOf(Thunder1, Thunder2, Thunder3)

    val cures: Set<Magic>
      get() = setOf(Cure1, Cure2, Cure3)

    val reflects: Set<Magic>
      get() = setOf(Reflect1, Reflect2, Reflect3)

    val magnets: Set<Magic>
      get() = setOf(Magnet1, Magnet2, Magnet3)

  }

}

enum class DriveForm(override val id: Int) : ImportantCheck {

  ValorForm(id = 300),
  WisdomForm(id = 301),
  LimitForm(id = 302),
  MasterForm(id = 303),
  FinalForm(id = 304)

}

enum class GrowthAbility(override val id: Int) : ImportantCheck {

  HighJump1(id = 350),
  HighJump2(id = 351),
  HighJump3(id = 352),
  HighJump4(id = 353),
  QuickRun1(id = 360),
  QuickRun2(id = 361),
  QuickRun3(id = 362),
  QuickRun4(id = 363),
  DodgeRoll1(id = 370),
  DodgeRoll2(id = 371),
  DodgeRoll3(id = 372),
  DodgeRoll4(id = 373),
  AerialDodge1(id = 380),
  AerialDodge2(id = 381),
  AerialDodge3(id = 382),
  AerialDodge4(id = 383),
  Glide1(id = 390),
  Glide2(id = 391),
  Glide3(id = 392),
  Glide4(id = 393);

  companion object {

    val highJumps: Set<GrowthAbility>
      get() = setOf(HighJump1, HighJump2, HighJump3, HighJump4)

    val quickRuns: Set<GrowthAbility>
      get() = setOf(QuickRun1, QuickRun2, QuickRun3, QuickRun4)

    val dodgeRolls: Set<GrowthAbility>
      get() = setOf(DodgeRoll1, DodgeRoll2, DodgeRoll3, DodgeRoll4)

    val aerialDodges: Set<GrowthAbility>
      get() = setOf(AerialDodge1, AerialDodge2, AerialDodge3, AerialDodge4)

    val glides: Set<GrowthAbility>
      get() = setOf(Glide1, Glide2, Glide3, Glide4)

  }

}

enum class ImportantAbility(override val id: Int) : ImportantCheck {

  SecondChance(id = 400),
  OnceMore(id = 401)

}

enum class TornPage(override val id: Int) : ImportantCheck {

  TornPage1(id = 500),
  TornPage2(id = 501),
  TornPage3(id = 502),
  TornPage4(id = 503),
  TornPage5(id = 504)

}

enum class Summon(override val id: Int) : ImportantCheck {

  ChickenLittle(id = 600),
  Genie(id = 601),
  Stitch(id = 602),
  PeterPan(id = 603)

}

enum class Proof(override val id: Int) : ImportantCheck {

  ProofOfNonexistence(id = 700),
  ProofOfConnection(id = 701),
  ProofOfPeace(id = 702)

}

object PromiseCharm : ImportantCheck {

  override val id: Int
    get() = 800

}

sealed class ImportantCheckState(val importantCheck: ImportantCheck, val consideredImportant: Boolean) {

  @delegate:Transient
  var found: Boolean by mutableStateOf(false)

  @delegate:Transient
  var hinted: Boolean by mutableStateOf(false)

}

@JsonClass(generateAdapter = true)
class AnsemReportState(
  @Json(name = "r") val ansemReport: AnsemReport,
  @Json(name = "imp") consideredImportant: Boolean,
  @Json(name = "rl") val reportLocation: ImportantCheckLocation,
  @Json(name = "hl") val hintedLocation: ImportantCheckLocation,
  @Json(name = "icc") @ImportantCheckCount val importantCheckCount: Int
) : ImportantCheckState(ansemReport, consideredImportant) {

  @Json(name = "fat") var failedAttempts: Int by mutableStateOf(0)

  val lockedOut: Boolean
    get() = failedAttempts >= 3

  val hintText: String
    get() {
      val locationDisplayName = hintedLocation.displayName
      return when (val count = importantCheckCount) {
        0 -> "$locationDisplayName is a heartless choice."
        1 -> "$locationDisplayName has 1 important check."
        else -> "$locationDisplayName has $count important checks."
      }
    }

}

@JsonClass(generateAdapter = true)
class DriveFormState(
  @Json(name = "df") val driveForm: DriveForm,
  @Json(name = "imp") consideredImportant: Boolean
) : ImportantCheckState(driveForm, consideredImportant) {

  @Json(name = "l") var driveFormLevel: Int by mutableStateOf(0)

}

@JsonClass(generateAdapter = true)
class OtherImportantCheckState(
  @Json(name = "ic") importantCheck: ImportantCheck,
  @Json(name = "imp") consideredImportant: Boolean
) : ImportantCheckState(importantCheck, consideredImportant)
