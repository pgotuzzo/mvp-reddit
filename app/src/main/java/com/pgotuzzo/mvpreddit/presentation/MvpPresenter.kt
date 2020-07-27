package com.pgotuzzo.mvpreddit.presentation

interface MvpPresenter<in V : MvpView> {
    /**
     * Associates a [V] instance to this
     * Check [detachView] to remove this association
     *
     * @param view [V] instance to be associated
     */
    fun attachView(view: V)

    /**
     * Removes any existing reference to a [V]
     * If [attachView] wasn't previously called, it won't do anything
     */
    fun detachView()

    /**
     * Restores to its last saved state. [saveState] will provide the means to persist
     * such a state. If a task was in progress, it will be re-started
     */
    fun restoreState()

    /**
     * Saves the current state. [restoreState] will provide the means to recover it.
     * If a task is in progress, it will be restarted on [restoreState].
     * It is recommended to call [cancelTasksInProgress] if you don't want task in progress to be
     * executed during on [restoreState] process or re-call [saveState] once the task is done
     * so that it won't be re-executed twice
     */
    fun saveState()

    /**
     * Cancel any tasks in progress. In order to postpone the execution of the in progress tasks
     * call [saveState] before, so that, they will be executed during [restoreState] call
     */
    fun cancelTasksInProgress()
}