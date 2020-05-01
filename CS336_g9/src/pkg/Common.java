package pkg;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletResponse;

import pkg.Models.StationTrain;

public class Common {

	ApplicationDB ap = new ApplicationDB();
	Connection c = ap.getConnection();
	
	public Common() {
		super();
	}
	//t1 - t2, so t1 should be later!
	public long difference(String t2, String t1)
	{
		
		LocalTime time1 = LocalTime.parse(t1);
		LocalTime time2 = LocalTime.parse(t2);
		Duration timeElapsed = Duration.between(time1, time2);
		
		return timeElapsed.toMinutes();
	}
	public String dbDate() {
		return "2020-04-15 ";
	}
	
	public String getStopId(String time, String train) throws SQLException{
		String sql_time = getSqlDate(dbDate(), time);
		String SQL = "SELECT stop_id " + 
				"FROM Stops " + 
				"WHERE arrive_time = ? " + 
				"AND train_id = ?";
		PreparedStatement ps = c.prepareStatement(SQL);
		ps.setString(1, sql_time);
		ps.setString(2, train);
		
		ResultSet rs = ps.executeQuery();
		
		rs.next();
		return rs.getString(1);
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
	public ArrayList<String> getWeek() throws ParseException {
		String today = getToday();
		ArrayList<String> week = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		String workingDate = today;
		week.add(workingDate);
		
		for (int i = 0; i < 6; i++)
		{
			
			c.setTime(sdf.parse(workingDate));
			c.add(Calendar.DATE, 1);  // number of days to add
			workingDate = sdf.format(c.getTime());  // dt is now the new date
			
			week.add(workingDate);
			
		}
		return week;
	}
	public String getTime()
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date today = Calendar.getInstance().getTime();
		String rn = df.format(today);
		rn = rn.substring(11,16);
		
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
	public ArrayList<StationTrain> sortTime(ArrayList<StationTrain> list) throws ParseException //sorts by time
	{
		for (int i = 0; i < list.size(); i++)
		{
			for (int j = 0; j < list.size() - i -1; j++)
			{
				if (greaterThan(list.get(j).getDep(), list.get(j+1).getDep()))
				{
					Collections.swap(list, j, j+1);
				}
			}
		}
		return list;
	}
	public boolean greaterThan(String t1, String t2) throws ParseException //Compares 2 times
	{
		SimpleDateFormat d = new SimpleDateFormat("HH:mm");
		d.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		
		Date d1 = new Date();
		Date d2 = new Date();
		
		d1 = d.parse(t1);
		d2 = d.parse(t2);
		
		return d1.after(d2);
	}
	
	public boolean greaterThanDate(String d1, String d2) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse(d1);
        Date date2 = sdf.parse(d2);
        
        return date1.after(date2);
	}
	
