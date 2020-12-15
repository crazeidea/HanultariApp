package com.hanultari.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hanultari.command.CommonCommand;
import com.hanultari.command.getParkingData;

@Controller
public class AndroidController {
	
	CommonCommand command;
	
	@RequestMapping("/getParkingData")
	public String listnearby(HttpServletRequest req, Model model) {
		System.out.println("getParkingData()");
		command = new getParkingData();
		command.execute(model);

		return "AndroidMainActivity/getParkingData";
	}
	
}
