package io.github.paulblessing.kh2randotracker

import com.squareup.moshi.*
import okio.buffer
import okio.sink
import okio.source
import java.io.File

@JsonClass(generateAdapter = true)
class SavedState(
  val trackerState: TrackerState?,
  val importantCheckLocationIconSet: String,
  val importantCheckIconSet: String
) {

  companion object {

    private val savedStateAdapter: JsonAdapter<SavedState> = run {
      val moshi: Moshi = Moshi.Builder().add(SavedStateAdapter()).build()
      moshi.adapter(SavedState::class.java)
    }

    private val saveStateFile: File by lazy {
      val trackerDirectory = File(System.getProperty("user.home"), ".kh2-rando-tracker")
      trackerDirectory.mkdirs()
      File(trackerDirectory, "saved-state.json")
    }

    fun load(): SavedState? {
      return if (saveStateFile.isFile) {
        saveStateFile.source().buffer().use { source ->
          savedStateAdapter.fromJson(source)
        }
      } else {
        null
      }
    }

    fun save(
      state: TrackerState?,
      importantCheckLocationIconSet: ImportantCheckLocationIconSet,
      importantCheckIconSet: ImportantCheckIconSet
    ) {
      val savedState = SavedState(
        state,
        importantCheckLocationIconSet = importantCheckLocationIconSet.name,
        importantCheckIconSet = importantCheckIconSet.name
      )
      saveStateFile.sink().buffer().use { sink ->
        savedStateAdapter.toJson(sink, savedState)
        sink.flush()
      }
    }

  }

}

@JsonQualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ImportantCheckCount

@Suppress("unused")
class SavedStateAdapter {

  @ToJson fun encodeImportantCheckLocation(importantCheckLocation: ImportantCheckLocation): Int {
    return importantCheckLocation.id
  }

  @FromJson fun decodeImportantCheckLocation(id: Int): ImportantCheckLocation {
    return ImportantCheckLocation.values().first { it.id == id }
  }

  @ToJson fun encodeAnsemReport(ansemReport: AnsemReport): Int = ansemReport.ordinal

  @FromJson fun decodeAnsemReport(ordinal: Int): AnsemReport = AnsemReport.values()[ordinal]

  @ToJson fun encodeDriveForm(driveForm: DriveForm): String = driveForm.name[0].toString()

  @FromJson fun decodeDriveForm(firstLetter: String): DriveForm {
    return when (firstLetter) {
      "V" -> DriveForm.ValorForm
      "W" -> DriveForm.WisdomForm
      "L" -> DriveForm.LimitForm
      "M" -> DriveForm.MasterForm
      "F" -> DriveForm.FinalForm
      else -> error("invalid drive form $firstLetter")
    }
  }

  @ToJson fun encodeImportantCheck(importantCheck: ImportantCheck): Int = importantCheck.id

  @FromJson fun decodeImportantCheck(id: Int): ImportantCheck {
    return when (id) {
      in 100 until 200 -> AnsemReport.values().first { it.id == id }
      in 200 until 300 -> Magic.values().first { it.id == id }
      in 300 until 400 -> DriveForm.values().first { it.id == id }
      in 400 until 500 -> ImportantAbility.values().first { it.id == id }
      in 500 until 600 -> TornPage.values().first { it.id == id }
      in 600 until 700 -> Summon.values().first { it.id == id }
      in 700 until 800 -> Proof.values().first { it.id == id }
      PromiseCharm.id -> PromiseCharm
      else -> error("Invalid important check ID $id")
    }
  }

  @ToJson fun encodeImportantCheckCount(@ImportantCheckCount importantCheckCount: Int): Int {
    return (importantCheckCount * 31) + 5962
  }

  @ImportantCheckCount @FromJson fun decodeImportantCheckCount(encodedCount: Int): Int {
    return (encodedCount - 5962) / 31
  }

}
