package ypjs.project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.util.StringUtils;
import ypjs.project.domain.enums.DeliveryStatus;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

@Entity
@Table(name = "delivery")
@Getter
@NoArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long deliveryId;  //배송번호

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;  //주문번호

    @Column(name = "delivery_receiver")
    private String receiver;  //받으실 분

    @Column(name = "delivery_phonenumber")
    private String phoneNumber;  //휴대전화 번호

    @Embedded
    @Column(name = "delivery_address")
    private Address address;  //배송주소

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status")
    private DeliveryStatus status;  //배송상태

    @Column(name = "delivery_carrier_id")
    private String carrierId;  //배송사ID

    @Column(name = "delivery_track_id")
    private String trackId;  //운송장번호


    //==생성자==//
    public Delivery(Long deliveryId, Order order, String receiver, String phoneNumber, Address address, DeliveryStatus status, String carrierId, String trackId) {
        this.deliveryId = deliveryId;
        this.order = order;
        this.receiver = receiver;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.status = status;
        this.carrierId = carrierId;
        this.trackId = trackId;
    }

    public Delivery(String receiver, String phoneNumber, Address address, DeliveryStatus deliveryStatus) {
        this.receiver = receiver;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.status = deliveryStatus;
    }


    //==연관관계 메서드==//
    public void setOrder(Order order) {
        this.order = order;
    }


    public void addTrackInfo(String carrierId, String trackId) {
        this.carrierId = carrierId;
        this.trackId = trackId;
    }


    public void updateStatus() {
        System.out.println("**배송상태 업데이트 요청됨");
        if(StringUtils.hasText(this.trackId)) {
            System.out.println("**운송장번호 확인");
            JSONObject trackLog = Tracker.trackLog(this.carrierId, this.trackId);
            System.out.println("**배송정보 조회");

            String status = trackLog.getString("status");
            System.out.println(status);

            if(status.equals("delivered")) {
                this.status = DeliveryStatus.배송완료;
            } else {
                this.status = DeliveryStatus.배송중;
            }
        }

    }


    @PostPersist  // 영속화 후 상태 업데이트
    @PostUpdate
    @PostLoad  // 엔티티 로드 후 상태 업데이트
    private void updateStatusAfterPersistAndLoad() {
        updateStatus();
    }


//=========================================================================================

    public class Tracker {

        /*
         * 택배사 API 조회
         * url : TRACKER_URL / carrier_id : tracker code / track_id : 송장번호
         *
         * https://apis.tracker.delivery/carriers/:carrier_id/tracks/:track_id
         *
         * */

        private static String apiURL (String carrierId, String trackId) {
            return "https://apis.tracker.delivery/carriers/"
                    + carrierId
                    + "/tracks/"
                    +trackId;
        }

        private static HashMap<String, String> carrier = new HashMap<String, String>(){{//초기값 지정
            put("kr.cjlogistics","CJ대한통운");  //id, name
            put("kr.epost","우체국택배");
            put("kr.hanjin","한진택배");
            put("kr.logen","로젠택배");
            put("kr.lotte","롯데택배");
        }};

        public static JSONObject trackLog(String carrierId, String trackId) {
            System.out.println("**운송장 조회 요청됨");

            JSONObject track = new JSONObject();

            String responseBody = getMethod(apiURL(carrierId,trackId));

            JSONObject responseJson = new JSONObject(responseBody);

            String message = responseJson.has("message") ? responseJson.get("message").toString() : "";

            String carrierName = carrier.get(carrierId);

            //내용이 없는 경우
            if(!message.equals("")) {
                track.put("trackId", trackId);
                track.put("time", "미정");
                track.put("location", "미정");
                track.put("status", "at_packing");
                track.put("carrierId", carrierId);
                track.put("carrierName", carrierName);

                JSONObject tempJson = new JSONObject();  //progress 저장용
                JSONObject tempJson2 = new JSONObject();  //progress.location 저장용
                JSONObject tempJson3 = new JSONObject();  //progress.status 저장용

                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                String currentDateTime = format.format(Calendar.getInstance().getTime());

                //progress.location
                tempJson2.put("name","배송준비중");

                //progress.status
                tempJson3.put("id", "at_packing");
                tempJson3.put("text", "배송준비중");

                //progress
                tempJson.put("description", "");
                tempJson.put("location",tempJson2);
                tempJson.put("time", currentDateTime);
                tempJson.put("status", tempJson3);

                track.put("progresses", tempJson);

                return track;

            }

            //내용이 있는 경우 -> Tracker API 결과값 responseJson 옮기기
            JSONObject carrierJson = new JSONObject(responseJson.get("carrier").toString());
            JSONArray progressesJson = new JSONArray(responseJson.get("progresses").toString());
            JSONObject stateJson = new JSONObject(responseJson.get("state").toString());

            for(int i = 0 ; i < progressesJson.length() ; i++) {
                JSONObject p = progressesJson.getJSONObject(i);
                JSONObject pStatus = p.getJSONObject("status");

                if(stateJson.get("id").equals(pStatus.get("id"))) {
                    try {
                        JSONObject pLocation = p.getJSONObject("location");

                        track.put("time", p.getString("time"));
                        track.put("location", pLocation.getString("name"));
                        track.put("carrierName", carrierName);
                        track.put("progresses", progressesJson);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            track.put("status", stateJson.getString("id"));
            track.put("carrierId", carrierJson.getString("id"));
            track.put("trackId", trackId);

            return track;

        }

        private static String getMethod(String aipUrl) {
            System.out.println(aipUrl);

            try {
                URL url = new URL(aipUrl);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                int code = connection.getResponseCode();
                if(code == 200) {
                    String api = readAPIBody(connection.getInputStream());
                    return api;
                } else {
                    return "{\"message\":\"보내시는 고객님께서 상품 배송준비중입니다.\"}";
                }

            } catch (IOException e) {
                throw new RuntimeException("API 요청 및 응답 실패");
            }
        }

        //Response된 API 정보를 읽는 메서드
        private static  String readAPIBody(InputStream body) {

            InputStreamReader inputStreamReader = new InputStreamReader(body);

            try (BufferedReader lineReader = new BufferedReader(inputStreamReader)) {

                StringBuilder responseBody = new StringBuilder();

                String line;
                while((line = lineReader.readLine()) != null) {
                    responseBody.append(line);
                }

                return responseBody.toString();
            } catch (IOException e) {
                throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
            }
        }





    }


}