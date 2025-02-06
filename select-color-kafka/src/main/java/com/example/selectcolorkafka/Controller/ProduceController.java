package com.example.selectcolorkafka.Controller;

import com.example.selectcolorkafka.VO.UserEventVO;  // UserEventVO 클래스를 사용하기 위한 import
import com.google.gson.Gson;  // JSON 변환을 위한 Gson 라이브러리 import
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController  // RESTful API 컨트롤러로 동작하도록 설정
@CrossOrigin(origins = "*", allowedHeaders = "*")  // CORS 정책 허용 (모든 출처에서 요청 허용)
public class ProduceController {

    // 로깅을 위한 SLF4J Logger 생성
    private final Logger logger = LoggerFactory.getLogger(ProduceController.class);

    // Kafka 메시지 전송을 위한 KafkaTemplate 주입
    private final KafkaTemplate<String, String> kafkaTemplate;

    // 생성자를 통해 KafkaTemplate 주입
    public ProduceController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * GET 요청을 처리하는 엔드포인트
     *
     * @param userAgentName - 요청을 보낸 사용자의 user-agent 정보
     * @param colorName     - 선택한 색상 정보
     * @param userName      - 사용자 이름
     */
    @GetMapping("/api/select")
    public void selectColor(
            @RequestHeader("user-agent") String userAgentName,  // HTTP 헤더에서 user-agent 정보 추출
            @RequestParam(value = "color") String colorName,  // 요청 파라미터에서 'color' 값 추출
            @RequestParam(value = "user") String userName) {  // 요청 파라미터에서 'user' 값 추출

        // 날짜 형식을 "yyyy-MM-dd'T'HH:mm:ss.SSSZZ" 형태로 설정
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
        Date now = new Date();  // 현재 시간 가져오기

        // Gson 객체 생성 (JSON 변환을 위해 사용)
        Gson gson = new Gson();

        // 사용자 이벤트 정보를 포함한 객체 생성
        UserEventVO userEventVO = new UserEventVO(sdfDate.format(now), userAgentName, colorName, userName);

        // UserEventVO 객체를 JSON 문자열로 변환
        String jsonColorLog = gson.toJson(userEventVO);

        try {
            // Kafka 메시지 동기 전송 (최대 10초 동안 응답 대기)
            kafkaTemplate.send("select-color", jsonColorLog).get(10, TimeUnit.SECONDS);

            // 메시지 전송 성공 시 로그 출력 (별도 로깅 없음)

        } catch (ExecutionException e) {
            // Kafka 브로커에서 발생한 오류를 처리 (예: 메시지 전송 실패)
            logger.error("Message failed to send due to broker error: {}", e.getCause().getMessage());

        } catch (InterruptedException e) {
            // 현재 스레드가 인터럽트된 경우 처리 (인터럽트 상태를 복원)
            Thread.currentThread().interrupt();
            logger.error("Message sending was interrupted: {}", e.getMessage());

        } catch (TimeoutException e) {
            // 메시지 전송이 지정된 시간(10초) 내에 완료되지 않았을
            logger.error("Message sending was timeout: {}", e.getMessage());
        }
    }
}