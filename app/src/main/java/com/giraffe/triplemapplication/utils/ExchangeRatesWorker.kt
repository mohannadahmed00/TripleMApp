package com.giraffe.triplemapplication.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.giraffe.triplemapplication.database.ConcreteLocalSource
import com.giraffe.triplemapplication.model.repo.Repo
import com.giraffe.triplemapplication.network.ApiClient
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.runBlocking

class ExchangeRatesWorker(private val context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    override fun doWork(): Result {
        val repo = Repo.getInstance(ApiClient, ConcreteLocalSource(context))
        var result = Result.failure()
        runBlocking {
            repo.getCurrencies()
                .catch {
                    result = if (runAttemptCount < 3) {
                        Result.retry()
                    } else {
                        Result.failure()
                    }
                }
                .collect {
                    repo.setExchangeRates(it)
                        .catch { result = Result.failure() }
                        .collect {
                            result = Result.success()
                        }
                }
        }
        return result
    }
}