package com.sit.cloudnative.HealthCheckService.ServiceAPI;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VideoApi {

    private final String urlForCheck = "https://dreamteam-videoservice.mybluemix.net/";

    public String checkMessage() {
        RestTemplate restTemplate = new RestTemplate();
        String message = null;
        try {
            message = restTemplate.getForObject(urlForCheck, String.class);
        } catch (Exception ex) {
            
        }
        return message;
    }
}