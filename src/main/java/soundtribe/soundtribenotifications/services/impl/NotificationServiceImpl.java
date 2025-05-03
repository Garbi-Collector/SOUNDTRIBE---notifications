package soundtribe.soundtribenotifications.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import soundtribe.soundtribenotifications.entities.NotificationType;
import soundtribe.soundtribenotifications.externalAPI.ExternalJWTService;
import soundtribe.soundtribenotifications.repositories.NotificationRepository;
import soundtribe.soundtribenotifications.services.NotificationService;
import soundtribe.soundtribenotifications.services.NotificationTypeService;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Qualifier("notificationTypeService")
    @Autowired
    NotificationTypeService typeService;

    @Autowired
    NotificationRepository repository;

    @Autowired
    ExternalJWTService jwtService;

    @Override
    public void crearNotificacion(
            String token,
            List<Long> receivers,
            NotificationType type,
            String redirectUrl
    ){
               

    }


}
