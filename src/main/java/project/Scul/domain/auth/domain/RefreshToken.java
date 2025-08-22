package project.Scul.domain.auth.domain;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Indexed;

@Getter
@Builder
public class RefreshToken {
    @Id
    String Id;

}
