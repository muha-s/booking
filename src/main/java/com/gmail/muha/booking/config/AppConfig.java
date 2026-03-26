package com.gmail.muha.booking.config;

import com.gmail.muha.booking.controller.*;
import com.gmail.muha.booking.mapper.*;
import com.gmail.muha.booking.repository.*;
import com.gmail.muha.booking.repository.impl.*;
import com.gmail.muha.booking.service.*;
import com.gmail.muha.booking.service.impl.*;

public class AppConfig {

    private final CityMapper cityMapper = CityMapper.getInstance();
    private final CityRepository cityRepository = CityRepositoryImpl.getInstance();
    private final CityService cityService = new CityServiceImpl(cityRepository, cityMapper);
    private final CityController cityController = new CityController(cityService);

    private final HotelMapper hotelMapper = HotelMapper.getInstance();
    private final HotelRepository hotelRepository = HotelRepositoryImpl.getInstance();
    private final HotelService hotelService = new HotelServiceImpl(hotelRepository, hotelMapper, cityService);
    private final HotelController hotelController = new HotelController(hotelService);

    private final UserMapper userMapper = UserMapper.getInstance();
    private final UserRepository userRepository = UserRepositoryImpl.getInstance();
    private final UserService userService = new UserServiceImpl(userRepository, userMapper);
    private final UserController userController = new UserController(userService);

    private final RoomMapper roomMapper = RoomMapper.getInstance();
    private final RoomRepository roomRepository = RoomRepositoryImpl.getInstance();
    private final RoomService roomService = new RoomServiceImpl(roomRepository, roomMapper, hotelService);
    private final RoomController roomController = new RoomController(roomService);

    private final BookingMapper bookingMapper = BookingMapper.getInstance();
    private final BookingRepository bookingRepository = BookingRepositoryImpl.getInstance();
    private final BookingService bookingService = new BookingServiceImpl
            (userService, roomService, hotelService, bookingRepository, bookingMapper);
    private final BookingController bookingController = new BookingController(bookingService);


    public AppConfig() {

    }

    public HotelMapper getHotelMapper() {
        return hotelMapper;
    }

    public HotelRepository getHotelRepository() {
        return hotelRepository;
    }

    public HotelService getHotelService() {
        return hotelService;
    }

    public HotelController getHotelController() {
        return hotelController;
    }

    public UserMapper getUserMapper() {
        return userMapper;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public UserService getUserService() {
        return userService;
    }

    public UserController getUserController() {
        return userController;
    }

    public RoomMapper getRoomMapper() {
        return roomMapper;
    }

    public RoomRepository getRoomRepository() {
        return roomRepository;
    }

    public RoomService getRoomService() {
        return roomService;
    }

    public RoomController getRoomController() {
        return roomController;
    }

    public CityMapper getCityMapper() {
        return cityMapper;
    }

    public CityRepository getCityRepository() {
        return cityRepository;
    }

    public CityService getCityService() {
        return cityService;
    }

    public CityController getCityController() {
        return cityController;
    }

    public BookingMapper getBookingMapper() {
        return bookingMapper;
    }

    public BookingRepository getBookingRepository() {
        return bookingRepository;
    }

    public BookingService getBookingService() {
        return bookingService;
    }

    public BookingController getBookingController() {
        return bookingController;
    }
}
