package com.pgotuzzo.mvpreddit.presentation.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pgotuzzo.mvpreddit.presentation.MvpPresenter
import com.pgotuzzo.mvpreddit.presentation.MvpView
import com.pgotuzzo.mvpreddit.util.log.Logger
import javax.inject.Inject

/**
 * Provides the integration between the Presenter and the Activity (View) which extends it.
 * Handles the Presenter lifecycle, synchronizing it with the Activity's one.
 * Additionally it makes sure Dagger component are injected in the right place
 */
abstract class BaseActivity<V : MvpView, P : MvpPresenter<V>>(private val logTag: String) :
    AppCompatActivity() {

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
     * It is going to be called during the [onCreate] process.
     * Appropriate place in order to apply dependency injection
     */
    protected abstract fun inject()

    /**
     * It is going to be called during the [onCreate] process.
     * Appropriate place in order to call [setContentView]
     */
    protected abstract fun setContent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent()
        inject()
        logger.d(logTag, "Activity created")
        if (savedInstanceState != null) {
            logger.d(logTag, "Restoring activity previous state")
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

    override fun onDestroy() {
        presenter.cancelTasksInProgress()
        presenter.detachView()
        logger.d(logTag, "Activity destroyed")
        super.onDestroy()
    }
}