package soundtribe.soundtribenotifications.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soundtribe.soundtribenotifications.dtos.NotificationPost;
import soundtribe.soundtribenotifications.dtos.NotificationGet;
import soundtribe.soundtribenotifications.services.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // para backs externos
    @PostMapping
    public ResponseEntity<String> createNotification(
            @RequestHeader("Authorization") String token,
            @RequestBody  NotificationPost noti
    ) {
        notificationService.crearNotificacion(
                cleanToken(token),
                noti
        );
        return ResponseEntity.ok("Notificación creada correctamente");
    }


    @GetMapping("/getmy")
    public ResponseEntity<List<NotificationGet>> getNotifications(
            @RequestHeader("Authorization") String token
    ) {
        List<NotificationGet> notis = notificationService.GetNotification(cleanToken(token));
        return ResponseEntity.ok(notis);
    }

    // Utilidad para limpiar el "Bearer "
    private String cleanToken(String token) {
        return token.startsWith("Bearer ") ? token.substring(7) : token;
    }
}
