package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.server.PathParam;

import org.hibernate.mapping.Array;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Message;
import com.example.exception.InvalidMessageLength;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    private MessageService messageService;

    public SocialMediaController(MessageService messageService){
        this.messageService = messageService;
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
        Message returnMessage = messageService.addMessage(message);
        if(returnMessage != null){
            return new ResponseEntity(returnMessage,HttpStatus.valueOf(200));
        }
        return new ResponseEntity(null,HttpStatus.valueOf(400));
    }

}
