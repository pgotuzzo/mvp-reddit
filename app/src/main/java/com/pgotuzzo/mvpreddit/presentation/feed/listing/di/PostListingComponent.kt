package com.pgotuzzo.mvpreddit.presentation.feed.listing.di

import com.pgotuzzo.mvpreddit.di.component.ApplicationComponent
import com.pgotuzzo.mvpreddit.di.scope.PerFragment
import com.pgotuzzo.mvpreddit.presentation.feed.listing.PostListingFragment
import dagger.Component

@PerFragment
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [PostListingModule::class]
)
interface PostListingComponent {

    fun inject(postListingFragment: PostListingFragment)
}