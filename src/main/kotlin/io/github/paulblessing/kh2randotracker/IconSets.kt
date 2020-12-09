package io.github.paulblessing.kh2randotracker

class ImportantCheckLocationIconSet(
  val name: String,
  val icons: Map<ImportantCheckLocation, String>
) {

  companion object {

    val classic: ImportantCheckLocationIconSet
      get() {
        return ImportantCheckLocationIconSet(
          "classic",
          mapOf(
            ImportantCheckLocation.SorasLevel to "images/classic/soraheart.png",
            ImportantCheckLocation.DriveForms to "images/classic/driveorb.png",
            ImportantCheckLocation.SimulatedTwilightTown to "images/classic/Simulated TT.png",
            ImportantCheckLocation.TwilightTown to "images/classic/Twilight Town.png",
            ImportantCheckLocation.HollowBastion to "images/classic/Hollow Bastion.png",
            ImportantCheckLocation.LandOfDragons to "images/classic/Land of Dragons.png",
            ImportantCheckLocation.BeastCastle to "images/classic/Beast Castle.png",
            ImportantCheckLocation.OlympusColiseum to "images/classic/Olympus Coliseum.png",
            ImportantCheckLocation.PortRoyal to "images/classic/Port Royal.png",
            ImportantCheckLocation.Agrabah to "images/classic/Agrabah.png",
            ImportantCheckLocation.HalloweenTown to "images/classic/Halloween Town.png",
            ImportantCheckLocation.PrideLands to "images/classic/Pride Lands.png",
            ImportantCheckLocation.DisneyCastle to "images/classic/Disney Castle.png",
            ImportantCheckLocation.SpaceParanoids to "images/classic/Space Paranoids.png",
            ImportantCheckLocation.WorldThatNeverWas to "images/classic/The World That Never Was.png",
            ImportantCheckLocation.Atlantica to "images/classic/Atlantica.png",
            ImportantCheckLocation.HundredAcreWood to "images/classic/100 Acre Wood.png",
            ImportantCheckLocation.GardenOfAssemblage to "images/classic/replica_data.png",
          )
        )
      }

    val simple: ImportantCheckLocationIconSet
      get() {
        return ImportantCheckLocationIconSet(
          "simple",
          mapOf(
            ImportantCheckLocation.SorasLevel to "images/simple/sora's_level.png",
            ImportantCheckLocation.DriveForms to "images/simple/drive_form.png",
            ImportantCheckLocation.SimulatedTwilightTown to "images/simple/simulated_twilight_town.png",
            ImportantCheckLocation.TwilightTown to "images/simple/twilight_town.png",
            ImportantCheckLocation.HollowBastion to "images/simple/hollow_bastion.png",
            ImportantCheckLocation.LandOfDragons to "images/simple/land_of_dragons.png",
            ImportantCheckLocation.BeastCastle to "images/simple/beast's_castle.png",
            ImportantCheckLocation.OlympusColiseum to "images/simple/olympus_coliseum.png",
            ImportantCheckLocation.PortRoyal to "images/simple/port_royal.png",
            ImportantCheckLocation.Agrabah to "images/simple/agrabah.png",
            ImportantCheckLocation.HalloweenTown to "images/simple/halloween_town.png",
            ImportantCheckLocation.PrideLands to "images/simple/pride_land.png",
            ImportantCheckLocation.DisneyCastle to "images/simple/disney_castle.png",
            ImportantCheckLocation.SpaceParanoids to "images/simple/space_paranoids.png",
            ImportantCheckLocation.WorldThatNeverWas to "images/simple/the_world_that_never_was.png",
            ImportantCheckLocation.Atlantica to "images/simple/atlantica.png",
            ImportantCheckLocation.HundredAcreWood to "images/simple/100_acre_wood.png",
            ImportantCheckLocation.GardenOfAssemblage to "images/simple/replica_data.png",
          )
        )
      }

  }

}

class ImportantCheckIconSet(val name: String, val icons: Map<ImportantCheck, String>) {

