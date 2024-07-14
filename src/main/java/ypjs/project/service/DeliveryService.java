package ypjs.project.service;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ypjs.project.domain.Delivery;
import ypjs.project.domain.enums.DeliveryStatus;
import ypjs.project.dto.deliverydto.DeliveryResponseDto;
import ypjs.project.dto.deliverydto.DeliveryTrackerDto;
import ypjs.project.repository.DeliveryRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public DeliveryResponseDto findOne(Long deliveryId) {
        Delivery delivery = deliveryRepository.findOne(deliveryId);
        return new DeliveryResponseDto(delivery);
    }

    @Transactional
    public Long addTracker (DeliveryTrackerDto deliveryTrackerDto) {
        Delivery d = deliveryRepository.findOne(deliveryTrackerDto.getDeliveryId());
        d.addTrackInfo(d.getCarrierId(), d.getTrackId());

        return  d.getDeliveryId();
    }

    @Transactional
    public void updateStatus (Long deliveryId) {
        Delivery d = deliveryRepository.findOne(deliveryId);

        if(d.getTrackId() != null && d.getTrackId().isEmpty()) {
            JSONObject trackLog = Delivery.Tracker.trackLog(d.getCarrierId(),d.getTrackId());

            String status = trackLog.getString("status");

            if(status.equals("배송완료")){
                d.updateStatus(DeliveryStatus.배송완료);
            } else {
                d.updateStatus(DeliveryStatus.배송중);
            }
        }

    }
}