	public String encode(String value)
	{
		try {
			return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
		}
		catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
		}
	}
	public String decode(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
	
	public static void main(String[] args) throws ParseException, SQLException
	{
		Common lib = new Common();
		
		System.out.println(lib.getStopId("05:00", "1001"));
	}
	
	public String transit_line_for_stations(String origin, String destination) throws SQLException
	{
		String SQL = "SELECT DISTINCT station_name, Trains.transitLine_Name " + 
				"FROM Stops " + 
				"JOIN Stations " + 
				"ON Stops.station_id = Stations.station_id " + 
				"JOIN Trains " + 
				"ON Trains.t_id = Stops.train_id " + 
				"WHERE station_name = ? OR station_name = ?;";
		PreparedStatement ps = c.prepareStatement(SQL);
		ps.setString(1, origin);
		ps.setString(2, destination);
		
		ResultSet rs = ps.executeQuery();
		ArrayList<String> potential_lines = new ArrayList<String>();
		String line;
		
		while (rs.next())
		{
			if (potential_lines.contains(rs.getString("transitLine_Name")))
			{
				line = rs.getString("transitLine_Name");
				return line;
			}
			else
			{
				potential_lines.add(rs.getString("transitLine_Name"));
			}
		}
		
		String fallback = "SELECT DISTINCT Trains.transitLine_Name " + 
				"FROM Trains " + 
				"JOIN Stops " + 
				"ON Stops.train_id = Trains.t_id " + 
				"JOIN Stations " + 
				"ON Stations.station_id = Stops.station_id " + 
				"WHERE station_name = ?";
		
		ps = c.prepareStatement(fallback);
		ps.setString(1, origin);
		rs = ps.executeQuery();
		rs.next();
		return rs.getString(1);
	}
	
	/***
	 * Prints out a SQL result.
	 * @param rs : The SQL result, usually the return value from the excecuteQuery() or executeUpdate() functions 
	 * @param res : httpServlet response
	 * @param headline : The headline above the result table, saying what the SQL result is about. For example: "Return the best customer"
	 * @throws SQLException
	 */
	public static void printSQLResultTable(ResultSet rs, HttpServletResponse res, String headline) throws SQLException {
		res.setContentType("text/html");
		try {
			PrintWriter out = res.getWriter();
			
			out.print("<html>" + 
					"<head>" + 
					"<link rel=\"stylesheet\" type=\"text/css\" href=\"css/main.css\">\r\n" + 
					"<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">" + 
					"</head>" + 
					"<body>");
			
			out.print("<h3>"+ headline +"</h3><br>");
			out.print("<table class=\"table table-striped\">");
			
			ResultSetMetaData rsmd = rs.getMetaData();
			
			int numCols = rsmd.getColumnCount();
			
			out.print(" <thead class=\"thead-dark\">" + 
						"<tr>");
			for(int i = 1; i <= numCols; i++) {
				out.print("<th scope=\"col\">" + rsmd.getColumnName(i) + "</th>");
			}
			
			out.print("</tr> </thead>");
			
			out.print("<tbody>");
			
			while(rs.next()) {
				out.print("<tr>");
				for(int i = 1; i <= numCols; i++) {	
					out.print("<td>");
					out.print(rs.getString(i));
					out.print("</td>");
				}
				out.print("</tr>");
			}
			out.print("</tbody>");
			
			out.print("</table>");
			
			out.print("</body></html>");
			
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/***
	 * Creates an update statement. For example: UPDATE TABLE ? SET ? = ? WHERE c = d.
	 * @param table : the table name. 
	 * @param columns : ArrayList of the column names that will be updated, for example userName. 
	 * @param values : Array List of new values. 
	 * @param condition : note that the condition must be a full statement. For example: WHERE userName = "Joe" 
	 */
	public static String createEditStatement (String table, ArrayList<String> columns, ArrayList<String> values, String condition) {
		String updateQuery = "UPDATE " + table + " SET ";
		StringBuilder editStatement = new StringBuilder(updateQuery);
		int numCols = columns.size();
		
		for(int i = 0; i < numCols; i++) {
			String s = columns.get(i) + " = " + "\"" + values.get(i) + "\"";
			if( i != numCols - 1) 	
				s += ", ";
			editStatement.append(s);
		}
		
		editStatement.append(condition);
		return editStatement.toString();
	}
	
	/***
	 * Checks whether a string is null or empty
	 * @param s : string
	 * @return
	 */
	public static boolean StringIsNotEmpty (String s) {
		if (s == null || s.isEmpty())
			return false;
		return true;
	}
	
	public static String getCusRepSSN (String userName, Connection con) {
		String query = "SELECT ssn FROM Employees WHERE userName = " + "\"" + userName + "\"";
		Statement s;
		try {
			s = con.createStatement();
			ResultSet rs = s.executeQuery(query);
			
			if(rs.next()) 
				return rs.getString(1);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public ArrayList<Integer> getTrainNumbers () throws SQLException { // get all train numbers 
			PreparedStatement p = c.prepareStatement("SELECT DISTINCT t_id FROM Trains;");
			ResultSet fetched_trains = p.executeQuery();
			ArrayList<Integer> trains = new ArrayList<Integer>();
			
			while (fetched_trains.next())
			{
				trains.add(fetched_trains.getInt(1));
			}
			
		return trains;
	}
}