  companion object {

    val classic: ImportantCheckIconSet
      get() {
        val icons = mutableMapOf<ImportantCheck, String>()

        AnsemReport.values().associateWithTo(icons) { "images/old/ansem_report.png" }

        listOf(Magic.Fire1, Magic.Fire2, Magic.Fire3).associateWithTo(icons) { "images/classic/fire.png" }
        listOf(Magic.Blizzard1, Magic.Blizzard2, Magic.Blizzard3)
          .associateWithTo(icons) { "images/classic/blizzard.png" }
        listOf(Magic.Thunder1, Magic.Thunder2, Magic.Thunder3).associateWithTo(icons) { "images/classic/thunder.png" }
        listOf(Magic.Cure1, Magic.Cure2, Magic.Cure3).associateWithTo(icons) { "images/classic/cure.png" }
        listOf(Magic.Reflect1, Magic.Reflect2, Magic.Reflect3).associateWithTo(icons) { "images/classic/reflect.png" }
        listOf(Magic.Magnet1, Magic.Magnet2, Magic.Magnet3).associateWithTo(icons) { "images/classic/magnet.png" }

        icons[DriveForm.ValorForm] = "images/classic/valor.png"
        icons[DriveForm.WisdomForm] = "images/classic/wisdom.png"
        icons[DriveForm.LimitForm] = "images/classic/limit.png"
        icons[DriveForm.MasterForm] = "images/classic/master.png"
        icons[DriveForm.FinalForm] = "images/classic/final.png"

        icons[ImportantAbility.SecondChance] = "images/classic/second_chance.png"
        icons[ImportantAbility.OnceMore] = "images/classic/once_more.png"

        TornPage.values().associateWithTo(icons) { "images/old/torn_page.png" }

        icons[Summon.Genie] = "images/classic/lamp.png"
        icons[Summon.Stitch] = "images/classic/ukelele.png"
        icons[Summon.ChickenLittle] = "images/classic/baseball.png"
        icons[Summon.PeterPan] = "images/classic/feather.png"

        icons[Proof.ProofOfNonexistence] = "images/classic/proof_of_nonexistence.png"
        icons[Proof.ProofOfConnection] = "images/classic/proof_of_connection.png"
        icons[Proof.ProofOfPeace] = "images/classic/proof_of_peace.png"

        icons[PromiseCharm] = "images/classic/promise_charm.png"

        return ImportantCheckIconSet("classic", icons)
      }

    val simple: ImportantCheckIconSet
      get() {
        val icons = mutableMapOf<ImportantCheck, String>()

        AnsemReport.values().associateWithTo(icons) { "images/simple/ansem_report.png" }

        listOf(Magic.Fire1, Magic.Fire2, Magic.Fire3).associateWithTo(icons) { "images/simple/fire.png" }
        listOf(Magic.Blizzard1, Magic.Blizzard2, Magic.Blizzard3)
          .associateWithTo(icons) { "images/simple/blizzard.png" }
        listOf(Magic.Thunder1, Magic.Thunder2, Magic.Thunder3).associateWithTo(icons) { "images/simple/thunder.png" }
        listOf(Magic.Cure1, Magic.Cure2, Magic.Cure3).associateWithTo(icons) { "images/simple/cure.png" }
        listOf(Magic.Reflect1, Magic.Reflect2, Magic.Reflect3).associateWithTo(icons) { "images/simple/reflect.png" }
        listOf(Magic.Magnet1, Magic.Magnet2, Magic.Magnet3).associateWithTo(icons) { "images/simple/magnet.png" }

        icons[DriveForm.ValorForm] = "images/simple/valor.png"
        icons[DriveForm.WisdomForm] = "images/simple/wisdom.png"
        icons[DriveForm.LimitForm] = "images/simple/limit.png"
        icons[DriveForm.MasterForm] = "images/simple/master.png"
        icons[DriveForm.FinalForm] = "images/simple/final.png"

        icons[ImportantAbility.SecondChance] = "images/simple/second_chance.png"
        icons[ImportantAbility.OnceMore] = "images/simple/once_more.png"

        TornPage.values().associateWithTo(icons) { "images/old/torn_page.png" }

        icons[Summon.Genie] = "images/simple/genie.png"
        icons[Summon.Stitch] = "images/simple/stitch.png"
        icons[Summon.ChickenLittle] = "images/simple/chicken_little.png"
        icons[Summon.PeterPan] = "images/simple/peter_pan.png"

        icons[Proof.ProofOfNonexistence] = "images/simple/proof_of_nonexistence.png"
        icons[Proof.ProofOfConnection] = "images/simple/proof_of_connection.png"
        icons[Proof.ProofOfPeace] = "images/simple/proof_of_tranquility.png"

        icons[PromiseCharm] = "images/simple/promise_charm.png"

        return ImportantCheckIconSet("simple", icons)
      }

  }

}
