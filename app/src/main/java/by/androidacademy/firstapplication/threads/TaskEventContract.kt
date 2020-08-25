package by.androidacademy.firstapplication.threads

interface TaskEventContract {
    interface Operationable {
        fun createTask()
        fun startTask()
        fun cancelTask()
    }
}