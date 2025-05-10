package soundtribe.soundtribenotifications.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private Long sender; // ID del usuario que generó la notificación (de otro microservicio)

    @ElementCollection
    @CollectionTable(name = "notification_receivers", joinColumns = @JoinColumn(name = "notification_id"))
    @Column(name = "receiver_id")
    private List<Long> receivers; // IDs de los destinatarios

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(nullable = true)
    private String redirectUrl;

    @Column(name = "is_read")
    private Boolean isRead;
}
