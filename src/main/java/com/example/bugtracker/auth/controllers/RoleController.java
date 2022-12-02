package com.example.bugtracker.auth.controllers;

import com.example.acm_backend.auth.entities.Role;
import com.example.acm_backend.auth.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController()
@RequestMapping(path = "/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping("")
    public ResponseEntity<Object> createRole(@RequestBody Role role){
        return new ResponseEntity<>(roleService.createRole(role), HttpStatus.ACCEPTED);
    }
    @GetMapping("")
    public ResponseEntity<Object> getAllRoles(){
        return new ResponseEntity<>(roleService.getAllRoles(), HttpStatus.ACCEPTED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Object> getRole(@PathVariable("id") Long id){
        Role role=roleService.getRole(id);
        if(role==null) return new ResponseEntity<>(Collections.emptyList(), HttpStatus.ACCEPTED);
        return new ResponseEntity<>(role, HttpStatus.ACCEPTED);
    }
}
