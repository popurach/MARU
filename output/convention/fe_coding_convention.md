# Front-End **Coding Convention**

# 1. 파일

### 1-1. 컴포넌트로 분리된 파일은 PascalCase를 적용

```jsx
Search.kt
MapComponent.kt
```

### 1-2. 컴포넌트 명의 경우 기능명 + CRUD형태로 작성

```jsx
WhiskyCreate.kt
WhiskyDetail.kt
WhiskyList.kt
WhiskyUpdate.kt 등
```

### 1-4. 서로 연관된 파일들은 하나의 폴더에 넣어 준다

```jsx
components ─── WhiskyRecommend
             └ WhiskyRecommend.kt
             └ WhiskyRecommendList.kt
             └ WhiskyRecommendListItem.kt 
```

# 2. 변수명

### 2-1. 변수는 camelCase와 영어 대소문자, 숫자를 사용

```kotlin
val variable = "var"
val nonConstScalar = "non-const"
val mutableCollection: MutableSet = HashSet()
val mutableElements = listOf(mutableInstance)
val mutableValues = mapOf("Alice" to mutableInstance, "Bob" to mutableInstance2)
val logger = Logger.getLogger(MyClass::class.java.name)
val nonEmptyArray = arrayOf("these", "can", "change")
```

### 2-2. 변수에 할당되는 값이 Boolean인 경우에는 is, can, exist, has를 접두사로 붙임

```kotlin
val isLoading = false;
val isOpen = true;

val canRead = true;
val hasItem = false;
```

### 2-3. 상수는 대문자로 작성

```jsx
const val NUMBER = 5
val NAMES = listOf("Alice", "Bob")
val AGES = mapOf("Alice" to 35, "Bob" to 32)
val COMMA_JOINER = Joiner.on(',') // Joiner is immutable
val EMPTY_ARRAY = arrayOf()
```

# 3. 함수

### 3-1. 함수 매개변수

- 함수 서명이 한 줄에 들어가지 않으면 각 매개변수 선언을 한 줄에 하나씩 표시합니다. 이 형식으로 정의된 매개변수에서는 단일 들여쓰기(+4)를 사용해야 합니다. 닫는 괄호() 및 반환 유형은 추가 들여쓰기 없이 한 줄에 하나씩 입력됩니다.

```kotlin
fun <T> Iterable<T>.joinToString(
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = ""
): String {
    // …
}
```

### 3-2. 표현식 함수

- 함수에 표현식이 하나만 포함되는 경우 [표현식 함수](https://kotlinlang.org/docs/reference/functions.html#single-expression-functions)로 표현될 수 있습니다.

```kotlin
override fun toString(): String {
    return "Hey"
}
```

```kotlin
override fun toString(): String = "Hey"
```

# 4. 서식

### 4-1. 중괄호

- `else` 브랜치가 2개 이상이 아니고 한 줄에 들어가는 `when` 브랜치 및 `if`
 표현식에는 중괄호가 필요하지 않습니다.

```kotlin
if (string.isEmpty()) return

val result =
    if (string.isEmpty()) DEFAULT_VALUE else string

when (value) {
    0 -> return
    // …
}
```

- `if`, `for`, `when`, `do while` 문과 표현식의 경우 본문이 비어 있거나 단일 구문만 포함하는 경우에도 중괄호가 필요합니다.

```kotlin
if (string.isEmpty())
    return  // WRONG!

if (string.isEmpty()) {
    return  // Okay
}

if (string.isEmpty()) return  // WRONG
else doLotsOfProcessingOn(string, otherParametersHere)

if (string.isEmpty()) {
    return  // Okay
} else {
    doLotsOfProcessingOn(string, otherParametersHere)
}
```

### 4-1. 비어 있지 않은 블록

- 비어 있지 않은 블록과 블록 형식 구문에서는 중괄호가 K&R(Kernighan and Ritchie) 스타일('이집트 대괄호')을 따릅니다.
    - 여는 중괄호 앞에 줄바꿈이 없습니다.
    - 여는 중괄호 뒤에 줄바꿈이 있습니다.
    - 닫는 중괄호 앞에 줄바꿈이 있습니다.
    - 중괄호로 구문이 종료되거나 함수, 생성자 또는 *named* 클래스의 본문이 종료되는 *경우에만* 닫는 중괄호 뒤에 줄바꿈이 있습니다. 예를 들어 중괄호 뒤에 `else` 또는 쉼표가 온다면 중괄호 뒤에 줄바꿈이 *없습니다*.

```kotlin
return Runnable {
    while (condition()) {
        foo()
    }
}

return object : MyClass() {
    override fun foo() {
        if (condition()) {
            try {
                something()
            } catch (e: ProblemException) {
                recover()
            }
        } else if (otherCondition()) {
            somethingElse()
        } else {
            lastThing()
        }
    }
}
```

### 4-3. 빈 블록

- 빈 블록 또는 블록 형식 구문은 K&R 스타일이어야 합니다

```kotlin
try {
    doSomething()
} catch (e: Exception) {} // WRONG!

try {
    doSomething()
} catch (e: Exception) {
} // Okay
```

### 4-4. 표현식

- 표현식으로 사용되는 `if/else` 조건문에서는 전체 표현식이 한 줄에 들어가는 *경우에만*
 중괄호를 생략할 수 있습니다.

```kotlin
val value1 = if (string.isEmpty()) 0 else 1  // Okay

val value2 = if (string.isEmpty())  // WRONG!
    0
else
    1

val value3 = if (string.isEmpty()) { // Okay
    0
} else {
    1
}
```

### 파일 트리

- 라우터에서 최초로 렌더링 되는 컴포넌트들은 하나의 폴더로 분리
- 이외의 컴포넌트들은 기능별로 폴더 생성하여 ‘`component`’의 하위 폴더로 분리
- 재사용하는 컴포넌트는 ‘`component/common`’ 폴더에 정리
    - NavBar / Button / Input / Form 등 다른 사람도 가져다 쓸 여지가 있는 컴포넌트만

### 들여쓰기, 세미콜론 등 양식 통일 관련

- 기본 합의사
    - 들여쓰기 : space 4
    - 세미콜론 : 사용 안함

### 

---

# Front-End **Design Style Convention**

### 와이어 프레임

- 최대한 간단하게 논리흐름 위주로 만들기, 디자인적 요소는 반영 X

### 프로토타입

- 완성품과 동일한 수준의 퀄리티로 만들기

### 디자인 리더

- 이예진님
    - 큰 틀에서의 디자인은 리더의 의사 존중
    - 의견 있을시 개선 예시를 만들어 디자인 리더에게 제출

### 환경 변수

- AndroidStudio →  Flamingo
- kotlin→ v1.7.0
- java → 1.8
- compose-ui→ v1.2.0

---

# 파일 구조

```jsx
com.shoebill.maru
├─di    // hilt module
├─model
│ ├─data    // data class
│ ├─repository  // 기능별 repository
│ ├─interface   // 기능별 통신 API interface
| └─APiInstance.kt  
├─ui
│ ├─component  // compose파일은 여기로(기능별로 하위 폴더 생성)
│ │ ├─search
│ │ └─map
│ ├─page       // 페이지를 나타내는 compose는 여기로
| └─theme 
├─util  
├─viewmodel  
├─MainActivity.kt
└─MaruApplication.kt
```