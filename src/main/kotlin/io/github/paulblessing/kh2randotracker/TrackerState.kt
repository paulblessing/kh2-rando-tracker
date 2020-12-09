@file:OptIn(ExperimentalStdlibApi::class)

package io.github.paulblessing.kh2randotracker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object TrackerState {

  val SorasLevel = Other("images/simple/sora's_level.png")
  val DriveForms = Other("images/simple/drive_form.png")

  val SimulatedTwilightTown = World("images/simple/simulated_twilight_town.png")
  val TwilightTown = World("images/simple/twilight_town.png")
  val HollowBastion = World("images/simple/hollow_bastion.png")
  val LandOfDragons = World("images/simple/land_of_dragons.png")
  val BeastCastle = World("images/simple/beast's_castle.png")
  val OlympusColiseum = World("images/simple/olympus_coliseum.png")
  val PortRoyal = World("images/simple/port_royal.png")
  val Agrabah = World("images/simple/agrabah.png")
  val HalloweenTown = World("images/simple/halloween_town.png")
  val PrideLands = World("images/simple/pride_land.png")
  val DisneyCastle = World("images/simple/disney_castle.png")
  val SpaceParanoids = World("images/simple/space_paranoids.png")
  val WorldThatNeverWas = World("images/simple/the_world_that_never_was.png")
  val Atlantica = World("images/simple/atlantica.png", defaultEnabled = false)
  val HundredAcreWood = World("images/simple/100_acre_wood.png")

  val GardenOfAssemblage = Other("images/simple/replica_data.png", broadcast = false)

  val Report1 = AnsemReport(id = 101, number = 1)
  val Report2 = AnsemReport(id = 102, number = 2)
  val Report3 = AnsemReport(id = 103, number = 3)
  val Report4 = AnsemReport(id = 104, number = 4)
  val Report5 = AnsemReport(id = 105, number = 5)
  val Report6 = AnsemReport(id = 106, number = 6)
  val Report7 = AnsemReport(id = 107, number = 7)
  val Report8 = AnsemReport(id = 108, number = 8)
  val Report9 = AnsemReport(id = 109, number = 9)
  val Report10 = AnsemReport(id = 110, number = 10)
  val Report11 = AnsemReport(id = 111, number = 11)
  val Report12 = AnsemReport(id = 112, number = 12)
  val Report13 = AnsemReport(id = 113, number = 13)

  val Fire1 = Magic(id = 200, imageName = "fire")
  val Fire2 = Magic(id = 201, imageName = "fire")
  val Fire3 = Magic(id = 202, imageName = "fire")
  val Blizzard1 = Magic(id = 210, imageName = "blizzard")
  val Blizzard2 = Magic(id = 211, imageName = "blizzard")
  val Blizzard3 = Magic(id = 212, imageName = "blizzard")
  val Thunder1 = Magic(id = 220, imageName = "thunder")
  val Thunder2 = Magic(id = 221, imageName = "thunder")
  val Thunder3 = Magic(id = 222, imageName = "thunder")
  val Cure1 = Magic(id = 230, imageName = "cure")
  val Cure2 = Magic(id = 231, imageName = "cure")
  val Cure3 = Magic(id = 232, imageName = "cure")
  val Reflect1 = Magic(id = 240, imageName = "reflect")
  val Reflect2 = Magic(id = 241, imageName = "reflect")
  val Reflect3 = Magic(id = 242, imageName = "reflect")
  val Magnet1 = Magic(id = 250, imageName = "magnet")
  val Magnet2 = Magic(id = 251, imageName = "magnet")
  val Magnet3 = Magic(id = 252, imageName = "magnet")

  val ValorForm = DriveForm(id = 300, imageName = "valor")
  val WisdomForm = DriveForm(id = 301, imageName = "wisdom")
  val LimitForm = DriveForm(id = 302, imageName = "limit")
  val MasterForm = DriveForm(id = 303, imageName = "master")
  val FinalForm = DriveForm(id = 304, imageName = "final")

  val SecondChance = ImportantAbility(id = 400, "second_chance")
  val OnceMore = ImportantAbility(id = 401, "once_more")

  val TornPage1 = TornPage(id = 500, number = 1)
  val TornPage2 = TornPage(id = 501, number = 2)
  val TornPage3 = TornPage(id = 502, number = 3)
  val TornPage4 = TornPage(id = 503, number = 4)
  val TornPage5 = TornPage(id = 504, number = 5)

  val Genie = Summon(id = 600, imageName = "genie")
  val Stitch = Summon(id = 601, imageName = "stitch")
  val ChickenLittle = Summon(id = 602, imageName = "chicken_little")
  val PeterPan = Summon(id = 603, imageName = "peter_pan")

  val ProofOfNonexistence = Proof(id = 700, imageName = "nonexistence")
  val ProofOfConnection = Proof(id = 701, imageName = "connection")
  val ProofOfPeace = Proof(id = 702, imageName = "tranquility")

  val PromiseCharm = PromiseCharm(id = 800)

  val allWorlds: Set<World>
    get() {
      return setOf(
        HollowBastion,
        TwilightTown,
        LandOfDragons,
        BeastCastle,
        OlympusColiseum,
        SpaceParanoids,
        HalloweenTown,
        PortRoyal,
        Agrabah,
        PrideLands,
        DisneyCastle,
        HundredAcreWood,
        SimulatedTwilightTown,
        WorldThatNeverWas,
        Atlantica
      )
    }

  val allImportantCheckLocations: Set<ImportantCheckLocation>
    get() {
      return buildSet {
        add(SorasLevel)
        add(DriveForms)
        addAll(allWorlds)
        add(GardenOfAssemblage)
      }
    }

  val allAnsemReports: Set<AnsemReport>
    get() {
      return setOf(
        Report1,
        Report2,
        Report3,
        Report4,
        Report5,
        Report6,
        Report7,
        Report8,
        Report9,
        Report10,
        Report11,
        Report12,
        Report13
      )
    }

  val allMagic: Set<Magic>
    get() {
      return setOf(
        Fire1,
        Fire2,
        Fire3,
        Blizzard1,
        Blizzard2,
        Blizzard3,
        Thunder1,
        Thunder2,
        Thunder3,
        Cure1,
        Cure2,
        Cure3,
        Reflect1,
        Reflect2,
        Reflect3,
        Magnet1,
        Magnet2,
        Magnet3
      )
    }

  val allDriveForms: Set<DriveForm>
    get() = setOf(ValorForm, WisdomForm, LimitForm, MasterForm, FinalForm)

  val allImportantAbilities: Set<ImportantAbility>
    get() = setOf(SecondChance, OnceMore)

  val allTornPages: Set<TornPage>
    get() = setOf(TornPage1, TornPage2, TornPage3, TornPage4, TornPage5)

  val allSummons: Set<Summon>
    get() = setOf(Genie, Stitch, ChickenLittle, PeterPan)

  val allProofs: Set<Proof>
    get() = setOf(ProofOfNonexistence, ProofOfConnection, ProofOfPeace)

  val allImportantChecks: Set<ImportantCheck>
    get() {
      return buildSet {
        addAll(allAnsemReports)
        addAll(allMagic)
        addAll(allDriveForms)
        addAll(allImportantAbilities)
        addAll(allTornPages)
        addAll(allSummons)
        addAll(allProofs)
        add(PromiseCharm)
      }
    }

  var hintsLoaded: Boolean by mutableStateOf(false)
  var broadcastWindow: Boolean by mutableStateOf(false)
  var activeLocation: ImportantCheckLocation? = null
  var unfoundImportantChecks: Set<ImportantCheck> by mutableStateOf(allImportantChecks)

}
