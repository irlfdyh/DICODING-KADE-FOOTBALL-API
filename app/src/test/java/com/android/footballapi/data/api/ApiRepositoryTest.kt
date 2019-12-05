package com.android.footballapi.data.api

import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class ApiRepositoryTest {

    @Test
    fun doRequest() {
        val apiRepository = mock(ApiRepository::class.java)
        val url = "https://www.thesportsdb.com/api/v1/json/1/search_all_teams.php?l=English%20Premier%20League"
        runBlocking {
            apiRepository.doRequestAsync(url)
            verify(apiRepository).doRequestAsync(url)
        }
    }
}