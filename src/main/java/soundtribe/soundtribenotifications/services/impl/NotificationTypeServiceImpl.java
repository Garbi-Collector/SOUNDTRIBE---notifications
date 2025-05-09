package soundtribe.soundtribenotifications.services.impl;

import org.springframework.stereotype.Service;
import soundtribe.soundtribenotifications.entities.NotificationType;
import soundtribe.soundtribenotifications.services.NotificationTypeService;

@Service("notificationTypeService")
public class NotificationTypeServiceImpl implements NotificationTypeService {

    /**
     * Devuelve un mensaje personalizado para el tipo de notificación especificado.
     *
     * @param type el tipo de notificación {@link NotificationType}
     * @return un mensaje predefinido para mostrar al usuario
     * @throws IllegalArgumentException si el tipo es desconocido
     */
    @Override
    public String MessageByType(NotificationType type) {
        return switch (type) {
            case DONATION ->  "ha donado para apoyar a soundtribe!";
            case RECORD -> "ha Roto el Record!";
            case NEW_ALBUM -> "ha subido un nuevo álbum: ";
            case FOLLOW -> "ha comenzado a seguirte!";
            case LIKE_SONG -> "le gustó una de tus canciones: ";
            case LIKE_ALBUM -> "le gustó uno de tus álbumes: ";
            default -> throw new IllegalArgumentException("Tipo de notificación no soportado: " + type);
        };
    }
}
