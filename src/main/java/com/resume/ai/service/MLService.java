package com.resume.ai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class MLService {

    @Value("${ml.service.url}")
    private String mlServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> predictCategory(org.springframework.web.multipart.MultipartFile file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        org.springframework.util.MultiValueMap<String, Object> body = new org.springframework.util.LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpEntity<org.springframework.util.MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(mlServiceUrl + "/predict", request, Map.class);

        return response.getBody();
    }

    public Map<String, Object> predictCategory(String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("text", text);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(mlServiceUrl + "/predict", request, Map.class);

        return response.getBody();
    }

    public Map<String, Object> calculateScore(String resumeText, String jobDescription) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("resume_text", resumeText);
        body.put("job_description", jobDescription);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(mlServiceUrl + "/rank", request, Map.class);

        return response.getBody();
    }

    public Map<String, Object> getMetrics() {
        return restTemplate.getForObject(mlServiceUrl + "/metrics", Map.class);
    }
}

