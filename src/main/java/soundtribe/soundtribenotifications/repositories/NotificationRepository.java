package soundtribe.soundtribenotifications.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import soundtribe.soundtribenotifications.entities.Notification;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n JOIN n.receivers r WHERE r = :receiverId ORDER BY n.createdAt DESC")
    List<Notification> findAllByReceiverId(Long receiverId);
}
