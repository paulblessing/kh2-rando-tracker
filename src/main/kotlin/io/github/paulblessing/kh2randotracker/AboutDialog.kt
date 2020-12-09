package io.github.paulblessing.kh2randotracker

import androidx.compose.foundation.ClickableText
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DesktopDialogProperties
import androidx.compose.ui.window.Dialog
import java.awt.Desktop
import java.net.URI

@Composable fun AboutDialog(onDismissRequest: () -> Unit) {
  val desktop = if (Desktop.isDesktopSupported()) Desktop.getDesktop() else null
  Dialog(
    onDismissRequest,
    properties = DesktopDialogProperties(title = "About tracker", size = IntSize(600, 600))
  ) {
    MaterialTheme(colors = darkColors()) {
      Surface(Modifier.fillMaxSize()) {
        ScrollableColumn(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
          BasicText("Copyright © 2020 Paul Blessing")
          Link(desktop, "https://github.com/paulblessing")

          Spacer(Modifier.height(16.dp))

          Text("Acknowledgements", style = MaterialTheme.typography.h5)

          Spacer(Modifier.height(16.dp))

          BasicText("RedBuddha, for the original design of the important checks tracker")
          Link(desktop, "https://github.com/TrevorLuckey")
          Link(desktop, "https://www.twitch.tv/redbuddha")

          Spacer(Modifier.height(16.dp))

          BasicText("Televo, for many of the icons")
          Link(desktop, "https://github.com/Televo")

          Spacer(Modifier.height(16.dp))

          BasicText("ShinyGoombah and zaxutic, for other trackers that served as inspiration")
          Link(desktop, "https://github.com/ShinyGoombah")
          Link(desktop, "https://www.twitch.tv/theshinygoombah")
          Link(desktop, "https://github.com/zaxutic")
          Spacer(Modifier.height(16.dp))

          Text("Powered by open-source software", style = MaterialTheme.typography.h5)

          Spacer(Modifier.height(16.dp))

          OpenSourceHeaderRow()
          OpenSourceRow(
            desktop,
            name = "Compose for Desktop",
            uri = "https://www.jetbrains.com/lp/compose/",
            licenseName = "Apache 2.0",
            licenseUri = "http://www.apache.org/licenses/LICENSE-2.0"
          )
          OpenSourceRow(
            desktop,
            name = "Moshi",
            uri = "https://github.com/square/moshi",
            licenseName = "Apache 2.0",
            licenseUri = "http://www.apache.org/licenses/LICENSE-2.0"
          )
          OpenSourceRow(
            desktop,
            name = "Okio",
            uri = "https://github.com/square/okio",
            licenseName = "Apache 2.0",
            licenseUri = "http://www.apache.org/licenses/LICENSE-2.0"
          )
        }
      }
    }
  }
}

@Composable private fun BasicText(text: String) {
  Text(text, style = MaterialTheme.typography.body2)
}

@Composable private fun Link(desktop: Desktop?, uriString: String, linkText: String = uriString) {
  ClickableText(
    AnnotatedString(linkText, SpanStyle(color = MaterialTheme.colors.secondary)),
    Modifier.padding(vertical = 2.dp),
    onClick = {
      if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
        desktop.browse(URI.create(uriString))
      }
    }
  )
}

@Composable private fun OpenSourceHeaderRow() {
  Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
    Box(Modifier.weight(1.0f)) {
      Text("Software")
    }
    Box(Modifier.weight(1.0f)) {
      Text("License")
    }
  }
}

@Composable private fun OpenSourceRow(
  desktop: Desktop?,
  name: String,
  uri: String,
  licenseName: String,
  licenseUri: String
) {
  Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
    Box(Modifier.weight(1.0f)) {
      Link(desktop, linkText = name, uriString = uri)
    }
    Box(Modifier.weight(1.0f)) {
      Link(desktop, linkText = licenseName, uriString = licenseUri)
    }
  }
}
