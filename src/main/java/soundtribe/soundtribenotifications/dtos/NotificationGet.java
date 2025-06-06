package soundtribe.soundtribenotifications.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import soundtribe.soundtribenotifications.entities.NotificationType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationGet {

    private Long id;// id de la noti

    private Long receiver;  //id del usuario que deberia ver lka notificacion

    private String message;

    private String slug;

    private NotificationType type;

    private Boolean isRead;

    private LocalDateTime createdAt;
}
