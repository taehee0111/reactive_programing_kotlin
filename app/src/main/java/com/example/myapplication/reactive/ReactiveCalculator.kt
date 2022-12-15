package com.example.myapplication.reactive

import android.text.TextUtils
import android.util.Log
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject

class ReactiveCalculator(num1: Int, num2: Int) {
    private val tag: String = ReactiveCalculator::class.java.simpleName
    private var nums = Pair(num1, num2)

    //과제  등록
    var subjectCalculator: Subject<ReactiveCalculator> = PublishSubject.create()

    //과제 등록 (덧셈)
//    private val subjectAdd: Subject<Pair<Int, Int>> = PublishSubject.create()
//    private val subjectMinus: Subject<Pair<Int, Int>> = PublishSubject.create()
//    private val subjectDIv: Subject<Pair<Int, Int>> = PublishSubject.create()
//    private val subjectMulti: Subject<Pair<Int, Int>> = PublishSubject.create()

    init {
        subjectCalculator.subscribe {
            with(it) {
                calculateAddition()
                calculateSubtraction()
                calculateMultiplication()
                calculateDivision()
//                subjectAdd.onNext(nums)
//                subjectMinus.onNext(nums)
//                subjectDIv.onNext(nums)
//                subjectMulti.onNext(nums)

            }
        }
//        subjectAdd.map { it.first + it.second }.subscribe { Log.d(tag, "ADD: $it") }
//        subjectMinus.map { it.first - it.second }.subscribe { Log.d(tag, "MINUS: $it") }
//        subjectDIv.map { it.first / it.second.toFloat() }.subscribe { Log.d(tag, "DIV: $it") }
//        subjectMulti.map { it.first * it.second }.subscribe { Log.d(tag, "MULTIPLE: $it") }

        subjectCalculator.onNext(this)
    }

    private inline fun calculateAddition(): Int {
        val result = nums.first + nums.second
        Log.d(tag, "add: $result")
        return result
    }


    private inline fun calculateSubtraction(): Int {
        val result = nums.first - nums.second
        Log.d(tag, "sub: $result")
        return result
    }

    private inline fun calculateMultiplication(): Int {
        val result = nums.first * nums.second
        Log.d(tag, "mul: $result")
        return result
    }

    private inline fun calculateDivision(): Double {
        val result = (nums.first * 1.0) / (nums.second * 1.0)
        Log.d(tag, "div: $result")
        return result
    }


    fun inputText(num1Text: String, num2Text: String) {

        val isExistsNum1 = !TextUtils.isEmpty(num1Text)
        val isExistsNum2 = !TextUtils.isEmpty(num2Text)
        if( isExistsNum1){
            var num1 = num1Text.toInt()
            modifyNumber(a= num1)
        }
        if (isExistsNum2) {
            var num2 = num2Text.toInt()
            modifyNumber(b= num2)
        }


    }

    private fun modifyNumber(a: Int = nums.first, b: Int = nums.second) {

        nums = Pair(a, b);
        subjectCalculator.onNext(this)
    }

}