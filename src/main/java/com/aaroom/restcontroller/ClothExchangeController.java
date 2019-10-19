package com.aaroom.restcontroller;

import com.aaroom.service.ClothExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClothExchangeController {

    @Autowired
    private ClothExchangeService clothExchangeService;


}
