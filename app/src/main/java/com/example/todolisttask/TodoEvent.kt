package com.example.todolisttask

sealed interface TodoEvent{
    object SaveTodo: TodoEvent
    data class SetName(val name: String): TodoEvent
    data class SetDescription(val description: String): TodoEvent
    data class SetStatus(val isFinished: Boolean): TodoEvent
    object ShowDialog: TodoEvent
    object HideTodoDialog: TodoEvent
    object HideDetailsDialog: TodoEvent
    data class SortTodos(val sortType: SortType): TodoEvent
    data class DeleteTodo(val todo: Todo): TodoEvent
    data class ShowDetails(val todo: Todo) : TodoEvent

}