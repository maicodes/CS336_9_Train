package pkg.Models;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import pkg.Common;
import pkg.Schedule;

public class StationTrain {
	private int id;
	private String dep, arr, origin, dest;
	
	private static Common lib = new Common();
	
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDest() {
		return dest;
	}
	public void setDest(String dest) {
		this.dest = dest;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDep() {
		return dep;
	}
	public void setDep(String dep) {
		this.dep = dep;
	}
	public String getArr() {
		return arr;
	}
	public void setArr(String arr) {
		this.arr = arr;
	}
	
	
	public static ArrayList<StationTrain> list(String origin, String destination) throws SQLException, ParseException {
		Schedule sch = new Schedule();
		ArrayList<Train> trains = sch.get_trains(lib.transit_line_for_stations(origin, destination));
		ArrayList<StationTrain>entries = new ArrayList<StationTrain>();
		
		boolean forward_run = sch.forward_run(origin, destination);
		
		for (Train t : trains)
		{
			for (Stop s : t.get_trips())
			{
				
				if (forward_run)
				{
				if (s.get_position() < s.get_nextStop().get_position())
				{
					StationTrain st = new StationTrain();
					while (s != null)
					{
						if (s.get_station_name().equals(origin)) {
							st.setId(t.getT_id());
							st.setDep(lib.addTime(s.get_arrive_time(), 2));
							st.setOrigin(s.get_stop_id());
						}
						if (s.get_station_name().equals(destination))
						{
							st.setArr(s.get_arrive_time());
							st.setDest(s.get_stop_id());
						}
						s = s.get_nextStop();
					}
					entries.add(st);
				}
				}
				else
				{
					if (s.get_position() > s.get_nextStop().get_position())
					{
						StationTrain st = new StationTrain();
						while (s != null)
						{
							if (s.get_station_name().equals(origin)) {
								st.setId(t.getT_id());
								st.setDep(lib.addTime(s.get_arrive_time(), 2));
								st.setOrigin(s.get_stop_id());
							}
							if (s.get_station_name().equals(destination))
							{
								st.setArr(s.get_arrive_time());
								st.setDest(s.get_stop_id());
							}
							s = s.get_nextStop();
						}
						entries.add(st);
					}
				}
			
			}
		}
		
		entries = lib.sortTime(entries);
		return entries;
	}
}
