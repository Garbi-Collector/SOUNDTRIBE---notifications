package soundtribe.soundtribenotifications.dtos.userExperience;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserGet {
    private Long id;
    private String username;
    private String urlFoto;
    private String slug;
    private Long followersCount;
}
