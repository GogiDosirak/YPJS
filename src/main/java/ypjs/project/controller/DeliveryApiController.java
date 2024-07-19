package ypjs.project.controller;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ypjs.project.domain.Delivery;
import ypjs.project.dto.deliverydto.DeliveryResponseDto;
import ypjs.project.dto.deliverydto.DeliveryTrackerDto;
import ypjs.project.service.DeliveryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ypjs/delivery")
public class DeliveryApiController {

    private final DeliveryService deliveryService;


    @PostMapping("/addTracker")
    public ResponseEntity addTracker(@RequestBody DeliveryTrackerDto deliveryTrackerDto) {
        System.out.println("**배송정보 등록 요청됨");
        System.out.println(deliveryTrackerDto);
        deliveryService.addTracker(deliveryTrackerDto);
        return ResponseEntity.ok().build();
    }
}
