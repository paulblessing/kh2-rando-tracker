package io.github.paulblessing.kh2randotracker

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import okio.Buffer
import org.junit.Assert.*
import org.junit.Test

class PersistableStateTest {

  private val moshi: Moshi = Moshi.Builder()
    .add(SavedStateAdapter())
    .build()

  private fun <T> toJsonString(adapter: JsonAdapter<T>, item: T): String {
    val buffer = Buffer()
    val jsonWriter = JsonWriter.of(buffer)
    adapter.toJson(jsonWriter, item)
    return buffer.readUtf8()
  }

  @Test fun `location state enabled with found checks and revealed hint`() {
    val locationState = ImportantCheckLocationState(
      location = ImportantCheckLocation.HalloweenTown,
      enabled = true,
      totalImportantChecks = 5
    ).apply {
      progression = 2
      hintRevealed = true
      foundImportantChecks = setOf(Magic.Cure3, Proof.ProofOfPeace)
    }
    val adapter = moshi.adapter(ImportantCheckLocationState::class.java)
    val jsonString = toJsonString(adapter, locationState)
    with(adapter.fromJson(jsonString)!!) {
      assertEquals(ImportantCheckLocation.HalloweenTown, location)
      assertTrue(enabled)
      assertEquals(5, totalImportantChecks)
      assertEquals(2, progression)
      assertEquals(true, hintRevealed)
      assertEquals(setOf(Magic.Cure3, Proof.ProofOfPeace), foundImportantChecks)
    }
  }

  @Test fun `ansem report considered important with failed attempts`() {
    val reportState = AnsemReportState(
      ansemReport = AnsemReport.Report10,
      consideredImportant = true,
      reportLocation = ImportantCheckLocation.HollowBastion,
      hintedLocation = ImportantCheckLocation.PortRoyal,
      importantCheckCount = 2
    ).apply {
      failedAttempts = 1
    }
    val adapter = moshi.adapter(AnsemReportState::class.java)
    val jsonString = toJsonString(adapter, reportState)
    with(adapter.fromJson(jsonString)!!) {
      assertEquals(AnsemReport.Report10, ansemReport)
      assertTrue(consideredImportant)
      assertEquals(ImportantCheckLocation.HollowBastion, reportLocation)
      assertEquals(ImportantCheckLocation.PortRoyal, hintedLocation)
      assertEquals(2, importantCheckCount)
      assertEquals(1, failedAttempts)
    }
  }

  @Test fun `drive form`() {
    val driveFormState = DriveFormState(
      driveForm = DriveForm.WisdomForm,
      consideredImportant = true
    ).apply {
      driveFormLevel = 3
    }
    val adapter = moshi.adapter(DriveFormState::class.java)
    val jsonString = toJsonString(adapter, driveFormState)
    with(adapter.fromJson(jsonString)!!) {
      assertEquals(DriveForm.WisdomForm, driveForm)
      assertTrue(consideredImportant)
      assertEquals(3, driveFormLevel)
    }
  }

  @Test fun `other important check considered important`() {
    val otherImportantCheckState = OtherImportantCheckState(
      importantCheck = Magic.Blizzard2,
      consideredImportant = true
    )
    val adapter = moshi.adapter(OtherImportantCheckState::class.java)
    val jsonString = toJsonString(adapter, otherImportantCheckState)
    with(adapter.fromJson(jsonString)!!) {
      assertEquals(Magic.Blizzard2, importantCheck)
      assertTrue(consideredImportant)
    }
  }

  @Test fun `other important check not considered important`() {
    val otherImportantCheckState = OtherImportantCheckState(
      importantCheck = PromiseCharm,
      consideredImportant = false
    )
    val adapter = moshi.adapter(OtherImportantCheckState::class.java)
    val jsonString = toJsonString(adapter, otherImportantCheckState)
    with(adapter.fromJson(jsonString)!!) {
      assertEquals(PromiseCharm, importantCheck)
      assertFalse(consideredImportant)
    }
  }

  // TODO: At least 1 test for the full persistable state?

}
