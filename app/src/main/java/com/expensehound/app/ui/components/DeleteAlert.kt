package com.expensehound.app.ui.components

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun DeleteAlert(
    openDialog: MutableState<Boolean>,
    name: String,
    onDelete: () -> Unit
) {

    val context = LocalContext.current

    if (!openDialog.value) {
        return Unit
    }

    AlertDialog(
        onDismissRequest = {
            openDialog.value = false
        },
        title = {
            Text(text = "Потвърди")
        },
        text = {
            Text(text = "Сигурни ли сте, че искате да изтриете ${name}?")
        },
        confirmButton = {
            TextButton(
                onClick = {
                    openDialog.value = false
                    onDelete()

                    Toast.makeText(
                        context,
                        "Успешно изтрихте ${name}",
                        Toast.LENGTH_LONG
                    ).show()
                }) {
                Text("Да")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    openDialog.value = false
                }) {
                Text("Откажи")
            }
        }
    )
}