package com.picpaysimplificado.services;

import com.picpaysimplificado.DTO.TransacationDTO;
import com.picpaysimplificado.domain.transaction.Transaction;
import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.repositories.TransactionRepository;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {

        @Autowired
        private UserService userService;

        @Autowired
        private TransactionRepository repository;

        @Autowired
        private RestTemplate restTemplate;

        @Autowired
        private NotificationService notificationService;

        public Transaction createTransaction(TransacationDTO transaction) throws Exception{
                User sender = this.userService.findUserById(transaction.senderId());
                User receiver = this.userService.findUserById(transaction.receiverId());

                userService.validateTransaction(sender, transaction.value());

                boolean isAuthorized = this.authorizeTransaction(sender,transaction.value());
                if (!this.authorizeTransaction(sender,transaction.value())) {
                        throw new Exception("Transação não autorizada");
                }

                Transaction newTransaction = new Transaction();
                newTransaction.setAmount(transaction.value());
                newTransaction.setSender(sender);
                newTransaction.setReceiver(receiver);
                newTransaction.setTimestamp(LocalDateTime.now());

                sender.setBalance(sender.getBalance().subtract(transaction.value()));
                receiver.setBalance(receiver.getBalance().add(transaction.value()));

                repository.save(newTransaction);
                userService.saveUser(sender);
                userService.saveUser(receiver);

                this.notificationService.sendNotification(sender,"Transação realizada com sucesso");

                this.notificationService.sendNotification(receiver,"Transação realizada com sucesso");

                return newTransaction;


        }
        public boolean authorizeTransaction(User sender, BigDecimal value) {
                ResponseEntity<Map> autorizationResponse  = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);
                if (autorizationResponse.getStatusCode()== HttpStatus.OK ){
                        String message = (String) autorizationResponse.getBody().get("message");
                        return "Autorizado".equalsIgnoreCase(message);
                } else  return false;
        }

}
