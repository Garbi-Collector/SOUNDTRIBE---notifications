package soundtribe.soundtribenotifications.entities;

/**
 * <h1>Tipos de Notificaciones</h1>
 *
 * <h2>Notificaciones públicas</h2>
 * <ul>
 *   <li><b>RECORD</b>: Muestra un récord en un juego. Visible para todos los usuarios.</li>
 *   <li><b>DONATION</b>: Muestra que un usuario ha donado a la app (tipo publicidad).</li>
 * </ul>
 *
 * <h2>Para seguidores de artistas</h2>
 * <ul>
 *   <li><b>NEW_ALBUM</b>: Notificación enviada a los seguidores cuando un artista sube un nuevo álbum.</li>
 * </ul>
 *
 * <h2>Notificaciones personales</h2>
 * <ul>
 *   <li><b>FOLLOW</b>: Cuando un usuario comienza a seguirte.</li>
 *   <li><b>LIKE_SONG</b>: Cuando le dan Me Gusta a una de tus canciones (si sos artista).</li>
 *   <li><b>LIKE_ALBUM</b>: Cuando le dan Me Gusta a uno de tus álbumes (si sos artista).</li>
 * </ul>
 */
public enum NotificationType {
    DONATION,
    RECORD,

    NEW_ALBUM,

    FOLLOW,
    LIKE_SONG,
    LIKE_ALBUM,
}
