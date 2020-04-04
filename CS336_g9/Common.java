package pkg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Common {

	ApplicationDB ap = new ApplicationDB();
	Connection c = ap.getConnection();
	
	public Common() {
		super();
	}
	
	
	public String stripTime(String time) //When you get a datetime from sql, this returns HH:mm
	{
		return time.substring(11,16);
	}
	
	public String getSqlDate(String d, String t) //Add yyyy-MM-dd + HH:mm to get an SQL insertable date
	{
		String f = d + t + ":00";
		return f;
	}
	public String getToday() //Returns yyyy-MM-dd
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date today = Calendar.getInstance().getTime();
		String rn = df.format(today);
		rn = rn.substring(0,11);
		
		return rn;
	}
	public String addTime(String base, int minutes) //Adds HH:mm + integer amount of minutes
	{
		SimpleDateFormat d = new SimpleDateFormat("HH:mm");
		d.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		Calendar cal = Calendar.getInstance();
		Date base_time = new Date();
		try {
			base_time = d.parse(base);
			cal.setTime(base_time);
			cal.add(Calendar.MINUTE, minutes);
			
			String newTime = d.format(cal.getTime());
			return newTime;
		}
		catch(ParseException e)
		{
			e.printStackTrace();
			System.out.println("You definitely messed up the string format");
			return base;
		}
	}
	public String getInitials(String s) //Gets initials of a string from uppercase letters
	{
		String r = "";
		for (int i = 0; i < s.length(); i++)
		{
			char ch = s.charAt(i);
			if (Character.isUpperCase(ch))
			{
				r+=ch;
			}
		}
		return r;
	}
	
	public ArrayList<String> get_transit_line_names() throws SQLException //Get all the transit lines
	{
		PreparedStatement p = c.prepareStatement("SELECT name FROM Transit_Lines ORDER BY name ASC");
		ResultSet fetched_names = p.executeQuery();
		ArrayList<String> names = new ArrayList<String>();
		while (fetched_names.next())
		{
			names.add(fetched_names.getString(1));
		}
		return names;
	}
	public String getOrigin(String line) throws SQLException //Get origin station for transit line
	{
		PreparedStatement p = c.prepareStatement("SELECT s.station_name "
				+ "FROM Stations s, Transit_Lines t "
				+ "WHERE t.name = ?"
				+ "AND s.station_id = t.origin_id");
		p.setString(1, line);
		ResultSet rs = p.executeQuery();
		rs.next();
		return rs.getString(1);
	}
	public String getDestination(String line) throws SQLException //get destination station
	{
		PreparedStatement p = c.prepareStatement("SELECT s.station_name "
				+ "FROM Stations s, Transit_Lines t "
				+ "WHERE t.name = ?"
				+ "AND s.station_id = t.destination_id");
		p.setString(1, line);
		ResultSet rs = p.executeQuery();
		rs.next();
		return rs.getString(1);
	}
}
