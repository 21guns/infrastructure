package com.guns21.support.service;

import com.guns21.data.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;

public  abstract class BaseCommandService<Entity,Id> implements CommandService<Entity,Id> {

    @Autowired
    private ResultFactory factory;
}
