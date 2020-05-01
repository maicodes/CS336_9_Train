package pkg;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class admin_revenue
 */
@WebServlet("/admin_revenue")
public class admin_revenue extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public admin_revenue() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		ApplicationDB ap = new ApplicationDB();
		Connection con = ap.getConnection();
		
		if(con == null) {
			System.out.print("Cannot connect to database");
			return;
		}
		
		/*
		 * GET THE BEST CUSTOMER
		 * 
		 * */
		if (req.getParameter("type").equals("bestCustomer")) {
			try {
				// This is the query statement to query the best customer who generated the highest booking fee and fares combined
				String query = "SELECT c.userName, c.firstName, c.lastName, t1.TotalRevenue " + 
						"From (SELECT userName, sum(revenue.Revenue) \"TotalRevenue\" " + 
						"FROM ( " + 
						"	SELECT rid, (totalFare + bookingFee) \"Revenue\" " + 
						"	FROM Reservations) revenue " + 
						"JOIN " + 
						"Bookings b " + 
						"ON b.rid = revenue.rid " + 
						"GROUP BY userName) t1, " + 
						"Customers c " + 
						"WHERE t1.userName = c.userName AND t1.TotalRevenue >= ALL ( " + 
						"SELECT sum(revenue.Revenue) \"TotalRevenue\" " + 
						"FROM ( " + 
						"	SELECT rid, (totalFare + bookingFee) \"Revenue\" " + 
						"	FROM Reservations) revenue " + 
						"JOIN " + 
						"Bookings b " + 
						"ON b.rid = revenue.rid " + 
						"GROUP BY userName " + 
						")";
		
				Statement sm = con.createStatement();
				ResultSet rs = sm.executeQuery(query);
				Common.printSQLResultTable(rs, res, "Report of the best customer");
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		/*
		 * GET TOP 5 ACTIVE TRANSIT LINES
		 * 
		 * */
		else if (req.getParameter("type").contentEquals("5mostActiveTransitLine")) {
			try {
				String query = 
						"SELECT * " + 
						"FROM (" +
							"SELECT transitLine \"Transit Line\", count(*) \"Total Number of Reservations\" " + 
							"FROM (SELECT t1.train_id, t.transitLine_Name transitLine " + 
								"FROM ( " + 
									"SELECT s.train_id " + 
									"FROM Reservations r, Stops s " + 
									"WHERE r.originStop_id = s.stop_id) t1, Trains t " + 
								"WHERE t.t_id = t1.train_id) train_transitLine " + 
								"GROUP BY train_transitLine.transitLine) t2 " + 
						"ORDER BY \"Total Number of Reservations\" DESC " + 
						"LIMIT 5;";
				Statement sm = con.createStatement();
				ResultSet rs = sm.executeQuery(query);
				Common.printSQLResultTable(rs, res, "Report of top 5 most active transit line");
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		/*
		 * GET REVENUE BY TRANSIT LINE
		 * 
		 * */
		else if (req.getParameter("type").equals("revByTransitLine")) {
			try {
				String query = "SELECT t.transitLine_Name, sum(t1.bookingFee) \"Total Booking Fee\", sum(t1.totalFare) \"Total Fare\", (sum(t1.bookingFee) + sum(t1.totalFare)) \"Total Revenue\" " + 
						"FROM " + 
						"	(SELECT s.train_id, r.bookingFee, r.totalFare " + 
						"	FROM Reservations r, Stops s " + 
						"	WHERE r.originStop_id = s.stop_id) t1, Trains t " + 
						"WHERE t1.train_id = t.t_id " + 
						"GROUP BY t.transitLine_Name";
				Statement sm = con.createStatement();
				ResultSet rs = sm.executeQuery(query);
				Common.printSQLResultTable(rs, res, "List of revenue per transit line:");
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		/*
		 * GET REVENUE BY DESTINATION CITY
		 * 
		 * */
		else if (req.getParameter("type").equals("revByCity")) {
			try {
				String query = "SELECT s.station_name, sum(t1.bookingFee) \"Total Booking Fee\", sum(t1.totalFare) \"Total Fare\", (sum(t1.bookingFee) + sum(t1.totalFare)) \"Total Revenue\" " + 
						"FROM " + 
						"	(SELECT s.station_id, r.bookingFee, r.totalFare " + 
						"	FROM Reservations r, Stops s " + 
						"	WHERE r.destinationStop_id = s.stop_id) t1, Stations s " + 
						"WHERE t1.station_id = s.station_id " + 
						"GROUP BY s.station_name";
				Statement sm = con.createStatement();
				ResultSet rs = sm.executeQuery(query);
				Common.printSQLResultTable(rs, res, "List of revenue per destination city:");
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		/*
		 * GET REVENUE BY CUSTOMER
		 * 
		 * */
		else if (req.getParameter("type").equals("revByCus")) {
			try {
				String query = "SELECT t1.userName \"User Name\", c.firstName \"First Name\", c.lastName \"Last Name\", sum(t1.bookingFee) \"Total Booking Fee\", sum(t1.totalFare) \"Total Fare\",(sum(t1.bookingFee) + sum(t1.totalFare)) \"Total Revenue\" " + 
						"FROM " + 
						"	(SELECT b.userName, r.bookingFee, r.totalFare" + 
						"	FROM Reservations r, Bookings b " +
						"    WHERE r.rid = b.rid) t1, Customers c " + 
						"WHERE t1.userName = c.userName " + 
						"GROUP BY t1.userName";
				Statement sm = con.createStatement();
				ResultSet rs = sm.executeQuery(query);
				Common.printSQLResultTable(rs, res, "List of revenue per customer:");
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		ap.closeConnection(con);
	}

}
