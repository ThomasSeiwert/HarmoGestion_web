package fr.afpa.cda19.harmogestionweb.utilities;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "fr.afpa.cda19.harmogestionweb")
@Data
public class CustomProperties {

    private String apiUrl;
}
