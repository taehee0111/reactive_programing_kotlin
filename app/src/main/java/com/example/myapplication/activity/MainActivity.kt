package com.example.myapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.reactive.ReactiveCalculator
import com.example.myapplication.utils.Utils
import com.example.myapplication.utils.Utils.longRunningTsk
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.kotlin.toObservable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {
    private val tag: String = MainActivity::class.java.simpleName
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //test1()
        //test2()
        //test3()
        //test4()

        binding.btnTest.setOnClickListener {
            test5()
        }


    }

    private fun test5() {
        val result = CoroutineScope(Dispatchers.Default).async {
            1
        }
        runBlocking {
            val num = result.await()
            Log.d(tag, "num:$num")
        }
    }

    private fun test4() {
        CoroutineScope(Dispatchers.Default).launch { // launch a new coroutine in background and continue
            Log.d(tag, "test4 start")
            longRunningTsk()
            Log.d(tag, "test4 end")
        }
    }

    private fun test3() {
        val reactiveCalculator = ReactiveCalculator(1, 2)
        binding.btnTest.setOnClickListener {
            reactiveCalculator.inputText(binding.et1.text.toString(), binding.et2.text.toString())
        }

    }

    private fun test2() {
        val subject: Subject<Int> = PublishSubject.create()

        binding.btnTest.setOnClickListener {
            subject.onNext(4)
            subject.onNext(9)
        }


        //var list1 = listOf(1, 2, 4, 6, 10)
        subject.map { isEven(it) }.subscribe {
            Log.d(tag, it.toString())
        }


    }

    private fun test1() {
        val list = listOf<Any>("One", 2, "Alpha", "Beta", "Gamma", "Delta", "Epsilon")
        list.toObservable() // extension function for Iterables
            .subscribeBy(  // named arguments for lambda Subscribers
                onNext = {
                    if (it is String) {
                        Log.d("", it.toString())
                    }
                },
                onError = { it.printStackTrace() },
                onComplete = { Log.d("", "Done!") }
            )
    }

    private fun isEven(num: Int): Boolean = num % 2 == 0

}