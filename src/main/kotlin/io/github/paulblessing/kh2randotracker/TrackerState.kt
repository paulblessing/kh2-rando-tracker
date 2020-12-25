package io.github.paulblessing.kh2randotracker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class TrackerState(
  @Json(name = "icls") val importantCheckLocationStates: List<ImportantCheckLocationState>,
  @Json(name = "ars") val ansemReportStates: List<AnsemReportState>,
  @Json(name = "dfs") val driveFormStates: List<DriveFormState>,
  @Json(name = "oics") val otherImportantCheckStates: List<OtherImportantCheckState>
) {

  @Transient
  private val importantCheckLocationStatesByLocation: Map<ImportantCheckLocation, ImportantCheckLocationState> =
    importantCheckLocationStates.associateBy(ImportantCheckLocationState::location)

  @Transient
  private val importantCheckStatesByCheck: Map<ImportantCheck, ImportantCheckState> = run {
    val result = mutableMapOf<ImportantCheck, ImportantCheckState>()
    ansemReportStates.associateByTo(result, AnsemReportState::ansemReport)
    driveFormStates.associateByTo(result, DriveFormState::driveForm)
    otherImportantCheckStates.associateByTo(result, OtherImportantCheckState::importantCheck)

    // Safeguard against a saved state that doesn't have growth abilities included
    for (growthAbility in GrowthAbility.values()) {
      if (growthAbility !in result) {
        result[growthAbility] = OtherImportantCheckState(growthAbility, consideredImportant = false)
      }
    }

    result
  }

  val foundImportantChecksConsideredImportantCount: Int
    get() = importantCheckStatesByCheck.values.count { it.consideredImportant && it.found }

  @Transient
  val totalImportantChecksConsideredImportantCount: Int = run {
    var total = 0
    total += ansemReportStates.count(AnsemReportState::consideredImportant)
    total += driveFormStates.count(DriveFormState::consideredImportant)
    total += otherImportantCheckStates.count(OtherImportantCheckState::consideredImportant)
    total
  }

  @delegate:Transient
  var activeLocation: ImportantCheckLocation? by mutableStateOf(null)

  @delegate:Transient
  var lastRevealedAnsemReport: AnsemReportState? by mutableStateOf(null)

  init {
    // Initialize some transient state
    for (importantCheckLocationState in importantCheckLocationStates) {
      for (foundImportantCheck in importantCheckLocationState.foundImportantChecks) {
        val importantCheckState = this[foundImportantCheck]
        importantCheckState.found = true
        if (importantCheckLocationState.hintRevealed) {
          importantCheckState.hinted = true
        }
      }
    }
  }

  operator fun get(importantCheckLocation: ImportantCheckLocation): ImportantCheckLocationState {
    return importantCheckLocationStatesByLocation.getValue(importantCheckLocation)
  }

  operator fun get(ansemReport: AnsemReport): AnsemReportState {
    return importantCheckStatesByCheck.getValue(ansemReport) as AnsemReportState
  }

  operator fun get(magic: Magic): OtherImportantCheckState {
    return importantCheckStatesByCheck.getValue(magic) as OtherImportantCheckState
  }

  operator fun get(driveForm: DriveForm): DriveFormState {
    return importantCheckStatesByCheck.getValue(driveForm) as DriveFormState
  }

  operator fun get(importantAbility: ImportantAbility): OtherImportantCheckState {
    return importantCheckStatesByCheck.getValue(importantAbility) as OtherImportantCheckState
  }

  operator fun get(tornPage: TornPage): OtherImportantCheckState {
    return importantCheckStatesByCheck.getValue(tornPage) as OtherImportantCheckState
  }

  operator fun get(summon: Summon): OtherImportantCheckState {
    return importantCheckStatesByCheck.getValue(summon) as OtherImportantCheckState
  }

  operator fun get(proof: Proof): OtherImportantCheckState {
    return importantCheckStatesByCheck.getValue(proof) as OtherImportantCheckState
  }

  operator fun get(promiseCharm: PromiseCharm): OtherImportantCheckState {
    return importantCheckStatesByCheck.getValue(promiseCharm) as OtherImportantCheckState
  }

  operator fun get(importantCheck: ImportantCheck): ImportantCheckState {
    return importantCheckStatesByCheck.getValue(importantCheck)
  }

  fun importantCheckHasBeenFound(importantCheck: ImportantCheck): Boolean {
    return this[importantCheck].found
  }

  fun showImportantCheck(importantCheck: ImportantCheck): Boolean {
    // Promise charm is really the only thing that we want to totally hide when it's off
    return if (importantCheck is PromiseCharm) {
      this[PromiseCharm].consideredImportant
    } else {
      true
    }
  }

  private fun activeLocationState(): ImportantCheckLocationState? {
    return activeLocation?.let { location -> this[location] }
  }

  fun attemptToAddImportantCheck(
    importantCheck: ImportantCheck,
    onAllowed: () -> Unit = { },
    onDisallowed: (reason: Any) -> Unit = { },
    checkLocation: (locationState: ImportantCheckLocationState) -> Any? = { null }
  ) {
    if (importantCheckHasBeenFound(importantCheck)) {
      onDisallowed("already found")
      return
    }
    val activeLocationState = activeLocationState() ?: run {
      onDisallowed("no active location")
      return
    }
    if (activeLocationState.foundImportantChecks.size >= 20) run {
      onDisallowed("no space")
      return
    }

    val locationCheckResult = checkLocation(activeLocationState)
    if (locationCheckResult != null) {
      onDisallowed(locationCheckResult)
      return
    }

    activeLocationState.foundImportantChecks += importantCheck
    val importantCheckState = this[importantCheck]
    importantCheckState.found = true
    if (activeLocationState.hintRevealed) {
      importantCheckState.hinted = true
    }

    onAllowed()
  }

  fun removeImportantCheck(importantCheck: ImportantCheck) {
    importantCheckLocationStates.forEach { it.foundImportantChecks -= importantCheck }
    val importantCheckState = this[importantCheck]
    importantCheckState.found = false
    importantCheckState.hinted = false
  }

  companion object {

    fun fromHints(hints: List<Hint>, hintSettings: Set<HintSetting>): TrackerState {
      val locationStates = run {
        ImportantCheckLocation.values().map { location ->
          val enabled = when (location) {
            ImportantCheckLocation.SorasLevel -> HintSetting.SorasHeart in hintSettings
            ImportantCheckLocation.SimulatedTwilightTown -> HintSetting.SimulatedTwilightTown in hintSettings
            ImportantCheckLocation.Atlantica -> HintSetting.Atlantica in hintSettings
            ImportantCheckLocation.HundredAcreWood -> HintSetting.HundredAcreWood in hintSettings
            else -> true
          }
          val totalImportantChecks = hints.firstOrNull { it.hintedLocation == location }?.importantCheckCount ?: 0
          ImportantCheckLocationState(location, enabled, totalImportantChecks)
        }
      }

      val ansemReportStates = run {
        val ansemReportsConsideredImportant = HintSetting.AnsemReports in hintSettings
        AnsemReport.values().map { ansemReport ->
          val hint = hints[ansemReport.ordinal]
          AnsemReportState(
            ansemReport,
            consideredImportant = ansemReportsConsideredImportant,
            reportLocation = hint.reportLocation,
            hintedLocation = hint.hintedLocation,
            importantCheckCount = hint.importantCheckCount
          )
        }
      }

      val driveFormStates = DriveForm.values().map { driveForm ->
        if (driveForm == DriveForm.FinalForm) {
          DriveFormState(DriveForm.FinalForm, consideredImportant = HintSetting.FinalForm in hintSettings)
        } else {
          DriveFormState(driveForm, consideredImportant = true)
        }
      }

      val otherImportantCheckStates = run {
        val importantCheckStates = mutableListOf<OtherImportantCheckState>()

        GrowthAbility.values().mapTo(importantCheckStates) { growthAbility ->
          OtherImportantCheckState(growthAbility, consideredImportant = false)
        }

        run {
          val cureConsideredImportant = HintSetting.Cure in hintSettings
          Magic.values().mapTo(importantCheckStates) { magic ->
            if (magic == Magic.Cure1 || magic == Magic.Cure2 || magic == Magic.Cure3) {
              OtherImportantCheckState(magic, consideredImportant = cureConsideredImportant)
            } else {
              OtherImportantCheckState(magic, consideredImportant = true)
            }
          }
        }

        run {
          val importantAbilitiesConsideredImportant = HintSetting.SecondChanceOnceMore in hintSettings
          ImportantAbility.values().mapTo(importantCheckStates) { importantAbility ->
            OtherImportantCheckState(importantAbility, consideredImportant = importantAbilitiesConsideredImportant)
          }
        }

        run {
          val tornPagesConsideredImportant = HintSetting.TornPages in hintSettings
          TornPage.values().mapTo(importantCheckStates) { tornPage ->
            OtherImportantCheckState(tornPage, consideredImportant = tornPagesConsideredImportant)
          }
        }

        Summon.values().mapTo(importantCheckStates) { summon ->
          OtherImportantCheckState(summon, consideredImportant = true)
        }

        Proof.values().mapTo(importantCheckStates) { proof ->
          OtherImportantCheckState(proof, consideredImportant = true)
        }

        importantCheckStates += OtherImportantCheckState(
          PromiseCharm,
          consideredImportant = HintSetting.PromiseCharm in hintSettings
        )

        importantCheckStates
      }

      return TrackerState(
        importantCheckLocationStates = locationStates,
        ansemReportStates = ansemReportStates,
        driveFormStates = driveFormStates,
        otherImportantCheckStates = otherImportantCheckStates
      )
    }
  }

}
