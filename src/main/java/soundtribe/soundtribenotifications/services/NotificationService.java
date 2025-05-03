package soundtribe.soundtribenotifications.services;

import org.springframework.stereotype.Service;
import soundtribe.soundtribenotifications.entities.NotificationType;

import java.util.List;

@Service
public interface NotificationService {
    void crearNotificacion(
            String token,
            List<Long> receivers,
            NotificationType type,
            String redirectUrl
    );
}
