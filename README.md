# Реализация реактивной библиотеки на Java (аналог RxJava)

Этот проект представляет собой учебную реализацию реактивной библиотеки, аналогичной RxJava, с поддержкой основных концепций реактивного программирования, включая асинхронное выполнение, операторы преобразования данных и управление потоками выполнения.

## Особенности реализации

- 🧩 Реализация паттерна "Наблюдатель" (Observer pattern)
- ⚡ Поддержка асинхронного выполнения через Schedulers
- 🔁 Основные операторы: `map`, `filter`, `flatMap`
- 🔀 Управление потоками: `subscribeOn`, `observeOn`
- 🛡️ Обработка ошибок через метод `onError`
- ✅ Полное покрытие юнит-тестами

## Структура проекта
```
RxJava/
├── src/
│   ├── main/
│   │   ├── java/
│   │       └── com/
│   │           └── example/
│   │               └── rxjava/
│   │                   ├── core/               
│   │                   ├── functions/            
│   │                   ├── operators/               
│   │                   ├── schedulers/               
│   │                   ├── Main.java             
│   │                             
│   │                         
│   │   
│   └── test/
│   │   ├── java/                            
│   │       └── example/
│   │           └── com/
│   │               └── rxjava
│   │                   ├──ObservableTest.java
│   │                   ├──OperatorTest.java
│   │                   ├──SchedulerTest.java
└── pom.xml 
```

## Примеры использования

### Создание Observable и подписка
```java
Observable.create(emitter -> {
    emitter.onNext("Hello");
    emitter.onNext("World");
    emitter.onComplete();
}).subscribe(
    item -> System.out.println("Received: " + item),
    error -> System.err.println("Error: " + error),
    () -> System.out.println("Completed")
);
```
Использование операторов и Schedulers
```
java
Observable.just(1, 2, 3, 4, 5)
    .map(x -> x * 10)
    .filter(x -> x > 25)
    .subscribeOn(new ComputationScheduler())
    .observeOn(new SingleThreadScheduler())
    .subscribe(
        item -> System.out.println("Received: " + item),
        Throwable::printStackTrace,
        () -> System.out.println("Processing complete")
    );
```
Асинхронная обработка с flatMap
```
java
Observable.just("user1", "user2", "user3")
    .flatMap(username -> Observable.create(emitter -> {
        // Имитация асинхронного запроса
        String userData = fetchUserData(username);
        emitter.onNext(userData);
        emitter.onComplete();
    }))
    .subscribe(System.out::println);
```
## Schedulers
### Библиотека предоставляет три типа планировщиков:

#### ComputationScheduler
- Использует FixedThreadPool с размером, равным количеству ядер процессора.
- Оптимален для CPU-интенсивных задач.

#### IOThreadScheduler
- Использует CachedThreadPool с неограниченным количеством потоков.
- Предназначен для I/O операций (сетевые запросы, работа с файлами).

#### SingleThreadScheduler
- Использует один поток для всех задач.
- Гарантирует последовательное выполнение операций.

## Тестирование

### Проект включает комплекс юнит-тестов, покрывающих все основные сценарии:
```
bash
mvn test
```
### Тесты проверяют:

- Корректность работы операторов (map, filter, flatMap)
- Правильность работы Schedulers в многопоточной среде
- Обработку ошибок
- Управление подписками

## Результаты выполнения

Пример вывода демонстрационной программы:
```
Starting RxJava demo...
Emitting on thread: rx-computation-thread-1
Received: 20 on thread: rx-single-thread
Received: 30 on thread: rx-single-thread
Completed on thread: rx-single-thread
```
## Сборка и запуск

### Клонировать репозиторий:
```
bash
git clone https://github.com/yourusername/rxjava-implementation.git
cd rxjava-implementation
```

### Собрать проект с помощью Maven:
```
bash
mvn clean package
```
### Запустить демонстрационную программу:
```
bash
java -cp target/classes com.example.rxjava.Main
```
