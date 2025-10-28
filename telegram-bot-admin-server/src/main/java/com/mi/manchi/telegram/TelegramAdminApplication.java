package com.mi.manchi.telegram;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.mi.manchi.telegram.mapper")
@ComponentScan(value = { "com.mi.*" })
public class TelegramAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelegramAdminApplication.class, args);
	}

}