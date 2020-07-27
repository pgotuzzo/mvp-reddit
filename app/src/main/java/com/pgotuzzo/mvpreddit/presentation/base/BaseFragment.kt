package com.pgotuzzo.mvpreddit.presentation.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.pgotuzzo.mvpreddit.presentation.MvpPresenter
import com.pgotuzzo.mvpreddit.presentation.MvpView
import com.pgotuzzo.mvpreddit.util.log.Logger
import javax.inject.Inject

/**
 * Provides the integration between the Presenter and the Fragment (View) which extends it.
 * Handles the Presenter lifecycle, synchronizing it with the Fragment's one.
 * Additionally it makes sure Dagger component are injected in the right place
 */
abstract class BaseFragment<V : MvpView, P : MvpPresenter<V>>(private val logTag: String) :
    Fragment() {

    @Inject
    protected lateinit var logger: Logger

    /**
     * @return presenter associated to the view return through [view]
     */
    protected abstract val presenter: P

    /**
     * @return view to be attached to the presenter
     */
    protected abstract val view: V

    /**
     * It is going to be called during the [onActivityCreated] process.
     * Appropriate place in order to apply dependency injection
     */
    protected abstract fun inject()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        inject()
        logger.d(logTag, "Fragment's activity created")
        if (savedInstanceState != null) {
            logger.d(logTag, "Restoring fragment's previous state")
            // Restore from dead
            presenter.restoreState()
        }
        presenter.attachView(view)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        logger.d(logTag, "Saving state")
        presenter.saveState()
    }

    override fun onDestroyView() {
        presenter.cancelTasksInProgress()
        presenter.detachView()
        logger.d(logTag, "Fragment's view destroyed")
        super.onDestroyView()
    }
}