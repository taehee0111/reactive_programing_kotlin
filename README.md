

### 책이름
Reactive Programming in Kotlin
코틀린 리액티브 프로그래밍
RxKotlin을 사용한 리액티브 프로그래밍
지음 리부차크라보티, 옮김 조승진

### 1장 리액티브 프로그래밍의 소개
### 함수형 리액티브 적용하는 이유
콜백 지옥의 제거
오류 처리를 위한 표준 메커니즘
간결해진 스레드 사용 
간단한 비동기 연산
전체를 위한 하나, 모든 작업에 대한 동일한 API
함수형 접근 
유지 보수 가능하고 테스트 가능한 코드
### 리액티브 선언
### RX 코틀린 시작하기
### 매커니즘
Iterator push 매커니즘
rxKotlin Observer 패턴의 푸시 매커니즘

### Observable
onNext: 관찰중인 값을 푸시
onError: 에러(중간에 에러 발생시)
onComplete: 성공(모든 푸시 성공시)

### Subject(과제)
PublishSubject(과제 제공자) => 과제 생성
subject.map().subscribe(): 과제 관찰
subject.onNext(): 과제 주입

### 2장 코틀린과 RxKotlin 을 사용한 함수형 프로그래밍
### 함수형 프로그래밍
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

### 인라인 함수: 프로그램의 성능과 메모리를 최적화 하기위한 함수
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

### ReactiveCalculator 클래스에 함수형 프로그래밍 적용

### 코루틴을 사용한 ReactiveCalculator 클래스 

### 함수형 프로그래밍 모나드
값을 캡슐화하고 추가 기능을 더해 새로운 타입을 생성하는 구조체

maybe empty -> subscribe -> onComplete
maybe exists value -> subscribe -> onSuccess

##### 코루틴 시작하기

##### 시퀀스 생성하기
async/await


### 단일 모나드
Maybe 는 단순히 모나드의 한 유형이다. Maybe 외에도 다수가 있다.
```
Maybe
maybe.subscribe{
    onSuccess //터미널 함수
    onFail //터미널 함수
    onComplete //터미널 함수
```
### 3장 옵저버블과 옵저버와 구독자
Observables, Observer, subjects
Observables: 계산작업
Observer: 소비자
Observable 은 소비자에게 값을 푸시한다.

Observable: onNext, onComplete, onError
Observable<T>: 어떤 유형도 감지할 수 있다 array/list도 가능하다

### 옵저버블
옵저버는 옵저버블을 구독한다.
옵저버블이 아이탬을 내보내기 시작한다.
옵저버는 옵저버블에서 내보내는 모든 아이템에 반응 한다.

### 옵저버블이 동작하는 방법 
onNext onComplete onError 

### Observable.create 메서드 이해
Observable.create 매서드로 옵저버블을 직접 생성할 수 있다.
사용자가 지정한 데이터 구조를 사용하거나 내보내는 값을 제어할떄 유용하다.

### Observable.from 메서드 이해
Observable.from Iterator, Future Array 등 다양한 값을 Observable 로 생성할 수 있다.

### Observable.toObservable 의 확장 함수 이해
변수.toObservable
내부적으로는 Observable.from 을 사용하고 있다.

### Observable.just 메서드 이해
Observable.Just: 팩토리 매서드
넘겨진 인자만을 배출하는 옵저버블을 생성한다.
Observable.just 에 단일 인자로 넘기면 전체 목록을 하나의 아이템으로 배출한다. 
Iterable 내부의 각각 아이템을 Observable 로 생성하는 Observable.from 과는 다르다.
 리스트를 단일 아이템으로 취급한다.
 just( 아이템1, 아이템2, 아이템3) -> 단일 아이템 3개가 나온다.

### Observable 의 다른 팩토리 메서드
observable.range(a,b) a~b 까지 onNext
observable.empty(a,b) a~b 까지 next 없이 complete
observable.interval() 0부터 지정된 숫자까지 (구독 취소하거나 완료될 때 까지 ) 
observable.timer() 0부터 지정된 숫자까지 (지정된 시간이 경과한 후 한 번만 실행)

### 구독자: Observer 인터페이스
rxKotlin 1.0 Subscriber -> rxkotlin 2.0 Observer
onNext: 아이템을 하나씩 넘겨주기 위해 옵저버블은 옵저버의 이 메서드를 호출한다.
onComplete: onNext 를 통한 아이템 전달이 종료 되었을 때 호출된다.
onError: 옵저버블에서 에러가 발생했을 때 옵저버에 정의된 로직이 있다 면 onError 을 호출하고 그렇지 않다면 예외를 발생한다.
onSubscriber: Observable 이 새로운 Observer 를 구독할 때 호출한다.

### 구독과 해지 
Observable: 관찰 대상 
Observer: 관찰자
Subscribe: Observable 과 Observer 를 연결하는 매개체
onSubscribe 는 disposable 인스턴스를 반환 Disposable 를 통해 주어진 신간에 배출을 멈출 수 있다.

### 핫, 콜드 옵저버블 
95page 공부 예정
