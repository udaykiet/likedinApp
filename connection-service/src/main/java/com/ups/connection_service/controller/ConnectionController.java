package com.ups.connection_service.controller;

import com.netflix.discovery.converters.Auto;
import com.ups.connection_service.entity.Person;
import com.ups.connection_service.service.ConnectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@Slf4j
public class ConnectionController {


    @Autowired
    private ConnectionService connectionService;

    @GetMapping("/first-degree")
    public ResponseEntity<List<Person>> getAllFirstDegreeConnections(){
        log.info("called");
        return ResponseEntity.ok(connectionService.getMyFirstDegreeConnections());
    }

    @PostMapping("/request/{userId}")
    public ResponseEntity<Boolean> sendConnectionRequest(@PathVariable Long userId){
        return ResponseEntity.ok(connectionService.sendConnectionRequest(userId));
    }



    @PostMapping("/accept/{userId}")
    public ResponseEntity<Boolean> acceptConnectionRequest(@PathVariable Long userId){
        return ResponseEntity.ok(connectionService.acceptConnectionRequest(userId));
    }

    @PostMapping("/reject/{userId}")
    public ResponseEntity<Boolean> rejectConnectionRequest(@PathVariable Long userId){
        return ResponseEntity.ok(connectionService.rejectConnectionService(userId));
    }



    //for checking purpose
    @GetMapping("/{id}")
    public ResponseEntity<Person> getNode(@PathVariable Long id){
        Person person =  connectionService.getNode(id);
        return ResponseEntity.ok(person);
    }
}
