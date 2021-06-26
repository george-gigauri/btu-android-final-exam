package btu.finalexam.georgegigauri.util

sealed class UIState<out T> {
    data class Success<out T>(val data: T) : UIState<T>()
    data class Error<out T>(val message: String) : UIState<T>()
    class Loading<out T> : UIState<T>()
    class Empty<out T> : UIState<T>()
}