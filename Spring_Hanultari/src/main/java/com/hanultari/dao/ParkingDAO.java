package com.hanultari.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.hanultari.dto.ParkingDTO;

public class ParkingDAO {

	DataSource dataSource;

	public ParkingDAO() {
		try {
			Context context = new InitialContext();
			dataSource = (DataSource) context.lookup("java:/comp/env/ateam");

		} catch (NamingException e) {
			e.getMessage();
		}

	}

	public ArrayList<ParkingDTO> listParking() {

		ArrayList<ParkingDTO> dtos = new ArrayList<ParkingDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = dataSource.getConnection();
			String sql = "SELECT * FROM PARKING";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				ParkingDTO dto = new ParkingDTO();
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setAddr(rs.getString("addr"));
				dto.setPrev_addr(rs.getString("prev_addr"));
				dto.setLat(rs.getFloat("lat"));
				dto.setLon(rs.getFloat("lon"));
				dto.setManager(rs.getString("manager"));
				dto.setContact(rs.getString("contact"));
				dto.setOper_mon(rs.getString("oper_mon").equals("1"));
				dto.setOper_tue(rs.getString("oper_tue").equals("1"));
				dto.setOper_wed(rs.getString("oper_wed").equals("1"));
				dto.setOper_thu(rs.getString("oper_thu").equals("1"));
				dto.setOper_fri(rs.getString("oper_fri").equals("1"));
				dto.setOper_sat(rs.getString("oper_sat").equals("1"));
				dto.setOper_sun(rs.getString("oper_sun").equals("1"));
				dto.setStart_time(rs.getString("start_time"));
				dto.setEnd_time(rs.getString("end_time"));
				dto.setPaid(rs.getString("paid").equals(1));
				dto.setPayment_cash(rs.getString("payment_cash").equals("1"));
				dto.setPayment_card(rs.getString("payment_card").equals("1"));
				dto.setPayment_machine(rs.getString("payment_machine").equals("1"));
				dto.setLayout(rs.getString("layout"));
				dto.setParked(rs.getInt("parked"));
				dto.setTotal(rs.getInt("total"));
				dto.setFare(rs.getInt("fare"));
				dto.setAdded_fare(rs.getInt("added_fare"));
				dto.setDuration(rs.getInt("duration"));
				dto.setDuration_interval(rs.getInt("duration_interval"));
				dto.setSmallcar(rs.getString("smallcar").equals("1"));
				dto.setWoman(rs.getString("woman").equals("1"));
				dto.setDisabled(rs.getString("disabled").equals("1"));
				dtos.add(dto);

			}
			
			System.out.println("조회된 데이터 수 : " + dtos.size());

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {

				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {

			}
		}

		return dtos;

	}

}
