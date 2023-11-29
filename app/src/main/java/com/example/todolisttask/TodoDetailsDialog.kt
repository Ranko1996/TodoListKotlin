package com.example.todolisttask

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolisttask.Todo
import com.example.todolisttask.TodoEvent
import com.example.todolisttask.TodoState

@Composable
fun TodoDetailsDialog(
    todo: Todo,
    state: TodoState,
    onEvent: (TodoEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = { onEvent(TodoEvent.HideDetailsDialog) },
        title = {
            Text(
                text = "Todo Details",
                fontSize = 24.sp, // Povećajte veličinu fonta prema vašim željama
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Name: ${todo.name}",
                    fontSize = 18.sp, // Povećajte veličinu fonta prema vašim željama
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Description: ${todo.description}")
                Text(
                    text = "Status: ${if (todo.isFinished) "Done" else "Not Done"}",
                    fontSize = 16.sp, // Povećajte veličinu fonta prema vašim željama
                    fontWeight = FontWeight.Bold
                )
                // Dodajte ostale informacije koje želite prikazati o zadatku
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                // Dodajte gumb za zatvaranje dijaloga ili bilo koje dodatne akcije
                Button(onClick = { onEvent(TodoEvent.HideDetailsDialog) }) {
                    Text(text = "Close")
                }
            }
        }
    )
}
