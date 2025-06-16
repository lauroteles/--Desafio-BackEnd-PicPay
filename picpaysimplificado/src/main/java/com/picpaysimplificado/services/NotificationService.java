package com.picpaysimplificado.services;

import com.picpaysimplificado.DTO.NotificationDTO;
import com.picpaysimplificado.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLOutput;

@Service
public class NotificationService {

    @Autowired
    private RestTemplate restTemplate;

    public void sendNotification(User user, String message) throws Exception{
        String email = user.getEmail();
        NotificationDTO notificatonRequest = new NotificationDTO(email, message);


       ResponseEntity<String> notificationResponse = restTemplate.postForEntity("https://util.devi.tools/api/v1/notify",notificatonRequest, String.class);
       if (notificationResponse.getStatusCode() == HttpStatus.OK) {

           System.out.println("Erro ao enviar notificação");
           throw new Exception("Serviço de notificação está fora do ar");
       }
    }


}
