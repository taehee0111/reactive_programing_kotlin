###### 2장 코틀린과 RxKotlin 을 사용한 함수형 프로그래밍
###### 함수형 프로그래밍
람다 표현식, 순수 함수, 고차 함수, 인라인 함수
람다 표현식: val sum = {num1 : Int, num2 : Int -> num1 + num2}
순수 함수: 입력값에 따른 결과값이 항상 일치한다.
고차 함수: 함수를 인자로 받아들이거나 반환하는 함수를 고차 함수라 부른다.

```
fun highOrderFunc(a:Int, validityCheckFun:(a:Int)->Boolean ){ //변수이름: 인자 -> 반환
     if(validityCheckFun(a)){ print("ok")}
     else{ print("no")}
 }
```

###### 인라인 함수: 프로그램의 성능과 메모리를 최적화 하기위한 함수
함수 호출시 인라인으로 대체하여 함수호출과 스택 유지를 필요하지 않게할 수 있으며 함수형 프로그램의 장점을 유지할 수 있다.
```
fun doSumStuff = (num:Int )->num+(num*num)
-> inline fun doSumStuff = (num:Int )->num+(num*num)
```
인라인 키워드로 고차 함수를 선언하면 함수 자체와 전달된 람다에 모두 영향을 미친다.
고차함수도 성능향상을 위해 inline 을 추가한다.
```
inline fun highOrderFuncInline(a:Int, validityCheckFunc:(a:Int)->Boolean){
    if(validityCheckFunc(a)){
        println("")
    }
}
fun main(args: Array<String>){
    hightOrderFuncInline(12, a:Int-> a.isEven()})
}
```



##### ReactiveCalculator 클래스에 함수형 프로그래밍 적용
55 page 진행 예정


###### 책이름
Reactive Programming in Kotlin
코틀린 리액티브 프로그래밍
RxKotlin을 사용한 리액티브 프로그래밍
지음 리부차크라보티, 옮김 조승진

###### 1장 리액티브 프로그래밍의 소개
###### 함수형 리액티브 적용하는 이유
콜백 지옥의 제거
오류 처리를 위한 표준 메커니즘
간결해진 스레드 사용 
간단한 비동기 연산
전체를 위한 하나, 모든 작업에 대한 동일한 API
함수형 접근 
유지 보수 가능하고 테스트 가능한 코드
###### 리액티브 선언
###### RX 코틀린 시작하기
###### 매커니즘
Iterator push 매커니즘
rxKotlin Observer 패턴의 푸시 매커니즘

###### Observable
onNext: 관찰중인 값을 푸시
onError: 에러(중간에 에러 발생시)
onComplete: 성공(모든 푸시 성공시)

###### Subject(과제)
PublishSubject(과제 제공자) => 과제 생성
subject.map().subscribe(): 과제 관찰
subject.onNext(): 과제 주입