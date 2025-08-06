# Testability

I have completed my prototype to determine the testability of individual files.
It works by scanning opcodes in the methods for instances of `invokestatic` and `getstatic`.
Each of these opcodes references a static method or a static field, respectively.
For each of these opcodes, it checks if the thing referenced by that code is on the blacklist, on the whitelist, or not on either list.
Then it applies the following rules:
- If anything is found on the blacklist, it is flagged as "not testable"
- If nothing is found on the blacklist, but there are things not on either list, it is flagged as "unknown testability"
- If nothing is found on the blacklist, and everything that is found is on the whitelist, it is flagged as "testable"

## Not Testable
```kotlin
class Sample1 {
    fun run(args: Array<String>) {
        val clock = Clock.systemUTC()
        val startTime = clock.instant()
        val fileName = args[0]
        val path = Paths.get(fileName)
        println(path.toAbsolutePath())
        val charset = Charsets.UTF_8
        val target = Files.readString(path, charset)
        val greeting = "Hello, $target!"
        System.out.println(greeting)
        val endTime = clock.instant()
        val duration = endTime.toEpochMilli() - startTime.toEpochMilli()
        val timeTakenMessage = "Took $duration milliseconds"
        System.out.println(timeTakenMessage)
    }
}
```

## Testable
```kotlin
class Sample2(
    private val clock: Clock,
    private val files: FilesContract,
    private val emit:(String)->Unit
    ) {

    fun run(args: Array<String>) {
        val startTime = clock.instant()
        val fileName = args[0]
        val path = Paths.get(fileName)
        val charset = Charsets.UTF_8
        val target = files.readString(path, charset)
        val greeting = "Hello, $target!"
        emit(greeting)
        val endTime = clock.instant()
        val duration = endTime.toEpochMilli() - startTime.toEpochMilli()
        val timeTakenMessage = "Took $duration milliseconds"
        emit(timeTakenMessage)
    }
}
```
