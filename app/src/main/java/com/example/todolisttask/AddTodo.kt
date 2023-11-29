package com.example.todolisttask

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddTodoDialog(
    state: TodoState,
    onEvent: (TodoEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = { onEvent(TodoEvent.HideTodoDialog) },
        title = {
            Text(
                text = "Add Todo",
                fontSize = 24.sp, // Povećajte veličinu fonta prema vašim željama
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = state.name,
                    onValueChange = { onEvent(TodoEvent.SetName(it)) },
                    placeholder = {
                        Text(
                            text = "Todo Name",
                            color = Color.Gray, // Dodano sivo bojenje teksta placeholdera
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                )
                TextField(
                    value = state.description,
                    onValueChange = { onEvent(TodoEvent.SetDescription(it)) },
                    placeholder = {
                        Text(
                            text = "Todo Description",
                            color = Color.Gray, // Dodano sivo bojenje teksta placeholdera
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                )
            }
        },
        buttons = {

            Box(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(onClick = {
                    onEvent(TodoEvent.SaveTodo)
                }) {
                    Text(text = "Save")
                }
//                }
            }

        }
    )
}


