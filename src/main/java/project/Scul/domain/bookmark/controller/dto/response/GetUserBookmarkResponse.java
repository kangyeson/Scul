package project.Scul.domain.bookmark.controller.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import project.Scul.domain.culture.domain.Culture;

@Getter
@NoArgsConstructor
public class GetUserBookmarkResponse {
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

    public GetUserBookmarkResponse(String place, String address, Boolean isReservable, Integer usageFee, String tag, String imageUrl) {
        this.place = place;
        this.address = address;
        this.isReservable = isReservable;
        this.usageFee = usageFee;
        this.tag = tag;
        this.imageUrl = imageUrl;
    }
}
