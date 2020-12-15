package com.hanultari.command;

import java.util.ArrayList;

import org.springframework.ui.Model;

import com.hanultari.dao.ParkingDAO;
import com.hanultari.dto.ParkingDTO;

public class getParkingData implements CommonCommand {

	@Override
	public void execute(Model model) {
		ParkingDAO dao = new ParkingDAO();
		ArrayList<ParkingDTO> dtos = dao.listParking();
		
		model.addAttribute("data", dtos);
		
	}

}
