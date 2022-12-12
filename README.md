
###### 책이름
Reactive Programming in Kotlin
코틀린 리액티브 프로그래밍
RxKotlin을 사용한 리액티브 프로그래밍
지음 리부차크라보티, 옮김 조승진

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