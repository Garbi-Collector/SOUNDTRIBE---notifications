package soundtribe.soundtribenotifications.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import soundtribe.soundtribenotifications.services.NotificationService;

@RestController
@RequestMapping("/eliminate-notification") // Puerto 8083
public class EliminateAccountController {

    @Autowired
    private NotificationService notificationService;

    @DeleteMapping
    public ResponseEntity<?> eliminarDonacionesDelUsuario(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Token no proporcionado o inválido");
        }

        String jwt = token.replace("Bearer ", "");

        try {
            notificationService.eliminarNotificaciones(jwt);
            return ResponseEntity.ok("notificaciones actualizadas y eliminadas correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error al eliminar donor: " + e.getMessage());
        }
    }
}
