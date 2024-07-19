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

import java.util.List;

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
        d.addTrackInfo(deliveryTrackerDto.getCarrierId(), deliveryTrackerDto.getTrackId());

        return  d.getDeliveryId();
    }

    @Transactional
    public void updateStatus() {
        List<Delivery> deliveries = deliveryRepository.findAll();
        for(Delivery d : deliveries) {
            d.updateStatus();
        }
    }


}