package com.example.todolisttask

data class TodoState(
    val todos: List<Todo> = emptyList(),
    val name: String = "",
    val description: String = "",
    val isFinished: Boolean = false,
    val isAddingTodo: Boolean = false,
//    val singleTodoView: Boolean = false,
    val sortType: SortType = SortType.IS_FINISHED_FALSE,
    val selectedTodo: Todo? = null,
    val isSingleTodoView: Boolean = false
)
