package project.Scul.domain.user.controller.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import project.Scul.domain.culture.domain.Culture;

@Getter
@NoArgsConstructor
public class GetBookmarkResponse {
    private String place;
    private String address;
    private Boolean isReservable;
    private Integer usageFee;
    private String tag;
    private String imageUrl;
//    "place" : "서울 시립 미술관",
//            "location" : "서울특별시 콩콩구 팥팥로"
//            "isReservable" : true,
//            "usageFee" : 0,
//            "tag" : "장애인, 유아",
//            "imageUrl" : "https://~~"

    public GetBookmarkResponse(Culture culture) {
        place = culture.getPlace();
        address = culture.getAddress();
        isReservable = culture.isReservable(); // 연관된 장소명
        usageFee = culture.getUsageFee();
        tag = culture.getTag();
        imageUrl = culture.getImageUrl();
    }
}
