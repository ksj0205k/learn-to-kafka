## 카프카를 활용하여 Spring Application 사이의 미들웨어 통신

1. 사용자의 입력(HTML + JavaScript)
   - 사용자 이름과 선택한 색깔을 입력
   - REST API를 통해 Kafka Producer Application으로 데이터 전송

2. Kafka Producer Application
   - HTTP 요청을 받아 데이터를 Kafka topic에 저장
   - 이때 토픽의 이름은 "select-color"

3. Kafka Broker
   - Producer가 보낸 메세지를 topic에 저장하고, Consumer가 가져갈 때 까지 유지

4. Kafka Consumer Application
   - Producer에서 보낸 메세지를 Kafka에서 구독
   - 메세지를 받아서 MariaDB에 저장
  
   - 
<img width="1141" alt="스크린샷 2025-02-06 오전 11 34 58" src="https://github.com/user-attachments/assets/ceff122f-7eea-4272-b6b9-091eaace13e7" />
