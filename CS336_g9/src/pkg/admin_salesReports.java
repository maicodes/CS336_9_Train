package pkg;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class admin_saleReports
 */
@WebServlet("/admin_salesReports")
public class admin_salesReports extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public admin_salesReports() {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doGet(request, response);
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		int month = Integer.parseInt(request.getParameter("admin-sales-month"));
		
		if (month < 1 || month > 12) {
			out.print("<p class=\"error\">Month must be between 1 and 12!</p>");
			return;
		}
		
		
		try {
			ApplicationDB ap = new ApplicationDB();
			
			Connection con = ap.getConnection();
			
			if(con == null) {
				System.out.print("Cannot connect to database");
				return;
			}
			
			/* Example:
			select * from test
			where 
			year(date) = 2015
			and month(date) = 10
			and day(date)= 28 ;
			*/
			
			String query = "SELECT * FROM Reservations "
					+ "WHERE month(date) = ?;";
			
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, String.valueOf(month));
			
			ResultSet rs = ps.executeQuery();
			
			out.print("<html>" + 
					"<head>" + 
					"<link rel=\"stylesheet\" type=\"text/css\" href=\"css/main.css\">\r\n" + 
					"<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">" + 
					"</head>" + 
					"<body>");
			
			out.print("<h3>Sales reports in month: " + month + "</h3><br>");
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
			
			double totalFare = 0.0;
			int totalBookingFee = 0;
			int totalDiscount = 0;
			
			while(rs.next()) {
				out.print("<tr>");
				for(int i = 1; i <= numCols; i++) {
					
					if(i == 2)
						totalBookingFee += Integer.parseInt(rs.getString(i));
					else if(i == 6)
						totalFare += Double.parseDouble(rs.getString(i));
					else if(i == 7)
						totalDiscount += Integer.parseInt(rs.getString(i));
					
					out.print("<td>");
					out.print(rs.getString(i));
					out.print("</td>");
				}
				out.print("</tr>");
			}
			out.print("</tbody>");
			
			out.print("</table>");
			
			out.print("<br><h6>Total Fares: $" + totalFare + "</h6><h6>Total Discount: $" + totalDiscount + "</h6><h6>Total Booking Fee: $" + totalBookingFee + "</h6>");
			
			out.print("</body></html>");
			
			ps.executeUpdate();
			ap.closeConnection(con);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}	
		finally {
			out.close();
		}
	}

}
