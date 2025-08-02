package project.Scul.domain.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity(name = "tbl_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(columnDefinition = "VARCHAR(60)")
    private String accountId;

    @Column(columnDefinition = "VARCHAR(60)")
    private String password;

    @Column(columnDefinition = "VARCHAR(60)")
    private String name;

    @Builder
    public User(Long userId, String accountId, String password, String name) {
        this.userId = userId;
        this.accountId = accountId;
        this.password = password;
        this.name = name;
    }
}
