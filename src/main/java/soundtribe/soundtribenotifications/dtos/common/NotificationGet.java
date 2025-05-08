package soundtribe.soundtribenotifications.dtos.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationGet {

    private Long id;// id de la noti

    private Long receiver;//id del usuario que deberia ver lka notificacion

    private String message;
    private String slug;
}
