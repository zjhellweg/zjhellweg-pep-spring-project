package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.repository.AccountRepository;
import com.example.entity.Account;
import com.example.exception.InvalidMessageLength;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    AccountRepository repository;

    @Autowired
    public AccountService(AccountRepository repository){
        this.repository = repository;
    }

    public Boolean existsById(int id){
        return repository.existsById(id);
    }

    public Account loginAccount(Account account){
        List<Account> accounts = repository.findAll();
        for (Account thisAccount : accounts) {
            if(thisAccount.getUsername().equals(account.getUsername())){
                if(thisAccount.getPassword().equals(account.getPassword())){
                    return thisAccount;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public Account createAccount(Account account){
        List<Account> accounts = repository.findAll();
        for(Account thisAccount : accounts){
            if(thisAccount.getUsername().equals(account.getUsername())){
                return null;
            }
        }
        Account returnValue = repository.save(account);
        return returnValue;
    }

}
