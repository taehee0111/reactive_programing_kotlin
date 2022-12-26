package com.example.myapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.reactive.ReactiveCalculator
import com.example.myapplication.utils.Utils.longRunningTsk
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.kotlin.toObservable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.AsyncSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import kotlinx.coroutines.*
import java.util.concurrent.Callable
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.function.BiFunction

class MainActivity : AppCompatActivity() {
    private val TAG: String = MainActivity::class.java.simpleName
    private lateinit var binding: ActivityMainBinding
    val observerAny: Observer<Any> = object : Observer<Any> {
        override fun onComplete() {
            Log.d(TAG, "onComplete ")

        }

        override fun onNext(item: Any) {
            Log.d(TAG, "onNext:$item")
        }

        override fun onError(e: Throwable) {
            Log.d(TAG, "onError:${e.message}")

        }

        override fun onSubscribe(d: Disposable) {
            Log.d(TAG, "onSubscribe")
        }
    }
    val observerString: Observer<String> = object : Observer<String> {
        override fun onComplete() {
            Log.d(TAG, "onComplete ")

        }

        override fun onNext(item: String) {
            Log.d(TAG, "onNext:$item")
        }

        override fun onError(e: Throwable) {
            Log.d(TAG, "onError:${e.message}")

        }

        override fun onSubscribe(d: Disposable) {
            Log.d(TAG, "onSubscribe")
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
        //testAsyncSubject()
        //testObservable_beforeFlowable()
        //testFlowable()
        //testFlowable_SubscribeInstance()
        //testError()
        Log.d(TAG, "onCreate: ")
        binding.btnTest.setOnClickListener {
            testZip()
        }

    }

    private fun testZip() {
        val observable1 = Observable.range(1, 10)
        val observable2 = Observable.range(11, 10)
        Observable.zip(observable1, observable2,
            io.reactivex.rxjava3.functions.BiFunction<Int, Int, Int> { emission1, emission2 ->
                emission1 + emission2
            })
    }

    private fun testError() {
        fun Any.toIntOrError(): Int = toString().toInt()

        Observable.just<Any>(1, 2, 3, 4, "isError", 5, 6, 7, 8)
            .map { it.toIntOrError() }
            .subscribeBy(
                onNext = {

                },
                onError = {

                }
            )

//        listOf<Any>(1, 2, 3, 4, "isError", 5, 6, 7, 8)
//            .map { it.toIntOrError() }

    }

//

    //플로어블과 구독자 126 page
// subscribe 의 onSubscription 의 subscription 은 요청 개수를 정할 수 있다. => 백프레셔 역활
    private fun testFlowable_SubscribeInstance() {
        data class MyItem(val id: Int) {
            init {
                Log.d(TAG, "$id")
            }
        }

        Flowable.range(1, 15)
            .map { MyItem(it) }
            .observeOn(Schedulers.io())
            .subscribe(object : org.reactivestreams.Subscriber<MyItem> {
                lateinit var subscription: org.reactivestreams.Subscription
                override fun onSubscribe(subscription: org.reactivestreams.Subscription) {//구독 초기화
                    this.subscription = subscription
                    subscription.request(5) //5개까지 요청

                }

                override fun onNext(item: MyItem) {
                    Log.d(TAG, "${item.id}")
                    if (item.id == 5) {
                        Log.d(TAG, "request More Two")
                        subscription.request(2) //2개 더 요청
                    }
                }

                override fun onError(throwable: Throwable?) {
                    Log.d(TAG, "onError")
                }

                override fun onComplete() {
                    Log.d(TAG, "onComplete")
                }
            })
        runBlocking { delay(60000) } //컨슈머가 모든 아이템을 처리하길 기다리는 코드
    }

    //Flowable 번갈아가면서 실행한다.
    private fun testFlowable() {
        class MyItem(val id: Int) {
            init {
                Log.d(TAG, "$id")
            }
        }
        Flowable.range(1, 1000)
            .map { MyItem(it) }
            .observeOn(Schedulers.computation()) //스레드 설정
            .subscribe({
                runBlocking { delay(50) } //컨슈머가 모든 아이템을 처리하길 기다리는 코드
                Log.d(TAG, "received$it")
            }, {
                Log.d(TAG, "")
            })
        runBlocking { delay(60000) } //컨슈머가 모든 아이템을 처리하길 기다리는 코드

    }

    //하나의 아이템을 처리하는 동안 많은 아이템을 생성한다 -> OutofMemory
    private fun testObservable_beforeFlowable() {
        data class MyItem(val id: Int) {
            init {
                Log.d(TAG, "$id")
            }
        }
        Observable.range(1, 1000)// 1~1000사이의 숫자를 배출하는 코드
            .map { MyItem(it) } //Int 에서 MyItem 클래스로 변환
            .observeOn(Schedulers.computation()) //스레드 설정
            .subscribe({
                Log.d(TAG, "received $it")
                runBlocking { delay(50) }
            }, {
                it.printStackTrace()
            })
        runBlocking { delay(60000) } //컨슈머가 모든 아이템을 처리하길 기다리는 코드
        Log.d(TAG, "end")

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
                Log.d(TAG, "onNext: $it")

            },
            {
                Log.d(TAG, "error: $it")

            }, {
                Log.d(TAG, "complete: ")
            }
        )
        subject.onNext(5)
        subject.subscribe(
            {
                Log.d(TAG, "onNext: $it")

            },
            {
                Log.d(TAG, "error: $it")

            }, {
                Log.d(TAG, "complete: ")
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
                Log.d(TAG, "Received1: $it")
            }
            delay(1100)
            subject.subscribe {
                Log.d(TAG, "Received2: $it")
            }
            delay(1100)
            subject.onComplete()
        }
    }

    private fun testHotObservable2() {
        CoroutineScope(Dispatchers.Default).launch() {
            val connectableObservable = Observable.interval(100, TimeUnit.MILLISECONDS).publish()

            connectableObservable.subscribe { Log.d(TAG, "Subscription 1: $it") }
            connectableObservable.subscribe { Log.d(TAG, "Subscription 2: $it") }
            connectableObservable.connect()
            delay(500)

            connectableObservable.subscribe { Log.d(TAG, "Subscription 3: $it") }
            delay(500)
        }
    }

    private fun testHotObservable() {
        val connectableObservable =
            listOf("String 1", "String 2", "String 3", "String 4", "String 5").toObservable()
                .publish()
        connectableObservable.subscribe { Log.d(TAG, "Subscription $it") }
        connectableObservable.map(String::reversed).subscribe() { Log.d(TAG, "Subscription2 $it") }
        connectableObservable.connect()
        connectableObservable.subscribe { Log.d(TAG, "Subscription3 $it") }
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
                Log.d(TAG, "onComplete ")

            }

            override fun onNext(item: String) {
                Log.d(TAG, "onNext:$item")
            }

            override fun onError(e: Throwable) {
                Log.d(TAG, "onError:${e.message}")

            }

            override fun onSubscribe(d: Disposable) {
                Log.d(TAG, "onSubscribe")
            }
        }

        val list: List<String> = listOf("String 1", "String 2", "String 3", "String 4")
        val observable: Observable<String> = list.toObservable()
        observable.subscribe(observer)
    }

    private fun test9ObserverFrom() {
        val observer: Observer<String> = object : Observer<String> {
            override fun onComplete() {
                Log.d(TAG, "onComplete ")

            }

            override fun onNext(item: String) {
                Log.d(TAG, "onNext:$item")
            }

            override fun onError(e: Throwable) {
                Log.d(TAG, "onError:${e.message}")

            }

            override fun onSubscribe(d: Disposable) {
                Log.d(TAG, "onSubscribe")

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
                Log.d(TAG, "onComplete")

            }

            override fun onNext(item: String) {
                Log.d(TAG, "onNext:$item")
            }

            override fun onError(e: Throwable) {
                Log.d(TAG, "onError:${e.message}")

            }

            override fun onSubscribe(d: Disposable) {
                Log.d(TAG, "onSubscribe")

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
                    Log.d(TAG, "onSubscribe")
                }

                override fun onNext(t: Any) {
                    Log.d(TAG, "onNext")
                }

                override fun onError(e: Throwable) {
                    Log.d(TAG, "onError")
                }

                override fun onComplete() {
                    Log.d(TAG, "onComplete")
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
                Log.d(TAG, "onSuccess$it")
            },
            onError = {
                Log.d(TAG, "onError$it")
            },
            onComplete = {
                Log.d(TAG, "onComplete")
            })
        val maybeEmpty: Maybe<Int> = Maybe.empty()
        //값이 비어있으면 onComplete 호출
        maybeEmpty.subscribeBy(
            onSuccess = {
                Log.d(TAG, "empty onSuccess$it")
            },
            onError = {
                Log.d(TAG, "empty onError$it")
            },
            onComplete = {
                Log.d(TAG, "empty onComplete")
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
        Log.d(TAG, fibonacciSeries.take(10).toList().toString())

    }


    private fun test4() {
        CoroutineScope(Dispatchers.Default).launch { // launch a new coroutine in background and continue
            Log.d(TAG, "test4 start")
            longRunningTsk()
            Log.d(TAG, "test4 end")
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
            Log.d(TAG, it.toString())
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
