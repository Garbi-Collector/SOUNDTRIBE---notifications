package soundtribe.soundtribenotifications.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import soundtribe.soundtribenotifications.dtos.NotificationPost;
import soundtribe.soundtribenotifications.dtos.NotificationGet;
import soundtribe.soundtribenotifications.entities.Notification;
import soundtribe.soundtribenotifications.entities.NotificationType;
import soundtribe.soundtribenotifications.externalAPI.ExternalJWTService;
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
        Long userId = (Long) userInfo.get("userId");
        if (!Boolean.TRUE.equals(isUSer)) {
            throw new RuntimeException("No eres un usuario");
        }

        Notification noti = switch (notiDto.getType()) {
            case DONATION ->  donationNotification(username, notiDto.getReceivers(),url,userId);
            case RECORD -> recordNotification(username, notiDto.getReceivers(), url, userId);
            case NEW_ALBUM -> newAlbumNotification(username, notiDto.getReceivers(), userId, notiDto.getSlugAlbum(), notiDto.getNameAlbum());
            case FOLLOW -> followNotification(username, notiDto.getReceivers(), userId, url);
            case LIKE_SONG -> likeSongNotification(username, notiDto.getReceivers(), userId, url, notiDto.getNameSong());
            case LIKE_ALBUM -> likeAlbumNotification(username, notiDto.getReceivers(), userId, notiDto.getSlugAlbum(), notiDto.getNameAlbum());
        };

        repository.save(noti);
    }


    @Override
    public List<NotificationGet> GetNotification(String jwt){
        Map<String, Object> userInfo = jwtService.validateToken(jwt);
        Boolean isUSer = (Boolean) userInfo.get("valid");
        if (!Boolean.TRUE.equals(isUSer)) {
            throw new RuntimeException("No eres un usuario");
        }
        Integer userIdInteger = (Integer) userInfo.get("userId");

        Long userId = userIdInteger.longValue();

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
                .type(noti.getType())
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

    private Notification donationNotification(String who, List<Long> receivers, String url, Long userId) {
        return Notification.builder()
                .message(who +" "+ typeService.MessageByType(DONATION))
                .sender(userId)
                .receivers(receivers)
                .type(DONATION)
                .redirectUrl(url)
                .build();
    }


}
