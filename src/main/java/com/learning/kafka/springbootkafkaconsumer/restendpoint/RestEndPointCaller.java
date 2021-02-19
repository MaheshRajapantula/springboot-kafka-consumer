package com.learning.kafka.springbootkafkaconsumer.restendpoint;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.kafka.springbootkafkaconsumer.Exception.RestEndPointInaccessibleException;
import com.learning.kafka.springbootkafkaconsumer.Exception.RestEndPointNotFoundException;
import com.learning.kafka.springbootkafkaconsumer.model.Urn;
import com.learning.kafka.springbootkafkaconsumer.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class RestEndPointCaller {

    @Value("${rest.url}")
    private String HOST;

    @Value("${rest.end.point}")
    private String END_POINT;

    public User GetUserData(Urn urn) throws RestEndPointNotFoundException, RestEndPointInaccessibleException {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(HOST+END_POINT);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("urn",urn.getUrn());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try{
            ResponseEntity<byte[]> result = restTemplate.exchange(uriComponentsBuilder.toUriString(), HttpMethod.GET, entity, byte[].class);
            System.out.println("result" + result);
            ObjectMapper mapper = new ObjectMapper();
            User userData = mapper.readValue(result.getBody(), User.class);
            return userData;
        }
        catch (HttpClientErrorException e)
        {
            // Wrong request format sent to Rest End Point
            throw new RestEndPointNotFoundException("JsonNotFound");
        }
        catch (Exception e)
        {
            // Rest End Point not running (ResourceAccessException)
            throw new RestEndPointInaccessibleException("Rest End Point not found");

        }
    }
}
