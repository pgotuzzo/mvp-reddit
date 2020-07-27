package com.pgotuzzo.mvpreddit.presentation.base

import android.os.Parcelable
import com.pgotuzzo.mvpreddit.model.data.ActivityStateRepository
import com.pgotuzzo.mvpreddit.presentation.MvpPresenter
import com.pgotuzzo.mvpreddit.presentation.MvpView
import com.pgotuzzo.mvpreddit.presentation.base.exception.TaskInterruptedException
import com.pgotuzzo.mvpreddit.util.log.Logger
import kotlinx.coroutines.Job

/**
 * Base implementation of [MvpPresenter]. Holds the code in common for all the potential
 * implementations, simplifying the extension of such a class, without losing capabilities.
 * Extensions in charge of two responsibilities:
 *  - Implementing [getCurrentState] which returns the state [S] to be persisted before presenter
 *  being killed
 *  - Implementing [applyState] which receives previous state by param in order to allow recovering
 *  the lost state
 *
 * Additionally offers a protected method to subscribe Jobs to be cancelled before the presenter
 * being killed. Check [addJobInProgress]
 */
abstract class BasePresenter<V : MvpView, S : Parcelable>(
    loggerHolder: LoggerHolder,
    private val stateRepo: ActivityStateRepository?,
    private var state: S?
) : MvpPresenter<V> {

    protected var view: V? = null

    protected val logger: Logger = loggerHolder.logger
    private val jobsInProgress: MutableList<Job> = emptyList<Job>().toMutableList()
    private val loggerTag: String = loggerHolder.tag

    constructor(loggerHolder: LoggerHolder) : this(loggerHolder, null, null)

    constructor(loggerHolder: LoggerHolder, state: S, stateRepository: ActivityStateRepository) :
            this(loggerHolder, stateRepository, state)

    /**
     * Applies the state restored, or set as default, in order to keep the presenter
     * up to date with the latest changes
     */
    protected abstract fun applyState(state: S?)

    /**
     * It is going to be used internally, in order to automatically save, if there is any,
     * state of the current instance
     *
     * @return current state
     */
    protected abstract fun getCurrentState(): S?

    final override fun attachView(view: V) {
        logger.d(loggerTag, "View attached")
        this.view = view
        logger.d(loggerTag, "Applying state")
        applyState(state)
    }

    final override fun cancelTasksInProgress() {
        val logMsg =
            if (jobsInProgress.isEmpty()) "No Task in progress to be cancelled" else "Cancelling tasks in progress"
        logger.d(loggerTag, logMsg)

        jobsInProgress.forEach { job ->
            if (job.isActive) {
                job.cancel(TaskInterruptedException())
            }
        }
        jobsInProgress.clear()
    }

    final override fun detachView() {
        logger.d(loggerTag, "View detached")
        view = null
    }

    final override fun restoreState() {
        state = stateRepo?.load()
        state?.run { stateRepo?.clear() }

        val logMsg = if (state == null) "No state to recover" else "Recovering previous state"
        logger.d(loggerTag, logMsg)
    }

    final override fun saveState() {
        state = getCurrentState()
        state?.run { stateRepo?.save(this) }

        val logMsg = if (state == null) "No state to save" else "Saving current state"
        logger.d(loggerTag, logMsg)
    }

    /**
     * Important! - Recover from Jobs interruption
     *
     * The in progress [Job] could be cancelled arbitrarily during the lifecycle of the presenter.
     * In order to recover from it, if the task requires it, a [TaskInterruptedException] is going
     * to be thrown.
     * Catch the exception and store the data necessary to re-launch it in the future. Remember
     * that [getCurrentState] and [applyState] are called to save and restore the presenter from
     * death.
     *
     * Tip: save task's relevant information internally and return it in the [getCurrentState]
     * method. The same information is going to be passed by param in the [applyState] call. So
     * that you can decide if re-launch the task manually or not ... your call
     */
    protected fun addJobInProgress(job: Job) {
        jobsInProgress.add(job)
        logger.d(
            loggerTag,
            "Job added to the jobs in progress list. Jobs count: ${jobsInProgress.size}"
        )
    }
}