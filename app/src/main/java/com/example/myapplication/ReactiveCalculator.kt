package com.example.myapplication

import android.util.Log
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject

class ReactiveCalculator(num1: Int, num2: Int) {
    private val tag: String = ReactiveCalculator::class.java.simpleName
    private var nums = Pair(num1, num2)

    //과제  등록
    var subjectCalc: Subject<ReactiveCalculator> = PublishSubject.create()

    //과제 등록 (덧셈)
    private val subjectAdd: Subject<Pair<Int, Int>> = PublishSubject.create()
    private val subjectMinus: Subject<Pair<Int, Int>> = PublishSubject.create()
    private val subjectDIv: Subject<Pair<Int, Int>> = PublishSubject.create()
    private val subjectMulti: Subject<Pair<Int, Int>> = PublishSubject.create()

    init {
        subjectCalc.subscribe {
            with(it) {
                subjectAdd.onNext(nums)
                subjectMinus.onNext(nums)
                subjectDIv.onNext(nums)
                subjectMulti.onNext(nums)
            }
        }
        subjectAdd.map { it.first + it.second }.subscribe { Log.d(tag, "ADD: $it") }
        subjectMinus.map { it.first - it.second }.subscribe { Log.d(tag, "MINUS: $it") }
        subjectDIv.map { it.first / it.second.toFloat() }.subscribe { Log.d(tag, "DIV: $it") }
        subjectMulti.map { it.first * it.second }.subscribe { Log.d(tag, "MULTIPLE: $it") }

        subjectCalc.onNext(this)
    }

    fun modifyNumber(a: Int = nums.first, b: Int = nums.second) {
        nums = Pair(a, b);
        subjectCalc.onNext(this)
    }
}