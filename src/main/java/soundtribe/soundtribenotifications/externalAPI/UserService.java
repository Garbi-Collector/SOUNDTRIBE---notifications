package soundtribe.soundtribenotifications.externalAPI;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import soundtribe.soundtribenotifications.dtos.userExperience.GetAll;

@Service
@Slf4j
public class UserService {

    @Value("${user.back.url}")
    private String userBackUrl;

    private final RestTemplate restTemplate;

    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GetAll getAllUsers(String jwt) {
        String url = userBackUrl + "user/all/jwt";

        log.info("Llamando a {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<GetAll> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    GetAll.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
                log.error("Error al obtener usuarios. Código: {}", response.getStatusCode());
                throw new RuntimeException("Error al obtener los usuarios");
            }

        } catch (Exception e) {
            log.error("Excepción al obtener usuarios", e);
            throw new RuntimeException("Fallo al conectarse al microservicio de usuarios", e);
        }
    }
}
