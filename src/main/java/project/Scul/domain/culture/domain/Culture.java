package project.Scul.domain.culture.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
@Entity(name = "tbl_culture")
public class Culture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cultureId;

    @Column(columnDefinition = "VARCHAR(60)")
    private String place; //장소명

    @Column(columnDefinition = "VARCHAR(60)")
    private String phoneNumber; //전화번호

    @Column(columnDefinition = "VARCHAR(60)")
    private String usageTime; // 이용시간

    @Column(columnDefinition = "VARCHAR(60)")
    private String registrationSchedule; //접수일정

    @Column(columnDefinition = "VARCHAR(60)")
    private String operationSchedule; //운영일정

    @Column(columnDefinition = "TRUE")
    private boolean isReservable;   //예약가능여부

    private Integer usageFee;  //요금 (0일 경우 무료로 표시)

    @Column(columnDefinition = "VARCHAR(60)")
    private String tag; //태그 -> 쉼표로 구분된 문자열 분리할 필요 있음

    @Column(columnDefinition = "VARCHAR(60)")
    private String imageUrl;

    @Column(columnDefinition = "VARCHAR(60)")
    private String address;  //주소

    @Column(columnDefinition = "VARCHAR(60)")
    private String cultureContent;

    @Builder
    public Culture(Long cultureId, String place, String phoneNumber, String usageTime, String registrationSchedule, String operationSchedule,
                   boolean isReservable, Integer usageFee, String tag, String imageUrl, String address, String cultureContent) {
        this.cultureId = cultureId;
        this.place = place;
        this.phoneNumber = phoneNumber;
        this.usageTime = usageTime;
        this.registrationSchedule = registrationSchedule;
        this.operationSchedule = operationSchedule;
        this.isReservable = isReservable;
        this.usageFee = usageFee;
        this.tag = tag;
        this.imageUrl = imageUrl;
        this.address = address;
        this.cultureContent = cultureContent;
    }
}
