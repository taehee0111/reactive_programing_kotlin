package com.example.myapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.reactive.ReactiveCalculator
import com.example.myapplication.utils.Utils.longRunningTsk
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.kotlin.toObservable
import io.reactivex.rxjava3.subjects.AsyncSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import kotlinx.coroutines.*
import java.util.concurrent.Callable
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private val tag: String = MainActivity::class.java.simpleName
    private lateinit var binding: ActivityMainBinding
    val observerAny: Observer<Any> = object : Observer<Any> {
        override fun onComplete() {
            Log.d(tag, "onComplete ")

        }

        override fun onNext(item: Any) {
            Log.d(tag, "onNext:$item")
        }

        override fun onError(e: Throwable) {
            Log.d(tag, "onError:${e.message}")

        }

        override fun onSubscribe(d: Disposable) {
            Log.d(tag, "onSubscribe")
        }
    }
    val observerString: Observer<String> = object : Observer<String> {
        override fun onComplete() {
            Log.d(tag, "onComplete ")

        }

        override fun onNext(item: String) {
            Log.d(tag, "onNext:$item")
        }

        override fun onError(e: Throwable) {
            Log.d(tag, "onError:${e.message}")

        }

        override fun onSubscribe(d: Disposable) {
            Log.d(tag, "onSubscribe")
        }
    }

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
        //test7Observer()
        //test8ObserverCreate()
        //testHotObservable()
        //testHotObservable2()
        //testHotObservable3()

        binding.btnTest.setOnClickListener {
        }

        testAsyncSubject()

    }

    private fun testAsyncSubject() {
//        val observable = Observable.just(1, 2, 3, 4)
//        observable.subscribe(subject)
        val subject = AsyncSubject.create<Int>()
        subject.onNext(1)
        subject.onNext(2)
        subject.onNext(3)
        subject.onNext(4)
        subject.subscribe(
            {
                Log.d(tag, "onNext: $it")

            },
            {
                Log.d(tag, "error: $it")

            }, {
                Log.d(tag, "complete: ")
            }
        )
        subject.onNext(5)
        subject.subscribe(
            {
                Log.d(tag, "onNext: $it")

            },
            {
                Log.d(tag, "error: $it")

            }, {
                Log.d(tag, "complete: ")
            }
        )
        subject.onComplete()
    }

    //subject -> 콜드 옵저버블을 핫 옵저버블 같이 행동 시킨다.
    private fun testHotObservable3() {
        CoroutineScope(Dispatchers.Default).launch() {
            val observable = Observable.interval(100, TimeUnit.MILLISECONDS)
            val subject = PublishSubject.create<Long>()
            observable.subscribe(subject)
            subject.subscribe {
                Log.d(tag, "Received1: $it")
            }
            delay(1100)
            subject.subscribe {
                Log.d(tag, "Received2: $it")
            }
            delay(1100)
            subject.onComplete()
        }
    }

    private fun testHotObservable2() {
        CoroutineScope(Dispatchers.Default).launch() {
            val connectableObservable = Observable.interval(100, TimeUnit.MILLISECONDS).publish()

            connectableObservable.subscribe { Log.d(tag, "Subscription 1: $it") }
            connectableObservable.subscribe { Log.d(tag, "Subscription 2: $it") }
            connectableObservable.connect()
            delay(500)

            connectableObservable.subscribe { Log.d(tag, "Subscription 3: $it") }
            delay(500)
        }
    }

    private fun testHotObservable() {
        val connectableObservable = listOf("String 1", "String 2", "String 3", "String 4", "String 5").toObservable().publish()
        connectableObservable.subscribe { Log.d(tag, "Subscription $it") }
        connectableObservable.map(String::reversed).subscribe() { Log.d(tag, "Subscription2 $it") }
        connectableObservable.connect()
        connectableObservable.subscribe { Log.d(tag, "Subscription3 $it") }
        connectableObservable.connect()// 배출 X
    }


    private fun test11observableJust() {
        Observable.just("A String").subscribe(observerAny)
        Observable.just(54).subscribe(observerAny)
        Observable.just(listOf("String1", "String2", "String3")).subscribe(observerAny)

    }

    private fun test10toObserver() {
        val observer: Observer<String> = object : Observer<String> {
            override fun onComplete() {
                Log.d(tag, "onComplete ")

            }

            override fun onNext(item: String) {
                Log.d(tag, "onNext:$item")
            }

            override fun onError(e: Throwable) {
                Log.d(tag, "onError:${e.message}")

            }

            override fun onSubscribe(d: Disposable) {
                Log.d(tag, "onSubscribe")
            }
        }

        val list: List<String> = listOf("String 1", "String 2", "String 3", "String 4")
        val observable: Observable<String> = list.toObservable()
        observable.subscribe(observer)
    }

    private fun test9ObserverFrom() {
        val observer: Observer<String> = object : Observer<String> {
            override fun onComplete() {
                Log.d(tag, "onComplete ")

            }

            override fun onNext(item: String) {
                Log.d(tag, "onNext:$item")
            }

            override fun onError(e: Throwable) {
                Log.d(tag, "onError:${e.message}")

            }

            override fun onSubscribe(d: Disposable) {
                Log.d(tag, "onSubscribe")

            }
        }

        val list = listOf("String 1", "String 2", "String 3", "String 4")
        val observableFromIterator = Observable.fromIterable(list)
        observableFromIterator.subscribe(observer)

        val callable = object : Callable<String> {
            override fun call(): String {
                return "From Callable"
            }
        }

        val observableFromCallable: Observable<String> = Observable.fromCallable(callable)
        observableFromCallable.subscribe(observer)

        val future: Future<String> = object : Future<String> {
            override fun cancel(mayInterruptIfRunning: Boolean): Boolean = false

            override fun isCancelled(): Boolean = false

            override fun isDone(): Boolean = true

            override fun get(): String = "Hello From Future"

            override fun get(timeout: Long, unit: TimeUnit?): String = "Hello From Future"
        }
        val observableFromFuture: Observable<String> = Observable.fromFuture(future)
        observableFromFuture.subscribe(observer)
    }

    private fun test8ObserverCreate() {
        val observer: Observer<String> = object : Observer<String> {
            override fun onComplete() {
                Log.d(tag, "onComplete")

            }

            override fun onNext(item: String) {
                Log.d(tag, "onNext:$item")
            }

            override fun onError(e: Throwable) {
                Log.d(tag, "onError:${e.message}")

            }

            override fun onSubscribe(d: Disposable) {
                Log.d(tag, "onSubscribe")

            }
        }

        val observable: Observable<String> = Observable.create<String> {
            it.onNext("Emit 1")
            it.onNext("Emit 2")
            it.onNext("Emit 3")
            it.onNext("Emit 4")
            it.onComplete()
        }
        observable.subscribe(observer)

        val observable2: Observable<String> = Observable.create<String> {
            it.onNext("Emit 1")
            it.onNext("Emit 2")
            it.onNext("Emit 3")
            it.onNext("Emit 4")
            it.onError(Exception("My custom Exception"))
        }

        observable2.subscribe(observer)
    }//test8ObserverCreate

    private fun test7Observer() {
        val observer: io.reactivex.rxjava3.core.Observer<Any> =
            object : io.reactivex.rxjava3.core.Observer<Any> {
                override fun onSubscribe(d: Disposable) {
                    Log.d(tag, "onSubscribe")
                }

                override fun onNext(t: Any) {
                    Log.d(tag, "onNext")
                }

                override fun onError(e: Throwable) {
                    Log.d(tag, "onError")
                }

                override fun onComplete() {
                    Log.d(tag, "onComplete")
                }
            }

        val observable: Observable<Any> = listOf("One", 2, "Three").toList().toObservable()
        observable.subscribe(observer)

        val observableOnList: Observable<List<Any>> = Observable.just(
            listOf("One", 2, "three", "four"),
            listOf("ListItems"),
            listOf(1, 2, 3, 4),
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

    private fun test5sequence() {
        val fibonacciSeries = sequence {
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
        CoroutineScope(Dispatchers.Default).launch { // launch a new coroutine in background and continue
            Log.d(tag, "test4 start")
            longRunningTsk()
            Log.d(tag, "test4 end")
        }
    }

    private fun test3Calculator() {
        val reactiveCalculator = ReactiveCalculator(1, 2)
        binding.btnTest.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                reactiveCalculator.inputText(
                    binding.et1.text.toString(),
                    binding.et2.text.toString()
                )

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