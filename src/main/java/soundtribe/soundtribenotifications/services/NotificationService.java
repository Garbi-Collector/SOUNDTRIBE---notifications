package soundtribe.soundtribenotifications.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import soundtribe.soundtribenotifications.dtos.common.NotificationGet;
import soundtribe.soundtribenotifications.entities.NotificationType;

import java.util.List;

@Service
public interface NotificationService {

    void crearNotificacion(
            String token,
            List<Long> receivers,
            NotificationType type,
            String slugSong,
            String nameSong,
            String slugAlbum,
            String nameAlbum
    );

    @Async
    List<NotificationGet> GetNotification(String jwt);
}
