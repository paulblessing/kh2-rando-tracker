package io.github.paulblessing.kh2randotracker

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DesktopDialogProperties
import androidx.compose.ui.window.Dialog

@Composable fun ConfirmResetDialog(
  onDismissRequest: () -> Unit,
  onResetConfirmed: () -> Unit
) {
  Dialog(
    onDismissRequest,
    properties = DesktopDialogProperties(title = "Reset tracker?", size = IntSize(400, 160))
  ) {
    MaterialTheme(colors = darkColors()) {
      Surface(Modifier.fillMaxSize()) {
        Column(Modifier.padding(16.dp)) {
          Text("Reset tracker? All items and hints will be cleared.")
          Row(Modifier.align(Alignment.End)) {
            Button(onClick = { onDismissRequest() }, colors = ButtonDefaults.textButtonColors()) {
              Text("Cancel")
            }

            Spacer(Modifier.width(8.dp))

            Button(
              onClick = { onResetConfirmed() },
              colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
            ) {
              Text("Reset")
            }
          }
        }
      }
    }
  }
}
