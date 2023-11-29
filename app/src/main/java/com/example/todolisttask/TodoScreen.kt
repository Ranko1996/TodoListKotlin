package com.example.todolisttask

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.RadioButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TodoScreen(
    state: TodoState,
    onEvent: (TodoEvent) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(TodoEvent.ShowDialog) }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add todo")
            }
        },
    ) { _ ->
        if (state.isAddingTodo) {
            AddTodoDialog(state = state, onEvent = onEvent)
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SortType.values().forEach { sortType ->
                        OutlinedButton(
                            onClick = {
                                onEvent(TodoEvent.SortTodos(sortType))
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = if (state.sortType == sortType) Color(0xFF6200EE) else Color(
                                    0xFFE0E0E0
                                ),
                                contentColor = if (state.sortType == sortType) Color.White else Color(
                                    0xFF000000
                                )
                            ),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        ) {
                            Text(text = sortType.name)
                        }
                    }

                    // Postavke za sortiranje, ako ih ima
//                    SortType.values().forEach { sortType ->
//                        Row(
//                            modifier = Modifier.clickable {
//                                onEvent(TodoEvent.SortTodos(sortType))
//                            },
//                            verticalAlignment = CenterVertically
//                        ) {
//                            RadioButton(
//                                selected = state.sortType == sortType,
//                                onClick = {
//                                    onEvent(TodoEvent.SortTodos(sortType))
//                                }
//                            )
//                            Text(text = sortType.name)
//                        }
//                    }
                }
            }
            items(state.todos) { todo ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onEvent(TodoEvent.ShowDetails(todo))
                    }) {

//                    Checkbox(
//                        checked = todo.isFinished,
//                        onCheckedChange = { isChecked ->
//                            onEvent(TodoEvent.SetStatus(isChecked))
//                        },
//                        modifier = Modifier.padding(2.dp)
//                    )

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = todo.name, fontSize = 20.sp)
//                        Text(text = todo.description, fontSize = 12.sp)
                        //Vidjeti za ovo dal radi
                        Text(
                            text = if (todo.isFinished) "Done" else "Not Done",
                            fontSize = 12.sp,
                            color = if (todo.isFinished) androidx.compose.ui.graphics.Color.Green else androidx.compose.ui.graphics.Color.Red
                        )
//                        Text(
//                            text = "Show Details",
//                            color = Color.Blue,
//                            modifier = Modifier.clickable {
//                                onEvent(TodoEvent.ShowDetails(todo))
////                                 onEvent(TodoEvent.ShowDialog)
////                                onEvent(TodoEvent.ShowDetails)
////                                FloatingActionButton(onClick = { onEvent(TodoEvent.ShowDialog) }) {
//                            }
//                        )
                    }
                    Checkbox(
                        checked = todo.isFinished,
                        onCheckedChange = { isChecked ->
                            onEvent(TodoEvent.SetStatus(isChecked, todo.id))
                        },
                        modifier = Modifier.padding(2.dp)
                    )

                    IconButton(onClick = {
                        onEvent(TodoEvent.DeleteTodo(todo))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
//                            colorResource(id = )
                            contentDescription = "Delete todo"
                        )
                    }
                }
            }
        }
        if (state.isSingleTodoView) {
            if (state.selectedTodo != null)
                TodoDetailsDialog(
                    todo = state.selectedTodo,
                    state = state,
                    onEvent = { onEvent(TodoEvent.HideDetailsDialog) }
                )
        }
    }
}