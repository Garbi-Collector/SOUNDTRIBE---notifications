package soundtribe.soundtribenotifications.services;

import org.springframework.stereotype.Service;
import soundtribe.soundtribenotifications.entities.NotificationType;

@Service
public interface NotificationTypeService {
    String MessageByType(NotificationType type);
}
