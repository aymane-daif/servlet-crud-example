package ma.ensa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import jakarta.servlet.*;
import jakarta.servlet.http.*;


public class User extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ConnDb connDb;   
    
    public User() {
        super();
     	connDb = new ConnDb();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
        
        String req = "SELECT * FROM user";
		String ul = "<table role=\"grid\"><thead><tr><th scope=\"col\">User ID</th><th scope=\"col\">Prenom</th><th scope=\"col\">Nom</th><th scope=\"col\">N°tel</th><th scope=\"col\">Action</th></tr></thead><tbody>";	

        try{
            ResultSet resultSet = connDb.getSt().executeQuery(req);

            while(resultSet.next()){
            	ul += "<tr>"
            			+ "<th scope=\"row\">" + resultSet.getString("userId") +"</th>"
            			+ "<td>" + resultSet.getString("prenom") +"</td>"
            			+ "<td>" + resultSet.getString("nom") +"</td>"
                    	+ "<td>" + resultSet.getString("tel") +"</td>"
                        + "<td><img src=\"./images/edit.svg\" alt='edit' data-userId="+resultSet.getString("userId")+" data-nom="+resultSet.getString("nom")+" data-prenom="+resultSet.getString("prenom") + " data-tel="+resultSet.getString("tel")+ " /><img src=\"./images/trash.svg\" alt='trash' data-userId="+resultSet.getString("userId")+" /></td>"

            	+ "</tr>";
            }
            ul += "</tbody></table>";
            
            out.println("<html>\n"
     				+ "<head>\n"
     				+ "<link rel=\"stylesheet\" href=\"https://unpkg.com/@picocss/pico@latest/css/pico.min.css\">\r\n"
     				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"./style.css\" />\r\n"
     				+ "<title>Search"
     				+ "</title>\r\n"
     				+ "</head>\r\n"
     				+ "<body><div class=\"container center\">\n"
     				+"<form id=\"formEdit\">\r\n"
     				+ "<div class=\"grid\">\r\n"
     				+ "<div>\r\n"
     				+ "<label for=\"prenom\">Prénom</label>\r\n"
     				+ "<input type=\"text\" name=\"prenom\" id=\"prenom\"/>\r\n"
     				+ "</div>\r\n"
     				+ "<div>\r\n"
     				+ "<label for=\"nom\">Nom</label>\r\n"
     				+ "<input type=\"text\" name=\"nom\" id=\"nom\"/>\r\n"
     				+ "</div>\r\n"
     				+ "</div>\r\n"
     				+ "<div>\r\n"
     				+ "<label for=\"tel\">N°tel</label>\r\n"
     				+ "<input type=\"text\" name=\"tel\" id=\"tel\"/>\r\n"
     				+ "</div>\r\n"
     				+ "<button type=\"submit\">Save</button>\r\n"
     				+ "</form>"
     				+ ul
     				+ "</div><script src=\"./app.js\"></script></body>\r\n"
     				+ "</html>"
     				);

        }catch (SQLException e){
            e.getMessage();
        }
		 
		
		 
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sql = "INSERT INTO user(userId, prenom, nom, tel) values(?,?,?,?);";
        String isUserExistsSql = "SELECT * from user WHERE userId=?;";

        try{
        	PreparedStatement stExists = connDb.getConn().prepareStatement(isUserExistsSql);
        	stExists.setString(1, request.getParameter("userId"));
        	stExists.executeQuery();
        	if(!stExists.getResultSet().next()) {
        		PreparedStatement preparedStatement = connDb.getConn().prepareStatement(sql);
                preparedStatement.setString(1, request.getParameter("userId"));
                preparedStatement.setString(2, request.getParameter("prenom"));
                preparedStatement.setString(3, request.getParameter("nom"));
                preparedStatement.setString(4, request.getParameter("tel"));

                preparedStatement.executeUpdate();
                
                doGet(request, response);
        	}
        	
            
        }catch (SQLException e){
            e.printStackTrace();
        }
	}


	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String sql = "DELETE FROM user WHERE userId=?;";

        try{
            PreparedStatement preparedStatement = connDb.getConn().prepareStatement(sql);
            preparedStatement.setString(1, req.getQueryString().split("=")[1]);
            preparedStatement.executeUpdate();
            doGet(req, resp);

        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }

	}


	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String sqlUpdate = "UPDATE user SET nom=?, prenom=?, tel=? WHERE userId=?; ";

		StringBuffer sb = new StringBuffer();
        String line = null;
        try {
          BufferedReader reader = req.getReader();
          while ((line = reader.readLine()) != null)
            sb.append(line);
        } catch (Exception e) { 
        	System.out.println(e.getMessage());
        }

        try {
          JSONObject jsonObject =  new JSONObject(new String(sb.toString()));
          try {
              PreparedStatement prepareStatement = connDb.getConn().prepareStatement(sqlUpdate);
              prepareStatement.setString(1, (String)jsonObject.get("nom"));
              prepareStatement.setString(2, (String)jsonObject.get("prenom"));
              prepareStatement.setString(3, (String)jsonObject.get("tel"));
              prepareStatement.setString(4, (String)jsonObject.get("userId"));
              
              prepareStatement.executeUpdate();
              doGet(req, resp);
          } catch (SQLException ex) {
              System.out.println(ex.getMessage());
          }
        } catch (JSONException e) {
          throw new IOException("Error parsing JSON request string");
        }
        
		
	}
	
	

}
