package pkg;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pkg.Models.Fare;


@WebServlet("/Reservation")
public class Reservation extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ApplicationDB ap = new ApplicationDB();
	Connection c = ap.getConnection();
	
	Common lib = new Common();
   
    public Reservation() {
        super();
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		//Check action of page
		String action = (String) request.getParameter("action");
		DecimalFormat df = new DecimalFormat("$00.00");
		
		if (action.equals("checkout"))
		{
			String line = (String) request.getParameter("line");
			try {
				Fare pricing = new Fare(line);
				
				double accruedDiscount = 0;
				double temp_discount;
				double overhead = pricing.getBase();
				double line_price = pricing.getStation_price();
				Integer distance = Integer.parseInt((String) request.getParameter("distance"));
				
				
				//Calculating initial price
				double initial_price = overhead + (distance * line_price);
				
				
				
				
				//Now handle age group discounts
				String agegroup = (String) request.getParameter("agegroup");
				double age_discount = 0;
				request.setAttribute("age_discount", false);
				
				if (agegroup.equals("child"))
				{
					age_discount = pricing.getChild();
					request.setAttribute("age_discount", true);
				}
				else if (agegroup.equals("senior"))
				{
					age_discount = pricing.getSenior();
					request.setAttribute("age_discount", true);
				}
				
				
				
				double price_after_age = initial_price * (1 - age_discount);
				
				temp_discount = initial_price * age_discount;
				request.setAttribute("discount_for_age", df.format(temp_discount));
				
				
				
				
				
				double price_after_groups_discount = price_after_age;
				request.setAttribute("disabled_discount", false);
				
				
				
				
				
				//Handling disabled
				
				if (request.getParameter("disabled") != null)
				{
					double disabled_discount = pricing.getDisabled();
					double price_after_disabled = price_after_age * (1 - disabled_discount);
					
					temp_discount = price_after_age * disabled_discount;
					
					request.setAttribute("discount_for_disabled", df.format(temp_discount));
					request.setAttribute("price_after_disabled", df.format(price_after_disabled));
					request.setAttribute("disabled_discount", true);
					
					price_after_groups_discount = price_after_disabled;
				}
				
				
				
				
				
				
				//Now handle class premiums
				String ticket_class = (String) request.getParameter("class");
				request.setAttribute("class", ticket_class);
				double class_premium = 0;
				
				if (ticket_class.equals("first"))
				{
					class_premium = price_after_groups_discount * 1.2;
					class_premium = Math.max(class_premium, 8);
				}
				else if (ticket_class.equals("business"))
				{
					class_premium = price_after_groups_discount * 0.8;
					class_premium = Math.max(class_premium, 5);
				}
				
				double price_after_class = price_after_groups_discount + class_premium;
				

				request.setAttribute("class_premium", df.format(class_premium));
				request.setAttribute("price_after_class", df.format(price_after_class));
				
				
				
				
				//Lettuce attempt to handle round trips/oneways/ monthly... etc.
				String type = (String) request.getParameter("type");
				String multiplier = "";
				
				int multiplier_for_discount = 1;
				
				int m = 30;
				double extra_ticket_price = 0;
				double price_after_bulk = price_after_class;
				double price_before_tax = price_after_bulk;
				double rate = 0;
				double bulk_discount;
				
				if (!type.equals("oneway"))
					{
					if (type.equals("roundtrip")) {
						multiplier = "(x2)";
						multiplier_for_discount = 2;
						extra_ticket_price = price_after_class;
						price_after_bulk = extra_ticket_price + price_after_class;
					}
					else if (type.equals("weekly")) {
						multiplier = "(x7)(x2)";
						multiplier_for_discount = 14;
						extra_ticket_price = price_after_class * 13;
						price_after_bulk = price_after_class + extra_ticket_price;
						
						
						rate = pricing.getWeek();
					}
					else
					{
						//TO DO MAKE MORE ACCURATE TO MONTH
						multiplier = "(x" + m + ")(x2)";
						
						multiplier_for_discount = m * 2;
						extra_ticket_price = price_after_class * ((2 * m) - 1);
						price_after_bulk = price_after_class + extra_ticket_price;
						
						
						rate = pricing.getMonth();
					}
				}
				
				bulk_discount = rate * price_after_bulk;
				price_before_tax = price_after_bulk - bulk_discount;
				
				
				request.setAttribute("extra_ticket_price", df.format(extra_ticket_price));
				request.setAttribute("multiplier", multiplier);
				request.setAttribute("price_after_bulk", df.format(price_after_bulk));
				request.setAttribute("bulk_discount", df.format(bulk_discount));
				request.setAttribute("price_before_tax", df.format(price_before_tax));
				
				
				
				
				
				
				
				//Figure out formula for total discount
				double originalTotal = (initial_price + class_premium) * multiplier_for_discount;
				accruedDiscount = originalTotal - price_after_bulk;
				
				request.setAttribute("total_discount", df.format(accruedDiscount));
				
				//Handle booking fee
				double booking_fee = 3;
				double price_after_booking_fee = booking_fee + price_before_tax;
				
				request.setAttribute("booking_fee", df.format(booking_fee));
				request.setAttribute("price_after_booking_fee", df.format(price_after_booking_fee));
				
				
				
				
				//Handle tax
				double tax_rate = 0.07;
				double applied_tax = tax_rate * price_after_booking_fee;
				double total_cost = applied_tax + price_after_booking_fee;
				
				request.setAttribute("applied_tax",df.format(applied_tax));
				request.setAttribute("total_cost", df.format(total_cost));
				
				
				
				
				//Handle time elapsed
				String timedata = (String) request.getParameter("time");
				String t1, t2;
				
				t1 = timedata.substring(11,16);
				t2 = timedata.substring(5,10);
				if (timedata.substring(0,3).equals("Any"))
				{
					t1 = timedata.substring(10, 15);
					t2 = timedata.substring(5, 9);
				}
				
				
				
				int total_time = (int) lib.difference(t1, t2);
				
				
				
				
				
				
				request.setAttribute("total_time", total_time);
				request.setAttribute("initial_price", df.format(initial_price));
				request.setAttribute("price_after_age", df.format(price_after_age));
				
				request.getRequestDispatcher("checkout.jsp").forward(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		else if (action.equals("finish")) {
			String SQL_Reservations = 
			"INSERT INTO Reservations (bookingFee, class, travelTime, totalFare, total_discount, originStop_id, "
			+ "destinationStop_id, is_round_trip, isWeekly, isMonthly)"
			+ " VALUES (?,?,?,?,?,?,?,?,?,?)";
			
			String SQL_Bookings = 
			"INSERT INTO Bookings (date, userName, rid) "
			+ "VALUES (?, ?, ?)";
			
			
			
			try {
				String userName = Auth.getLoginUser().getUserName();
				
				// String rid = get_next_rid();
				double bookingFee = Double.parseDouble( ((String)request.getParameter("booking_fee")).substring(1));
				String ticket_class = (String) request.getParameter("ticket_class");
				String fdate = (String) request.getParameter("fdate");
				fdate += " ";
				int travelTime = Integer.parseInt(request.getParameter("total_time"));
				double totalFare = Double.parseDouble(((String) request.getParameter("total_cost")).substring(1));
				double total_discount = Double.parseDouble(((String) request.getParameter("total_discount")).substring(1));
				
				
				
				String ftid = (String) request.getParameter("ftid");
				String fdep = (String) request.getParameter("fdep");
				String farr = (String) request.getParameter("farr");
				
				String insertDate = lib.getSqlDate(fdate, lib.addTime(fdep, -2));
				String originStop_id = lib.getStopId(lib.addTime(fdep, -2), ftid);
				String destinationStop_id = lib.getStopId(farr, ftid);
				
				int round_trip = 0;
				int monthly = 0;
				int weekly = 0;
				
				String type = (String) request.getParameter("type");
				
				if (type.equals("monthly"))
				{
					round_trip = 1;
					monthly = 1;
				}
				else if (type.equals("weekly"))
				{
					round_trip = 1;
					weekly = 1;
				}
				else if (type.equals("roundtrip"))
				{
					round_trip = 1;
				}
				
				
				PreparedStatement ps = c.prepareStatement(SQL_Reservations);
				PreparedStatement ps2 = c.prepareStatement(SQL_Bookings);
				int insertSuccess;
				
				int arrr = 1;
				if (!type.equals("oneway"))
					arrr = 2;
				
				
				for (int i = 0; i < arrr; i++)
				{
					
					if (i == 1)
					{
						ps = c.prepareStatement(SQL_Reservations);
						ps2 = c.prepareStatement(SQL_Bookings);
						
						// rid = get_next_rid();
						if (request.getParameter("rdate") != null)
						{
							
							String rdep = (String) request.getParameter("rdep");
							String rtid = (String) request.getParameter("rtid");
							String rarr = (String) request.getParameter("rarr");
							
							fdate = (String) request.getParameter("rdate");
							fdate += " ";
							insertDate = lib.getSqlDate(fdate, lib.addTime(rdep, -2));
							
							
							totalFare = 0;
							originStop_id = lib.getStopId(lib.addTime(rdep, -2), rtid);
							destinationStop_id = lib.getStopId(rarr, rtid);
							
							round_trip = 0;
						}
						
					}
					// ps.setString(1,rid);
					ps.setDouble(1, bookingFee);
					ps.setString(2, ticket_class);
					ps.setInt(3, travelTime);
					ps.setDouble(4, totalFare);
					ps.setDouble(5, total_discount);
					ps.setString(6, originStop_id);
					ps.setString(7, destinationStop_id);
					ps.setInt(8, round_trip);
					ps.setInt(9, weekly);
					ps.setInt(10, monthly);
					
					insertSuccess = ps.executeUpdate();
					if (insertSuccess == 0)
					{
						System.out.println("Nothing was added - you got errors.");
					}
					else
					{
						System.out.println("Something was added.");
					}
					
					int rid = get_next_rid();
					ps2.setString(1, insertDate);
					ps2.setString(2, userName);
					ps2.setInt(3, rid);
					ps2.executeUpdate();
					
				}
				
				
				
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Time to enter debug mode....");
			}
			
		}
	}
	
	
	public int get_next_rid() throws SQLException {
		String SQL = "SELECT rid FROM Reservations ORDER BY rid DESC LIMIT 1";
		PreparedStatement ps = c.prepareStatement(SQL);
		
		ResultSet rs = ps.executeQuery();
		int suffix = 1;
		if (rs.next())
		{
//			String latestEntry = rs.getString(1);
//			int suffix = Integer.parseInt(latestEntry.substring(1, latestEntry.length()));
//			suffix++;
//			return "r" + suffix;
			suffix = rs.getInt(1);
		}
		return suffix;
	}
}
