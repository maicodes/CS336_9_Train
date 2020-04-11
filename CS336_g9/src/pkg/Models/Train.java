package pkg.Models;

import java.util.ArrayList;

public class Train {
	private int t_id, cars, seats;
	private String transit_line;
	private ArrayList<Stop> trips = new ArrayList<Stop>();
	
	public int getT_id() {
		return t_id;
	}
	public void setT_id(int t_id) {
		this.t_id = t_id;
	}
	public int getCars() {
		return cars;
	}
	public void setCars(int cars) {
		this.cars = cars;
	}
	public int getSeats() {
		return seats;
	}
	public void setSeats(int seats) {
		this.seats = seats;
	}
	public String getTransit_line() {
		return transit_line;
	}
	public void setTransit_line(String transit_line) {
		this.transit_line = transit_line;
	}
	
	
	public void add_trip(Stop s)
	{
		trips.add(s);
	}
	public ArrayList<Stop> get_trips()
	{
		return trips;
	}
	
	public Stop findTime(String time)
	{
		for (Stop s : trips)
		{
			if (s.get_arrive_time().equals(time))
			{
				return s;
			}
		}
		return null;
	}
	
}
