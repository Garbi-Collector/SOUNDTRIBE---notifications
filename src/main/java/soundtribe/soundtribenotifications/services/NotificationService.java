package soundtribe.soundtribenotifications.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soundtribe.soundtribenotifications.dtos.NotificationGet;
import soundtribe.soundtribenotifications.dtos.NotificationPost;
import soundtribe.soundtribenotifications.dtos.NotificationGet;
import soundtribe.soundtribenotifications.entities.NotificationType;

import java.util.List;

@Service
public interface NotificationService {

    @Async
    void crearNotificacion(
            String token,
            NotificationPost notiDto
    );

    List<NotificationGet> GetNotification(String jwt);

    void readNotification(String jwt, Long notificationId);

    @Transactional
    @Async
    void eliminarNotificaciones(String token);
}
