package com.giraffe.triplemapplication.repo_testing

import com.giraffe.triplemapplication.database.LocalSource
import com.giraffe.triplemapplication.model.repo.Repo
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.model.wishlist.WishListItem
import com.giraffe.triplemapplication.repo_testing.fakelocalsource.FakeLocalSource
import com.giraffe.triplemapplication.repo_testing.fakeremotesource.FakeRemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class RepoTest {
    private lateinit var fakeLocalSource: FakeLocalSource
    private lateinit var fakeRemoteSource: FakeRemoteSource
    private lateinit var fakeRepo: Repo

    @Before
    fun setup() {
        fakeLocalSource = FakeLocalSource()
        fakeRemoteSource = FakeRemoteSource()
        fakeRepo = Repo.getInstance(fakeRemoteSource ,fakeLocalSource)
    }



}