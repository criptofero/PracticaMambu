package com.sofka.practicaMambu.application.config;

import com.sofka.practicaMambu.domain.service.ClientService;
import com.sofka.practicaMambu.domain.service.DepositProductService;
import com.sofka.practicaMambu.infraestructure.ClientRepository;
import com.sofka.practicaMambu.infraestructure.DepositProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientServiceConfig {

    @Bean
    public ClientService clientService(){ return new ClientRepository();
    }

    @Bean
    public DepositProductService productService(){ return new DepositProductRepository();
    }
}
