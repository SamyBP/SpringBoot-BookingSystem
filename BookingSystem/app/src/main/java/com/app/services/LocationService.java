package com.app.services;

import com.app.dtos.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LocationService {
    @Value("${ip.api}")
    private String IP_API_URL;
    private final RestTemplate restTemplate;

    @Autowired
    public LocationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Location getUsersLocation(String ipAddress) {
        return restTemplate.getForObject(IP_API_URL + ipAddress, Location.class);
    }
}
