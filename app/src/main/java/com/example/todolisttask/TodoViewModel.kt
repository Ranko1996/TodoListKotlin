package com.example.todolisttask

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
    private val _sortType = MutableStateFlow(SortType.IS_FINISHED_FALSE)

    private val _todos = _sortType
        .flatMapLatest { sortType ->
            when (sortType) {
                SortType.IS_FINISHED_TRUE -> dao.getFinishedTodos()
                SortType.IS_FINISHED_FALSE -> dao.getUnfinishedTodos()
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
        when (event) {
            is TodoEvent.DeleteTodo -> {
                viewModelScope.launch {
                    dao.deleteTodo(event.todo)
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

            is TodoEvent.SetStatus -> {
                _state.update {
                    it.copy(isFinished = event.isFinished)
                }
            }

            TodoEvent.ShowDialog -> {
                _state.update {
                    it.copy(isAddingTodo = true)
                }
            }

            is TodoEvent.SortTodos -> {
                _sortType.value = event.sortType
            }
        }
    }
}
