package com.gmail.muha.booking.service.room.impl;

import com.gmail.muha.booking.dto.room.RoomAvailableDto;
import com.gmail.muha.booking.dto.room.RoomSearchCriteriaDto;
import com.gmail.muha.booking.mapper.RoomMapper;
import com.gmail.muha.booking.model.entity.Room;
import com.gmail.muha.booking.model.repository.RoomRepository;
import com.gmail.muha.booking.service.hotel.HotelReviewService;
import com.gmail.muha.booking.service.room.RoomSearchService;
import com.gmail.muha.booking.service.room.result.RoomOption;
import com.gmail.muha.booking.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RoomSearchServiceImpl implements RoomSearchService {

    private final Validator validator;
    private final RoomMapper roomMapper;
    private final RoomRepository roomRepository;
    private final HotelReviewService hotelReviewService;


    @Override
    public List<RoomAvailableDto> findAvailableRooms(RoomSearchCriteriaDto criteria) {

        validator.validateBookingDates(
                criteria.getStartDate(),
                criteria.getEndDate()
        );

        List<Room> availableRooms = roomRepository.findAvailableRooms(
                criteria.getCityId(),
                criteria.getStartDate(),
                criteria.getEndDate(),
                criteria.getRoomCapacity(),
                criteria.getRoomType()
        );

        Set<RoomOption> uniqueRoomOptions = new HashSet<>();

        List<Room> uniqueRooms = availableRooms.stream()
                .filter(room -> uniqueRoomOptions.add(
                        new RoomOption(
                                room.getHotel().getId(),
                                room.getRoomType(),
                                room.getRoomCapacity())))
                .toList();

        long numberOfNights = ChronoUnit.DAYS.between(criteria.getStartDate(), criteria.getEndDate());

        List<RoomAvailableDto> roomAvailableDtos = roomMapper.toAvailableDtoList(uniqueRooms);

        for (RoomAvailableDto roomAvailableDto : roomAvailableDtos) {

            roomAvailableDto.
                    setTotalStayPrice(roomAvailableDto.getPricePerNight().multiply(BigDecimal.valueOf(numberOfNights)));

            roomAvailableDto.
                    setHotelReviewCount(hotelReviewService.countCommentsByHotelId(roomAvailableDto.getHotelId()));
        }
        return roomAvailableDtos;
    }
}
