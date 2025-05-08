package soundtribe.soundtribenotifications.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import soundtribe.soundtribenotifications.dtos.common.NotificationGet;
import soundtribe.soundtribenotifications.entities.Notification;
import soundtribe.soundtribenotifications.entities.NotificationType;
import soundtribe.soundtribenotifications.externalAPI.ExternalJWTService;
import soundtribe.soundtribenotifications.repositories.NotificationRepository;
import soundtribe.soundtribenotifications.services.NotificationService;
import soundtribe.soundtribenotifications.services.NotificationTypeService;

import java.util.List;
import java.util.Map;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Qualifier("notificationTypeService")
    @Autowired
    NotificationTypeService typeService;

    @Autowired
    NotificationRepository repository;

    @Autowired
    ExternalJWTService jwtService;

    @Async
    @Override
    public void crearNotificacion(
            String token,
            List<Long> receivers,
            NotificationType type,
            String slugSong,
            String nameSong,
            String slugAlbum,
            String nameAlbum
    ){
        Map<String, Object> userInfo = jwtService.validateToken(token);
        Boolean isUSer = (Boolean) userInfo.get("valid");
        String username = (String) userInfo.get("username");
        String url = (String) userInfo.get("slug");
        Long userId = (Long) userInfo.get("userId");
        if (!Boolean.TRUE.equals(isUSer)) {
            throw new RuntimeException("No eres un usuario");
        }

        Notification noti = switch (type) {
            case DONATION ->  donationNotification(username, receivers,url,userId);
            case RECORD -> recordNotification(username, receivers, url, userId);
            case NEW_ALBUM -> newAlbumNotification(username, receivers, userId, slugAlbum, nameAlbum);
            case FOLLOW -> followNotification(username, receivers, userId, url);
            case LIKE_SONG -> likeSongNotification(username, receivers, userId, url, nameSong);
            case LIKE_ALBUM -> likeAlbumNotification(username, receivers, userId, slugAlbum, nameAlbum);
        };

        repository.save(noti);
    }

    @Async
    @Override
    public List<NotificationGet> GetNotification(String jwt){
        Map<String, Object> userInfo = jwtService.validateToken(jwt);
        Boolean isUSer = (Boolean) userInfo.get("valid");
        if (!Boolean.TRUE.equals(isUSer)) {
            throw new RuntimeException("No eres un usuario");
        }
        Long userId = (Long) userInfo.get("userId");

        List<Notification> notifications = repository.findAllByReceiverId(userId);
        return mapListNotifications(notifications,userId);
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
                .build();
    }



    private Notification likeAlbumNotification(String liker, List<Long> receivers, Long likerId, String likerUrl, String nameAlbum) {
        return Notification.builder()
                .message("a "+ liker +" "+ typeService.MessageByType(NotificationType.LIKE_ALBUM)+ nameAlbum)
                .sender(likerId)
                .receivers(receivers)
                .type(NotificationType.LIKE_ALBUM)
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
                .message(who +" "+ typeService.MessageByType(NotificationType.RECORD))
                .sender(userId)
                .receivers(receivers)
                .type(NotificationType.RECORD)
                .redirectUrl(url)
                .build();
    }

    private Notification donationNotification(String who, List<Long> receivers, String url, Long userId) {
        return Notification.builder()
                .message(who +" "+ typeService.MessageByType(NotificationType.DONATION))
                .sender(userId)
                .receivers(receivers)
                .type(NotificationType.DONATION)
                .redirectUrl(url)
                .build();
    }


}
