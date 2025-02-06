package com.example.kafkatodb.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_color_selection")
@Getter
@Setter
@NoArgsConstructor
public class UserColorSelection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 자동 증가 ID

    private LocalDateTime timestamp; // Kafka에서 받은 시간

    @Column(nullable = false)
    private String userAgent; // User-Agent 정보

    @Column(nullable = false)
    private String colorName; // 선택한 색상

    @Column(nullable = false)
    private String userName; // 유저 이름

    // 생성자 추가
    public UserColorSelection(LocalDateTime timestamp, String userAgent, String colorName, String userName) {
        this.timestamp = timestamp;
        this.userAgent = userAgent;
        this.colorName = colorName;
        this.userName = userName;
    }
}