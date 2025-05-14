package soundtribe.soundtribenotifications.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import soundtribe.soundtribenotifications.entities.NotificationType;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationPost {
    List<Long> receivers;
    NotificationType type;
    String slugSong;
    String nameSong;
    String slugAlbum;
    String nameAlbum;
    String mensaje;
}
