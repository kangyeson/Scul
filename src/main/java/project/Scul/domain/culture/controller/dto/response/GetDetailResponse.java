package project.Scul.domain.culture.controller.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import project.Scul.domain.culture.domain.Culture;

@Getter
@NoArgsConstructor
public class GetDetailResponse {
    private Long cultureId;
    private String place;
    private String phoneNumber;
    private String usageTime;
    private String registrationSchedule;
    private String operationSchedule;
    private Integer usageFee;
    private String tag;
    private String imageUrl;
    private String address;
    private String cultureContent;
    private boolean isBookmarked;

    public GetDetailResponse(Culture culture, boolean isBookmarked)
    {
        cultureId = culture.getCultureId();
        place = culture.getPlace();
        phoneNumber = culture.getPhoneNumber();
        usageTime = culture.getUsageTime();
        registrationSchedule = culture.getRegistrationSchedule();
        operationSchedule = culture.getOperationSchedule();
        usageFee = culture.getUsageFee();
        tag = culture.getTag();
        imageUrl = culture.getImageUrl();
        address = culture.getAddress();
        cultureContent = culture.getCultureContent();
        isBookmarked = isBookmarked;
    }
}
