package com.pgotuzzo.mvpreddit.presentation.feed.listing.di

import android.app.Activity
import com.pgotuzzo.mvpreddit.di.scope.PerFragment
import com.pgotuzzo.mvpreddit.model.domain.PostService
import com.pgotuzzo.mvpreddit.presentation.feed.listing.PostListingContract
import com.pgotuzzo.mvpreddit.presentation.feed.listing.PostListingPresenter
import com.pgotuzzo.mvpreddit.util.log.Logger
import dagger.Module
import dagger.Provides

@Module
class PostListingModule(private val activity: Activity) {

    @PerFragment
    @Provides
    fun provideLogInPresenter(
        logger: Logger,
        postService: PostService
    ): PostListingContract.Presenter {
//        val repo = PresenterStateRepository(activity, "POST_LISTING_PRESENTER")
        return PostListingPresenter(logger, postService)
    }
}