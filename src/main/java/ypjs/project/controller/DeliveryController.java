package ypjs.project.controller;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ypjs.project.domain.Delivery;
import ypjs.project.dto.deliverydto.DeliveryResponseDto;
import ypjs.project.service.DeliveryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ypjs/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping("/tracker")
    public ResponseEntity tracker(@RequestParam(name = "deliveryId") Long deliveryId) {
        System.out.println("**배송 조회 요청됨");
        DeliveryResponseDto d = deliveryService.findOne(deliveryId);
        if(d.getCarrierId() != null && d.getTrackId() !=null) {
            System.out.println("**배송 추적 가능");
            JSONObject trackLog = Delivery.Tracker.trackLog(d.getCarrierId(),d.getTrackId());
            System.out.println(trackLog);
            return ResponseEntity.ok(trackLog.toString());
        } else {
            System.out.println("**배송 추적 불가 - 배송준비중");
            return ResponseEntity.ok().build();
        }
    }
}
