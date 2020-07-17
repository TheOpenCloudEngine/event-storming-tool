### 서버 실행

axon 서버 실행이 되어야 정상적으로 작동 합니다.  
아래 Site 에서 서버를 다운로드 해주세요.  
https://axoniq.io/download  


```

cd axon-server
java -jar axonserver-4.3.5.jar
```

axon 서버 UI  
http://localhost:8024  

-----

### 실행시 주의점.  
1. 템플릿 엔진으로 만들어진 파일로는 정상적으로 실행이 안됩니다.  
Aggregate 파일에서 Create 하는 Command 를 꼭 생성자로 커맨드를 발행하여 주세요.  

````java
@CommandHandler
public OrderAggregate(OrderPlacedCommand command){
    OrderPlacedEvent orderPlaced = new OrderPlacedEvent();
    orderPlaced.setId(command.getId());
    AggregateLifecycle.apply(orderPlaced);
}
````

2. controller 에서 command 를 구현하여 주어야 합니다.  

3. axon 서버는 ID 값을 기준으로 이벤트를 소싱 하기 때문에 아래와 같은 에러가 발생시 ID 값을 중복으로 POST 한 것은 아닌지 확인해 보시길 바랍니다.   

```
CommandExecutionException(OUT_OF_RANGE: [AXONIQ-2000] Invalid sequence number 0 for aggregate 2, expected 1)
```

