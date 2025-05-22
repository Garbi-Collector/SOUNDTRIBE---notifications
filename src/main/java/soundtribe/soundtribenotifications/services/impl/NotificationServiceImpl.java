package soundtribe.soundtribenotifications.services.impl;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soundtribe.soundtribenotifications.dtos.NotificationPost;
import soundtribe.soundtribenotifications.dtos.NotificationGet;
import soundtribe.soundtribenotifications.dtos.userExperience.GetAll;
import soundtribe.soundtribenotifications.dtos.userExperience.UserGet;
import soundtribe.soundtribenotifications.entities.Notification;
import soundtribe.soundtribenotifications.entities.NotificationType;
import soundtribe.soundtribenotifications.externalAPI.ExternalJWTService;
import soundtribe.soundtribenotifications.externalAPI.UserService;
import soundtribe.soundtribenotifications.repositories.NotificationRepository;
import soundtribe.soundtribenotifications.services.NotificationService;
import soundtribe.soundtribenotifications.services.NotificationTypeService;

import java.util.List;
import java.util.Map;

import static soundtribe.soundtribenotifications.entities.NotificationType.*;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Qualifier("notificationTypeService")
    @Autowired
    NotificationTypeService typeService;

    @Autowired
    NotificationRepository repository;

    @Autowired
    ExternalJWTService jwtService;

    @Autowired
    UserService userService;

    @Transactional
    @Async
    @Override
    public void crearNotificacion(
            String token,
            NotificationPost notiDto
    ){
        Map<String, Object> userInfo = jwtService.validateToken(token);
        Boolean isUSer = (Boolean) userInfo.get("valid");
        String username = (String) userInfo.get("username");
        String url = (String) userInfo.get("slug");

        Integer userIdInteger = (Integer) userInfo.get("userId");

        GetAll allUsers = userService.getAllUsers(token);
        List<Long> idsAllUsers = allUsers.getUsuarios().stream()
                .map(UserGet::getId)
                .toList();

        Long userId = userIdInteger.longValue();

        if (!Boolean.TRUE.equals(isUSer)) {
            throw new RuntimeException("No eres un usuario");
        }

        Notification noti = switch (notiDto.getType()) {
            case DONATION ->  donationNotification(idsAllUsers,url,userId, notiDto.getMensaje());
            case RECORD -> recordNotification(username, idsAllUsers, url, userId);
            case NEW_ALBUM -> newAlbumNotification(username, notiDto.getReceivers(), userId, notiDto.getSlugAlbum(), notiDto.getNameAlbum());
            case FOLLOW -> followNotification(username, notiDto.getReceivers(), userId, url);
            case LIKE_SONG -> likeSongNotification(username, notiDto.getReceivers(), userId, url, notiDto.getNameSong());
            case LIKE_ALBUM -> likeAlbumNotification(username, notiDto.getReceivers(), userId, notiDto.getSlugAlbum(), notiDto.getNameAlbum());
        };
        noti.setIsRead(false);

        repository.save(noti);
    }


    @Transactional
    @Override
    public List<NotificationGet> GetNotification(String jwt){
        Long userId = validateJwt(jwt);

        List<Notification> notifications = repository
                .findAllByReceiverId(
                userId,
                PageRequest.of(0, 15)
        );
        return mapListNotifications(notifications,userId);
    }

    @NotNull
    private Long validateJwt(String jwt) {
        Map<String, Object> userInfo = jwtService.validateToken(jwt);
        Boolean isUSer = (Boolean) userInfo.get("valid");
        if (!Boolean.TRUE.equals(isUSer)) {
            throw new RuntimeException("No eres un usuario");
        }
        Integer userIdInteger = (Integer) userInfo.get("userId");

        return userIdInteger.longValue();
    }


    @Transactional
    @Override
    public void readNotification(String jwt, Long notificationId) {
        Long userId = validateJwt(jwt); // Validamos el usuario

        Notification noti = repository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        // Verificamos que el usuario sea receptor de esta notificación
        if (!noti.getReceivers().contains(userId)) {
            throw new RuntimeException("No tienes permiso para leer esta notificación");
        }

        // Marcamos como leída solo si aún no lo está
        if (Boolean.FALSE.equals(noti.getIsRead())) {
            noti.setIsRead(true);
            repository.save(noti);
        }
    }


    @Transactional
    @Async
    @Override
    public void eliminarNotificaciones(String token){
        Map<String, Object> userInfo = jwtService.validateToken(token);
        Boolean isUser = (Boolean) userInfo.get("valid");
        if (!Boolean.TRUE.equals(isUser)) {
            throw new RuntimeException("No eres un usuario");
        }

        Integer userIdInteger = (Integer) userInfo.get("userId");
        Long userId = userIdInteger.longValue();

        // 1. Eliminar notificaciones donde el usuario es el sender
        List<Notification> enviadas = repository.findBySender(userId);
        repository.deleteAll(enviadas);

        // 2. Obtener notificaciones donde el usuario está en la lista de receivers
        List<Notification> recibidas = repository.findByReceiversContaining(userId);

        for (Notification noti : recibidas) {
            noti.getReceivers().remove(userId); // removerlo de la lista
        }
        repository.saveAll(recibidas); // actualizar los cambios
    }



    private List<NotificationGet> mapListNotifications(List<Notification> notis, Long receiverId) {
        return notis.stream()
                .map(notification -> mapNotification(notification, receiverId))
                .toList();
    }


    private NotificationGet mapNotification(Notification noti, Long receiverId){
        return NotificationGet.builder()
                .id(noti.getId())
                .receiver(receiverId)
                .message(noti.getMessage())
                .slug(noti.getRedirectUrl())
                .type(noti.getType())
                .isRead(noti.getIsRead())
                .createdAt(noti.getCreatedAt())
                .build();
    }



    private Notification likeAlbumNotification(String liker, List<Long> receivers, Long likerId, String likerUrl, String nameAlbum) {
        return Notification.builder()
                .message("a "+ liker +" "+ typeService.MessageByType(LIKE_ALBUM)+ nameAlbum)
                .sender(likerId)
                .receivers(receivers)
                .type(LIKE_ALBUM)
                .redirectUrl(likerUrl)
                .build();
    }

    private Notification likeSongNotification(String liker, List<Long> receivers, Long likerId, String likerUrl, String nameSong) {
        return Notification.builder()
                .message("a "+ liker +" "+ typeService.MessageByType(NotificationType.LIKE_SONG)+ nameSong)
                .sender(likerId)
                .receivers(receivers)
                .type(NotificationType.LIKE_SONG)
                .redirectUrl(likerUrl)
                .build();
    }


    private Notification followNotification(String follower, List<Long> receivers, Long followerId, String url) {
        return Notification.builder()
                .message(follower +" "+ typeService.MessageByType(NotificationType.FOLLOW))
                .sender(followerId)
                .receivers(receivers)
                .type(NotificationType.FOLLOW)
                .redirectUrl(url)
                .build();
    }

    private Notification newAlbumNotification(String artist, List<Long> receivers, Long artistId, String slugAlbum, String nameAlbum) {
        return Notification.builder()
                .message(artist +" "+ typeService.MessageByType(NotificationType.NEW_ALBUM)+ nameAlbum)
                .sender(artistId)
                .receivers(receivers)
                .type(NotificationType.NEW_ALBUM)
                .redirectUrl(slugAlbum)
                .build();
    }

    private Notification recordNotification(String who, List<Long> receivers, String url, Long userId) {
        return Notification.builder()
                .message(who +" "+ typeService.MessageByType(RECORD))
                .sender(userId)
                .receivers(receivers)
                .type(RECORD)
                .redirectUrl(url)
                .build();
    }

    private Notification donationNotification(List<Long> receivers, String url, Long userId, String mensaje) {
        return Notification.builder()
                .message(mensaje)
                .sender(userId)
                .receivers(receivers)
                .type(DONATION)
                .redirectUrl(url)
                .build();
    }


}
