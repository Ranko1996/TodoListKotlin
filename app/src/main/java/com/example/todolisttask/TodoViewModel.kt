package com.example.todolisttask

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class TodoViewModel(
    private val dao: TodoDao
) : ViewModel() {
    private val _sortType = MutableStateFlow(SortType.ALL)
    var selectedTodo by mutableStateOf<Todo?>(null)
        private set
    private val _todos = _sortType
        .flatMapLatest { sortType ->
            when (sortType) {
                SortType.FINISHED -> dao.getFinishedTodos()
                SortType.UNFINISHED -> dao.getUnfinishedTodos()
                SortType.ALL -> dao.getTodos()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(TodoState())


    //Ovo je public state koji ide za ui
    // KOmbiniramo tri flowa i kada se neki od njih promjeni onda se mijenja i state
    val state = combine(_state, _sortType, _todos) { state, sortType, todos ->
        state.copy(
            todos = todos,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TodoState())

    fun onEvent(event: TodoEvent) {
//        println("onEvent u onEvent u todo vju modelucalled with event: $event")
        when (event) {
            is TodoEvent.DeleteTodo -> {
                viewModelScope.launch {
                    dao.deleteTodo(event.todo)
                }
            }

            is TodoEvent.SetStatus -> {
                viewModelScope.launch {
                    try {


                        dao.updateTodoStatus(event.todoId, event.isFinished)

                        _state.update {
                            val updatedState = it.copy(isFinished = event.isFinished)
                            updatedState
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e("TodoApp", "Exception in SetStatus: $e")
                    }
                }
            }


            TodoEvent.HideTodoDialog -> {
                _state.update {
                    it.copy(
                        isAddingTodo = false
                    )
                }
            }


            TodoEvent.SaveTodo -> {

                val name = state.value.name
                val description = state.value.description
                val isFinished = state.value.isFinished

                if (name.isBlank() || description.isBlank()) {
                    return
                }

                val todo = Todo(
                    name = name,
                    description = description,
                    isFinished = isFinished
                )

                viewModelScope.launch { dao.upsertTodo(todo) }

                _state.update {
                    it.copy(
                        isAddingTodo = false,
                        name = "",
                        description = "",
                        isFinished = false
                    )
                }
            }

            is TodoEvent.SetName -> {
                _state.update {
                    it.copy(name = event.name)
                }
            }

            is TodoEvent.SetDescription -> {
                _state.update {
                    it.copy(description = event.description)
                }
            }

//            is TodoEvent.SetStatus -> {
//                _state.update {
//                    it.copy(isFinished = event.isFinished)
//                }
//            }


//            is TodoEvent.SetStatus -> {
//                viewModelScope.launch {
//                    state.value.selectedTodo?.let { selectedTodo ->
//                        dao.updateTodoStatus(selectedTodo.id, event.isFinished)
//                        _state.update {
//                            val updatedState = it.copy(isFinished = event.isFinished)
//                            println("Updated _state: $updatedState")
//                            Log.d("TodoApp", "Updated _state: $updatedState")
//                            updatedState
//                            it.copy(isFinished = event.isFinished)
//                        }
//                        Log.d("TodoApp", "Updated isFinished to: ${event.isFinished}")
//                        println("Updated isFinished to: ${event.isFinished}")
//                    }
//                }
//            }
//            is TodoEvent.SetStatus -> {
//                viewModelScope.launch {
//                    state.value.selectedTodo?.let { selectedTodo ->
//                        dao.updateTodoStatus(selectedTodo.id, event.isFinished)
////                        dao.updateTodoStatus(selectedTodo.id)
//                    }
//                }
//            }


            TodoEvent.ShowDialog -> {
                _state.update {
                    it.copy(isAddingTodo = true)
                }
            }

            is TodoEvent.SortTodos -> {
                _sortType.value = event.sortType
            }

            is TodoEvent.ShowDetails -> {
//                selectedTodo = event.todo
                println("Before state: $_state")
                _state.update {
                    it.copy(
                        selectedTodo = event.todo,
                        isSingleTodoView = true
                    )
                }

            }

            is TodoEvent.HideDetailsDialog -> {
                _state.update {
                    it.copy(
                        selectedTodo = null,
                        isSingleTodoView = false,
                    )
                }
            }


        }
    }
}
