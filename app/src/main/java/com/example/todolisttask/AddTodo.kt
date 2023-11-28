package com.example.todolisttask

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddTodoDialog(
    state: TodoState,
    onEvent: (TodoEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = { onEvent(TodoEvent.HideTodoDialog) },
        title = { Text(text = "Add todo") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = state.name,
                    onValueChange = { onEvent(TodoEvent.SetName(it)) },
                    placeholder = { Text(text = "Todo Name") }
                )
                TextField(
                    value = state.description,
                    onValueChange = { onEvent(TodoEvent.SetDescription(it)) },
                    placeholder = { Text(text = "Todo Description") }
                )
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically
//                )
//                {
//                    Text(text = "Status:")
//                    Spacer(modifier = Modifier.width(8.dp))
//                    RadioButton(
//                        selected = state.isFinished,
//                        onClick = { onEvent(TodoEvent.(!state.isFinished)) }
//                    )
//                    Text(text = "Done")
//                }
            }
        },
        buttons = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(onClick = {
                    onEvent(TodoEvent.SaveTodo)
                }) {
                    Text(text = "Save")
                }
            }
        }
    )
}
