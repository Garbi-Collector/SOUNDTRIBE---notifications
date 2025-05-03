package soundtribe.soundtribenotifications.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    private LocalDateTime createdAt = LocalDateTime.now();

    private Long sender; // ID del usuario que generó la notificación (de otro microservicio)

    @ElementCollection
    @CollectionTable(name = "notification_receivers", joinColumns = @JoinColumn(name = "notification_id"))
    @Column(name = "receiver_id")
    private List<Long> receivers; // IDs de los destinatarios

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String redirectUrl; // Ej: /perfil/usuario, /album/123
}
