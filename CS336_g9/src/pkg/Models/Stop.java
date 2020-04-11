package pkg.Models;

public class Stop {
	private String station_name, arrive_time, stop_id;
	private Stop nextStop;
	private int position;
	private boolean lastOfLine = false;
	public Stop()
	{
		nextStop = null;
	}
	
	public void set_stop_id(String s)
	{
		stop_id = s;
	}
	public void set_station_name(String s)
	{
		station_name = s;
	}
	public void set_arrive_time(String s)
	{
		arrive_time = s;
	}
	public void set_position(int p)
	{
		position = p;
	}
	public void set_nextStop(Stop s)
	{
		nextStop = s;
	}
	public boolean lastOfLine()
	{
		return lastOfLine;
	}
	public void lastOfLine(boolean b)
	{
		lastOfLine = b;
	}
	
	public String get_stop_id() {return stop_id;}
	public String get_station_name() {return station_name;}
	public int get_position() {return position;}
	public Stop get_nextStop() {return nextStop;}
	public String get_arrive_time() {return arrive_time;}
}
