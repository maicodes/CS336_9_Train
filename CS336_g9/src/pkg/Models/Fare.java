package pkg.Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import pkg.ApplicationDB;

public class Fare {
	private double base, station_price, week, month, child, senior, disabled;
	private String SQL_fare, SQL_week, SQL_month, line;
	
	public Fare(String line) throws SQLException
	{
		SQL_fare = "SELECT * FROM Fares WHERE transit_line = ?";
		SQL_week = "SELECT weekly_discount FROM Fares JOIN Weekly_Fares ON Fares.fid = Weekly_Fares.fid WHERE transit_line = ?";
		SQL_month = "SELECT monthly_discount FROM Fares JOIN Monthly_Fares ON Fares.fid = Monthly_Fares.fid WHERE transit_line = ?";
		
		ApplicationDB ap = new ApplicationDB();
		Connection c = ap.getConnection();
		PreparedStatement ps = c.prepareStatement(SQL_fare);
		ps.setString(1, line);
		ResultSet rs = ps.executeQuery();
		
		
		if (rs.next())
		{
			station_price = rs.getDouble("price");
			child = rs.getDouble("children_discount");
			senior = rs.getDouble("senior_discount");
			disabled = rs.getDouble("disabled_discount");
		}
		else
		{
			station_price = 0;
			child = 0;
			senior = 0;
			disabled = 0;
		}
		
		ps = c.prepareStatement(SQL_week);
		ps.setString(1, line);
		rs = ps.executeQuery();
		
		if (rs.next())
		{
			week = rs.getDouble(1);
		}
		else
			week = 0;
		
		ps = c.prepareStatement(SQL_month);
		ps.setString(1, line);
		rs = ps.executeQuery();
		
		if (rs.next())
			month = rs.getDouble(1);
		else
			month = 0;
		
		this.line = line;
		base = 3.5;
	}

	public double getBase() {
		return base;
	}

	public void setBase(double base) {
		this.base = base;
	}

	public double getStation_price() {
		return station_price;
	}

	public void setStation_price(double station_price) {
		this.station_price = station_price;
	}

	public double getWeek() {
		return week;
	}

	public void setWeek(double week) {
		this.week = week;
	}

	public double getMonth() {
		return month;
	}

	public void setMonth(double month) {
		this.month = month;
	}

	public double getChild() {
		return child;
	}

	public void setChild(double child) {
		this.child = child;
	}

	public double getSenior() {
		return senior;
	}

	public void setSenior(double senior) {
		this.senior = senior;
	}

	public double getDisabled() {
		return disabled;
	}

	public void setDisabled(double disabled) {
		this.disabled = disabled;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}
	
	
}
