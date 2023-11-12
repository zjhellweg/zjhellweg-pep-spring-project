package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.server.PathParam;

import org.hibernate.mapping.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.InvalidMessageLength;
import com.example.service.MessageService;
import com.example.service.AccountService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    private MessageService messageService;
    private AccountService accountService;

    @Autowired
    public SocialMediaController(MessageService messageService, AccountService accountService){
        this.messageService = messageService;
        this.accountService = accountService;
    }

    @GetMapping("/messages")
    public List<Message> getAllMessages(){
        List<Message> returnValue = messageService.getAllMessages();
        return returnValue;
    }
    
    @GetMapping("/messages/{id}")
    public Message getMessageById(@PathVariable int id) throws Exception{
        Message returnValue = messageService.getMessageById(id);
        return returnValue;
    }
    
    @DeleteMapping("messages/{id}")
    public Integer deleteMessageById(@PathVariable int id) throws Exception{
        int before = messageService.getAllMessages().size();
        messageService.deleteMessageById(id);
        int after = messageService.getAllMessages().size();
        if(before - after != 0){
            return before-after;
        }else {
            return null;
        }
    }
    
    @PostMapping("messages")
    public ResponseEntity<Message> insertMessage(@RequestBody Message message) throws InvalidMessageLength{
        if(!accountService.existsById(message.getPosted_by())){
            return new ResponseEntity(null,HttpStatus.valueOf(400));
        }
        Message returnMessage = messageService.addMessage(message);
        if(returnMessage != null){
            return new ResponseEntity(returnMessage,HttpStatus.valueOf(200));
        }
        return new ResponseEntity(null,HttpStatus.valueOf(400));
    }

    @GetMapping("accounts/{account_id}/messages")
    public List<Message> getMessagesByUser(@PathVariable int account_id){
        List<Message> value = messageService.getAllMessages();
        List<Message> returnValue = new ArrayList<Message>();
        for (Message message : value) {
            if(message.getPosted_by() == account_id){
                returnValue.add(message);
            }
        }
        return returnValue;
    }

    @PatchMapping("messages/{id}")
    public ResponseEntity<Integer> updateMessage(@PathVariable int id,@RequestBody Message message){
        Message returnValue = messageService.updateMessage(id,message.getMessage_text());
        if(returnValue == null){
            return new ResponseEntity<Integer>(HttpStatus.valueOf(400));
        }
        return new ResponseEntity<Integer>(1,HttpStatus.valueOf(200));
    } 

    @PostMapping("login")
    public ResponseEntity<Account> userLogin(@RequestBody Account account){
        Account returnValue = accountService.loginAccount(account);
        if(returnValue != null){
            return new ResponseEntity<Account>(returnValue,HttpStatus.valueOf(200));
        }
        return new ResponseEntity<Account>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("register")
    public ResponseEntity<Account> registerUser(@RequestBody Account account){
        if(account.getUsername().length() == 0 || account.getPassword().length() <= 3){
            return new ResponseEntity(HttpStatus.valueOf(400));
        }
        
        Account returnValue = accountService.createAccount(account);

        if(returnValue != null){
            return new ResponseEntity<Account>(returnValue, HttpStatus.OK);
        } else {
            return new ResponseEntity<Account>(HttpStatus.CONFLICT);
        }
    }

}
