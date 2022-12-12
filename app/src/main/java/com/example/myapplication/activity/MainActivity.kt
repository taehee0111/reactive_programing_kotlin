package com.example.myapplication.activity

import android.database.Observable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.myapplication.R
import com.example.myapplication.ReactiveCalculator
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.kotlin.toObservable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import org.reactivestreams.Subscriber

class MainActivity : AppCompatActivity() {
    private val tag: String = MainActivity::class.java.simpleName
    private lateinit var btnTest: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnTest = findViewById<Button>(R.id.btn_test)

        //test1()
        //test2()
        test3()

    }

    private fun test3() {
        var reactiveCalculator = ReactiveCalculator(1, 2)

        btnTest.setOnClickListener {
            reactiveCalculator.modifyNumber(1,1)
        }

    }

    private fun test2() {
        val subject: Subject<Int> = PublishSubject.create()

        btnTest.setOnClickListener {
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