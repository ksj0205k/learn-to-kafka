package com.example.kafkatodb.Consumer;


import com.example.kafkatodb.Entity.UserColorSelection;
import com.example.kafkatodb.Repository.UserColorSelectionRepository;
import com.example.kafkatodb.VO.UserEventVO;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ColorSelectionConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ColorSelectionConsumer.class);
    private final UserColorSelectionRepository userColorSelectionRepository;
    private final Gson gson = new Gson();

    public ColorSelectionConsumer(UserColorSelectionRepository userColorSelectionRepository) {
        this.userColorSelectionRepository = userColorSelectionRepository;
    }

    //Kafka 리스너 - "select-color" 토픽 구독
    @KafkaListener(topics = "select-color", groupId = "color-selection-group")
    public void consume(String message) {
        try{
            logger.info("Received Kafka Message: {}", message);

            // JSON 메시지를 Java 객체로 변환
            UserEventVO event = gson.fromJson(message, UserEventVO.class);

            // MariaDB에 저장할 객체 생성
            UserColorSelection userColorSelection = new UserColorSelection(
                    LocalDateTime.now(), event.getUserAgent(), event.getColorName(), event.getUserName()
            );

            // 데이터 저장
            userColorSelectionRepository.save(userColorSelection);
            logger.info("Saved to MariaDB: {}", userColorSelection);
        } catch (JsonSyntaxException e) {
            logger.error("Failed to parse JSON: {}", e.getMessage());
        } catch (Exception e){
            logger.error("Error while saving to database: {}", e.getMessage());
        }
    }
}
