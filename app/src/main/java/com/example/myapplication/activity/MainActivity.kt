package com.example.myapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.reactive.ReactiveCalculator
import com.example.myapplication.utils.Utils.longRunningTsk
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.kotlin.toObservable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext

class MainActivity : AppCompatActivity() {
    private val tag: String = MainActivity::class.java.simpleName
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //test1()
        //test2()
        //test3Calculator()
        //test4()
        //test5()
        //test6monad()
        test7Observer()

        binding.btnTest.setOnClickListener {
        }

    }

    private fun test7Observer() {
        val observer: io.reactivex.rxjava3.core.Observer<Any> = object : io.reactivex.rxjava3.core.Observer<Any> {
            override fun onSubscribe(d: Disposable) {
                Log.d(tag,"onSubscribe")
            }

            override fun onNext(t: Any) {
                Log.d(tag,"onNext")
            }

            override fun onError(e: Throwable) {
                Log.d(tag,"onError")
            }

            override fun onComplete() {
                Log.d(tag,"onComplete")
            }
        }

        val observable: Observable<Any> = listOf("One",2,"Three",).toList().toObservable()
        observable.subscribe(observer)

        val observableOnList: Observable<List<Any>> = Observable.just(
            listOf("One",2,"three","four"),
            listOf("ListItems"),
            listOf(1,2,3,4),
        )
        observableOnList.subscribe(observer)

    }

    private fun test6monad() {
        val maybeValue: Maybe<Int> = Maybe.just(14)
        //값이 존재시 onSuccess 호출
        maybeValue.subscribeBy(
            onSuccess = {
                Log.d(tag, "onSuccess$it")
            },
            onError = {
                Log.d(tag, "onError$it")
            },
            onComplete = {
                Log.d(tag, "onComplete")
            })
        val maybeEmpty: Maybe<Int> = Maybe.empty()
        //값이 비어있으면 onComplete 호출
        maybeEmpty.subscribeBy(
            onSuccess = {
                Log.d(tag, "empty onSuccess$it")
            },
            onError = {
                Log.d(tag, "empty onError$it")
            },
            onComplete = {
                Log.d(tag, "empty onComplete")
            })
    }

    private fun test5() {
//        val fibonacciSeries = buildSequence{
        val fibonacciSeries = sequence<Int> {
            var a = 0
            var b = 1
            yield(a)
            yield(b)
            while (true) {
                val c = a + b
                yield(c)
                a = b
                b = c
            }
        }
        Log.d(tag, fibonacciSeries.take(10).toList().toString())

    }

    private fun test4() {
        GlobalScope.launch { // launch a new coroutine in background and continue
            Log.d(tag, "test4 start")
            longRunningTsk()
            Log.d(tag, "test4 end")
        }

    }

    private fun test3Calculator() {
        val reactiveCalculator = ReactiveCalculator(1, 2)


        binding.btnTest.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                reactiveCalculator.inputText(binding.et1.text.toString(), binding.et2.text.toString())

            }

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