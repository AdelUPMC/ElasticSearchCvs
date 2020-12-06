package com.example.demo;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import Controller.FileUploadController;

@SpringBootApplication
@ComponentScan({"com.example.demo","controller"})
public class Project3Application {

	
	public static void main(String[] args) {
		new File(FileUploadController.UploadDirectory).mkdir();
		SpringApplication.run(Project3Application.class, args);
	}

}
