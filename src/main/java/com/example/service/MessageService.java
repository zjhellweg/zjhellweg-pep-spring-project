package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.repository.MessageRepository;
import com.example.entity.Message;
import com.example.exception.InvalidMessageLength;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    MessageRepository repository;

    @Autowired
    public MessageService(MessageRepository repository){
        this.repository = repository;
    }

    public List<Message> getAllMessages(){
        return repository.findAll();
    }

    public Message getMessageById(int id){
            Optional<Message> returnValue = repository.findById(id);
            if (returnValue.isPresent()) {
                return returnValue.get();
            } else {
                return null;
            }
    }

    public Message deleteMessageById(int id){
        Optional<Message> returnValue = repository.findById(id);
        if(returnValue.isPresent()){
            repository.deleteById(id);
            return new Message(); //returnValue.get();
        }
        return null;

    }

    public Message addMessage(Message message) throws InvalidMessageLength {
        Message returnMessage = new Message();
        if(message.getMessage_text().length() > 254 || message.getMessage_text().equals("")){
            return null;
        }
        returnMessage = repository.save(message);

        return returnMessage;
    }
}
