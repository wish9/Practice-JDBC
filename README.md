[JDBC 블로그 포스팅 주소](https://velog.io/@wish17?tag=JDBC)

# [Spring MVC] JDBC 기반 데이터 액세스 계층

> JDBC
- 데이터를 데이터베이스에 저장 및 업데이트 하거나 데이터베이스에 저장된 데이터를 사용할 수 있도록 해주는 Java에서 제공하는 표준 API
-  JDBC API를 사용해서 다양한 벤더(Oracle, MS SQL, MySQL 등)의 데이터베이스와 연동 가능


[![](https://velog.velcdn.com/images/wish17/post/d66ca8f0-0c47-4f7a-940f-d033364e92c5/image.png)](https://ittrue.tistory.com/250)

#### 일반적인 드라이버 -> 데이터베이스 과정

[![](https://velog.velcdn.com/images/wish17/post/fb1b9764-6606-40c7-a7d5-67b7db0f9bb9/image.png)](https://ittrue.tistory.com/250)

``Connection 객체`` = DB와 연결되는 객체

``Statement 객체`` = SQL 쿼리문을 실행하기 위해 사용하는 객체

위와 같은 방식으로 하면 속도가 느려서 ``Connection Pool``을 사용한다.

> Connection Pool
- 미리 만든  Connection을 보관하고 필요할 때 꺼내주는 역할을 하는 관리자
    - Connection 객체를 생성하는데 시간이 오래 걸려서 로딩 시점에 미리 만들어두고 사용한다.

[![](https://velog.velcdn.com/images/wish17/post/71463b11-23d7-415f-8f7d-ac792baaf948/image.png)](https://ittrue.tistory.com/250)

***

## Spring Data JDBC

> SQL 중심 기술
- SQL 쿼리문을 애플리케이션 내부에 직접적으로 작성하여 DB에 접근하는 기술
- ex. mybatis, Spring JDBC 등

> 객체(Object) 중심 기술 - ORM(Object-Relational Mapping)
- 모든 데이터를 객체(Object) 관점으로 바라보는 기술
- Java 객체(Object)를 SQL 쿼리문으로 자동 변환 한 후에 DB에 접근
- JPA, Spring Data JDBC 등

> Spring Data JDBC
- 데이터와 java object를 연계하기 위해 제공되는 Spring Data의 라이브러리 중 하나
- ORM 기술을 사용

Spring Data JDBC를 사용하기 위해서는 build.gradle의 dependencies에 아래와 같은 의존 라이브러리를 추가해야한다.

```java
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-data-jdbc'
	runtimeOnly 'com.h2database:h2' // 데이터관리 쉽게하기 위해서 인메모리(In-memory) DB인 H2를추가
}
```

***

### 인메모리(In-memory) DB H2 사용방법

> 인메모리(In-memory) DB
- 메모리 안에 데이터를 저장하는 데이터베이스
    - 메모리 = 휘발성 저장공간(껏다키면 사라짐)
- 테스트 환경에서 주로 사용


1. H2 인메모리 DB를 사용하기 위해서 ``application.properties`` 파일 확장자를 application.yml로 바꾸고 아래 코드를 입력해 설정을 추가한다.

```java
spring:
  h2:
    console:
      enabled: true
      path: /h2     // Context path 변경
  datasource:
    url: jdbc:h2:mem:test     //#JDBC URL 변경
  sql:
    init:
      schema-locations: classpath*:db/h2/schema.sql // 테이블 생성 파일 경로
```

추가한 뒤에 프로젝트 실행시키면 아래와 같이 나온다.
![](https://velog.velcdn.com/images/wish17/post/8cc3d66e-d43f-4d84-a6e4-1fe2b3875156/image.png)


2. 웹브라우저에서 ``localhost:8080/h2``를 입력

![](https://velog.velcdn.com/images/wish17/post/494f2791-f4d3-4933-8c65-ce613066e4b0/image.png)



3. JDBC URL란에 1번에서 나온 실행결과에 있는 ``jdbc:h2:mem:test``를 입력 후 Connect 버튼 누르기

![](https://velog.velcdn.com/images/wish17/post/853371ba-3f30-43df-9e7b-e79ba7ccc924/image.png)


H2 Browser 로그인 성공!

***

### 간단한 실습

[풀코드 GitHub](https://github.com/wish9/section3-week2/commit/0c605c1b0da34da33137b5ae74d2ee288c37372e)

#### 사용기능

``CrudRepository`` 인터페이스
- 데이터베이스에 CRUD(데이터 생성, 조회, 수정, 삭제) 작업을 진행하기 위해 Spring에서 지원해주는 인터페이스

``@ID``
- 사용한 멤버변수가 엔티티의 고유 식별자 역할을 하게 만드는 애너테이션
-  Primary key로 지정한 column(열)에 해당된다는 의미를 부여하는 애너테이션

``schema.sql`` 파일 (MESSAGE 테이블 생성 스크립트)
```sql
CREATE TABLE IF NOT EXISTS MESSAGE (
    message_id bigint NOT NULL AUTO_INCREMENT,
    message varchar(100) NOT NULL,
    PRIMARY KEY (message_id)
);
```
#### 포인트 개념
- 엔티티(Entity) 클래스 이름은 데이터베이스 테이블의 이름에 매핑되고, 엔티티 클래스 각각의 멤버 변수는 데이터베이스 테이블의 컬럼에 매핑된다.

- ``application.yml`` 파일의 설정 정보 등록을 통해 데이터베이스 설정, 데이터베이스의 초기화 설정 등의 다양한 설정을 할 수 있다.

- ``application.properties`` 방식보다 ``application.yml`` 방식이 선호되는 추세다.

#### 실습결과

![](https://velog.velcdn.com/images/wish17/post/0aac070b-a067-4e50-90c5-32250c5f23b3/image.png)

![](https://velog.velcdn.com/images/wish17/post/1c512af8-d040-4706-a56a-6708bc155625/image.png)

## Spring Data JDBC 기반의 도메인 엔티티 및 테이블 설계

### DDD(Domain Driven Design)

> 도메인 위주의 설계 기법

> [도메인 지식(Domain Knowledge)](https://ko.wikipedia.org/wiki/%EB%8F%84%EB%A9%94%EC%9D%B8_%EC%A7%80%EC%8B%9D)
- 대상 시스템을 운영하는 환경에 관한 지식
- 구현, 설계하고자 하는 정보, 지식
- ex. 키오스크 프로그램을 만든다고 치면 장바구니, 주문기능이 있어야 겠다고 생각할 것이다. 이런게 도메인 지식이다.
-  서비스 계층에서 비즈니스 로직으로 구현하는 것들


> 애그리거트(Aggregate)
- 도메인들의 묶음
- 비슷한 범주의 연관된 업무들을 하나로 그룹화 해놓은 것

> 애그리거트 루트(Aggregate Root)
- 애그리거트 즉, 도메인들의 묶음에서 해당 묶음을 대표하는 도메인


[![](https://velog.velcdn.com/images/wish17/post/018beae8-17a7-4bd0-80dc-ef4e0618d3c6/image.png)](https://velog.io/@wish17/%EC%BD%94%EB%93%9C%EC%8A%A4%ED%85%8C%EC%9D%B4%EC%B8%A0-%EB%B0%B1%EC%97%94%EB%93%9C-%EB%B6%80%ED%8A%B8%EC%BA%A0%ED%94%84-32%EC%9D%BC%EC%B0%A8-%EA%B4%80%EA%B3%84%ED%98%95-%EB%8D%B0%EC%9D%B4%ED%84%B0%EB%B2%A0%EC%9D%B4%EC%8A%A4Schema-Query-Design#instagram-%EC%8A%A4%ED%82%A4%EB%A7%88-%EB%94%94%EC%9E%90%EC%9D%B8)

전에 디자인 했던 schema(스키마)다. (사진에 링크 첨부 해뒀음)

위와 같이 검정색 묶음이 애그리거트(Aggregate)고
빨간색으로 표시한게 애그리거트 루트(Aggregate Root)다.

***

## 도메인 엔티티 클래스 정의

### Spring Data JDBC에서의 애그리거트(Aggregate) 객체 매핑

#### 애그리거트 객체 매핑 규칙

1. 모든 엔티티 객체의 상태는 애그리거트 루트를 통해서만 변경할 수 있다.
2. 애그리거트(묶음) 내에서 각각의 엔티티 간의 연결은 객체를 참조하는 방식으로 연결한다.

3. 서로 다른 애그리거트(묶음)의 애그리거트 루트 간의 참조는 (1대1과 1대N 관계일 때) 객체 참조 대신에 ID(Foreign key)로 참조한다.
(N대N 관계일 때는 외래키 방식인 ID 참조와 객체 참조 방식이 함께 사용)

### 사용 기능

``@Table``
- 엔티티와 매핑할 테이블을 지정

- 생략 시 매핑한 엔티티 일므을 테이블 이름으로 사용

``@Id``
- 기본 키 매핑(식별자로 지정)
- 사용한 멤버변수를 포함한 클래스를 자동으로 같은 이름의 테이블과 매핑시킨다. ``@Entity`` 역할까지 해줌.
- 직접적인 객체 참조가 아닌 ID 참조(1대N 관계의 애그리거트 루트 간 ID 참조)로 만들려면 ``AggregateReference`` 클래스로 감싸줘야 함.
- N 대 N 관계에서는 ``AggregateReference``로 감쌀 필요가 없다.

```java
@Getter
@Setter
@Table("ORDERS") // 엔티티와 매핑할 테이블을 지정, 생략 시 매핑한 엔티티 이름을 매핑할 테이블 이름으로 사용
// ‘Order’라는 단어는 SQL 쿼리문에서 사용하는 예약어이기 때문에 테이블 이름을 변경
public class Order {
    @Id // 기본 키 매핑(식별자로 지정)
    private long orderId;
    
    // 매핑 규칙 3번 = 애그리거트 루트와 애그리거트 루트 간에는 객체로 직접 참조하는 것이 아니라 ID로 참조한다.
    private AggregateReference<Member, Long> memberId; // 테이블 외래키처럼 memberId를 추가해서 참조하도록 한다.
    // AggregateReference클래스로 Member클래스를 감싸면 직접적인 객체 참조가 아닌 ID 참조가 이루어진다.
    
    ~~~
}    
```
[``AggregateReference``](https://docs.spring.io/spring-data/jdbc/docs/current/api/org/springframework/data/jdbc/core/mapping/AggregateReference.html)클래스
- 1대N 관계일 때 객체 참조 대신에 ID로 참조하게 하려고 감싸는 용도



[``@MappedCollection``](https://docs.spring.io/spring-data/jdbc/docs/current/api/org/springframework/data/relational/core/mapping/MappedCollection.html)
- 엔티티 클래스 간에 연관 관계를 설정하는 에너테이션
- 같은 에그리거트 관계에서만 사용하는 것

![](https://velog.velcdn.com/images/wish17/post/b716c8b5-1543-4157-8964-752162152ed4/image.png)



```java
public class Order {
		
    ~~~
					
    @MappedCollection(idColumn = "ORDER_ID", keyColumn = "ORDER_COFFEE_ID")
    private Set<CoffeeRef> orderCoffees = new LinkedHashSet<>();  // CoffeeRef클래스와 1대N 관계 연결
    
    ~~~
}    
```

- ``idColumn`` = 자식 테이블에 추가되는 외래키에 해당되는 컬럼명 지정
- ``keyColumn`` = 외래키를 **포함하고 있는** 테이블의 **기본키 컬럼명**을 지정




***


## Spring Data JDBC를 통한 데이터 액세스 계층 구현(2) - 서비스, 리포지토리 구현

### 사용기능 정리

>``CrudRepository`` 인터페이스
- 데이터를 데이터베이스의 테이블에 저장, 조회, 수정, 삭제하는 기능을 갖고 있는 인터페이스
- Repository 인터페이스 만들 때 상속해서 사용하면 됨


``UriComponentsBuilder`` 클래스
- URI 객체 생성할 때 쓰는 클래스

```java
URI location =
                UriComponentsBuilder
                        .newInstance()
                        .path(ORDER_DEFAULT_URL + "/{order-id}")
                        .buildAndExpand(order.getOrderId())
                        .toUri();
```

``Optional``클래스
- 데이터 검증에 대한 로직 만들 때 쓰기 좋은 메서드를 많이 갖고 있다.
- ``Optional.ofNullable`` =  값이 null이더라도 NullPointerException이 발생하지 않고, 다음 메서드 호출 가능



>[``@Builder``](https://joosjuliet.github.io/builder/)
- 빌더 자동생성
- 메서드, 생성자에 붙여서 사용
- 간단하게 말하자면 new 객체를 하나 만들고 일일이 set으로 멤버변수 하나하나 값을 초기화해서 원하는 객체를 만드는게 아니라 빌더를 통해 한번에 세팅하면서 객체를 만들어 내는 것이다.
- cf) 클래스 레벨에서 @Builder 어노테이션을 붙이면 모든 요소를 받는 package-private 생성자가 자동으로 생성되며 이 생성자에 @Builder 어노테이션을 붙인 것과 동일하게 동작한다고 한다. 즉 클래스 레벨도 결국은 중간 단계를 거쳐 생성자 레벨로 변환되어 동작한다.

***


### 쿼리 메서드(Query Method)

> Spring Data JDBC에서는 쿼리 메서드를 이용해서 SQL 쿼리문을 사용하지 않고 데이터베이스에 질의를 할 수 있다.

#### 데이터 조회 메서드 정의 예시

``find + By + SQL 쿼리문에서 WHERE 절의 컬럼명 + (WHERE 절 컬럼의 조건이 되는 데이터)``

- Spring Data JDBC에서는 위 형식으로 쿼리 메서드(Query Method)를 정의하면 조건에 맞는 데이터를 테이블에서 조회한다.

```java
public interface MemberRepository extends CrudRepository<Member, Long> {
      Optional<Member> findByEmail(String email);
	  // Member테이블의 Email열 값이 email인 row를 찾는 메서드
}
```
- cf) ``And``를 사용하면 WHERE 절의 조건 컬럼을 여러 개 지정 가능
    - ``findByEmailAndName(String email, String name)``

위 방법 말고도 ``@Query()`` 에너테이션을 이용해서도 가능하다.

- ``@Query("SELECT * FROM COFFEE WHERE COFFEE_ID = :coffeeId")``

***

### service, repository 구현 적용

[풀코드 GitHub링크](https://github.com/wish9/section3-week2/commit/ee2d2604cfd540dbd60c08e24e393dfbea8bff74)

아래와 같이 커피주문 샘플 애플리케이션을 만들어 봤다.

![](https://velog.velcdn.com/images/wish17/post/54115844-dfb2-4b53-a95d-81c331391991/image.png)

![](https://velog.velcdn.com/images/wish17/post/b265c9ea-8029-4a36-890b-1e0c02d598c3/image.png)

![](https://velog.velcdn.com/images/wish17/post/b1a58ea3-4452-4487-b003-385fbefd722e/image.png)

***

### 페이지네이션(Pagenation) 기능 구현

[풀코드 GitHub](https://github.com/wish9/be-homework-jdbc) 학원에서 올린 실습용 코드를 fork해와서 외부에서는 접근이 안되는 것 같다.



![](https://velog.velcdn.com/images/wish17/post/2505abe9-05e8-4f4d-be39-90bbda5403e7/image.png)






***


### 오늘의 정리

- Spring data JDBC는 단방향이다!!
    - 위 예제로 설계한 코드에서 coffee객체가 orederId를 참고할 수 없다!!
