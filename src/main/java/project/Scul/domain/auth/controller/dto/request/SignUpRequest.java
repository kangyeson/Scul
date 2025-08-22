package project.Scul.domain.auth.controller.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequest {
    private String accountId;
    private String password;
    private String name;
}
