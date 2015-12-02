package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import jsprit.core.problem.VehicleRoutingProblem;
import jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import jsprit.core.problem.solution.route.VehicleRoute;
import jsprit.core.problem.solution.route.activity.TourActivity;
import jsprit.core.problem.solution.route.activity.TourActivity.JobActivity;
import jsprit.core.problem.vehicle.Vehicle;
import jsprit.core.util.Coordinate;

public class OROutils {
	
	public static void write(VehicleRoutingProblemSolution sol, String name, long eTime, String path){
		if(sol == null) {
			System.out.println("Solution not defined!");
			return;
		}
			 
		// Write solution on files
		BufferedWriter writer = getAppender(path);
		try {
			double seconds = eTime/1000.0;
			DecimalFormat df = new DecimalFormat("#.00");
			writer.write(name + ";" + df.format(sol.getCost()) + ";" + df.format(seconds) + ";" + sol.getRoutes().size() +"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}	
		close(writer);
	}
	
	public static void writeHTML(VehicleRoutingProblem vrp, VehicleRoutingProblemSolution sol, String path){
		if(sol == null) {
			System.out.println("Solution not defined!");
			return;
		}
		
		String pathHTML = path + ".html";
		String pathJS 	= path + ".js";
		
		// Write solution on files
		BufferedWriter writer1 = getWriter(pathHTML);
		try {
			if(sol.getRoutes().size() == 0)
				writer1.write("-1");
			else {
				writer1.write("<!DOCTYPE html>\n");
				writer1.write("<html lang=\"en\">\n");
				writer1.write("<head>\n");
				writer1.write("<title>Solution</title>\n");
				writer1.write("<meta charset=\"utf-8\">\n");
				writer1.write("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n");
				writer1.write("<link rel=\"stylesheet\" href=\"bootstrap/css/bootstrap.min.css\">\n");
				writer1.write("<script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js\"></script>\n");
				writer1.write("<script src=\"https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false\"></script>\n");
				writer1.write("<script src=\"../"+pathJS+"\"></script>\n");
				writer1.write("<script src=\"bootstrap/js/bootstrap.min.js\"></script>\n");
				writer1.write("<script>\n");
				writer1.write("function myFunction() {\n");
				writer1.write("\tvar e = document.getElementById(\"veicolo\");\n");
				writer1.write("\tvar veicolo = e.options[e.selectedIndex].value;\n");
				writer1.write("//document.getElementById('demo').innerHTML = \"initialize_\"+veicolo+\"()\";\n");
				writer1.write("eval(\"initialize_\"+veicolo+\"()\");\n");
				writer1.write("}\n");
				writer1.write("</script>\n");
				writer1.write("<style>html, body, #map-canvas {height: 100%;margin: 0px;padding: 0px}</style>\n"); 
				writer1.write("</head>\n");
				writer1.write("<body onload='initialize_0()'>\n");
				writer1.write("<nav class=\"navbar navbar-default\">\n");
				writer1.write("<div class=\"container-fluid\">\n");
				writer1.write("<div class=\"navbar-header\">\n");
				writer1.write("<button type=\"button\" class=\"navbar-toggle collapsed\" data-toggle=\"collapse\" data-target=\"#bs-example-navbar-collapse-1\">\n");
				writer1.write("<span class=\"sr-only\">Toggle navigation</span>\n");
				writer1.write("<span class=\"icon-bar\"></span>\n");
				writer1.write("<span class=\"icon-bar\"></span>\n");
				writer1.write("</button>\n");
				writer1.write("<a class=\"navbar-brand\" href=\"#\">"+ "PoliTO ORO Group" +"</a>\n");
				writer1.write("</div>\n");
				writer1.write("<!-- Collect the nav links, forms, and other content for toggling -->\n");
				writer1.write("<div class=\"collapse navbar-collapse\" id=\"bs-example-navbar-collapse-1\">\n");
				writer1.write("<ul class=\"nav navbar-nav\">\n");
				// Selector of routes
				writer1.write("<li class=\"select\" style='padding-top: 8px; padding-left: 20px;'>\n");
				writer1.write("<select class=\"form-control\" id=\"veicolo\" onchange=\"myFunction()\">\n");
				writer1.write("<option value='0'>ALL</option>\n");
				for (int routeNu=1; routeNu <= sol.getRoutes().size(); routeNu++) 
					writer1.write("<option value=\""+ routeNu+"\">Veicolo " + routeNu + " </option>\n");
				writer1.write("</select>\n");
				writer1.write("</li>\n");
				// Button for the visualization of routes
				writer1.write("</ul>\n");
				writer1.write("</div>\n");
				writer1.write("</div>\n");
				writer1.write("</nav>\n");
				writer1.write("<p id=\"demo\"></p>\n");
				writer1.write("<div id=\"map-canvas\" style=\"height:90%\"></div>\n");
				writer1.write("</body>\n");
				writer1.write("</html>\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}	
		close(writer1);
		// Details of routes
		BufferedWriter writer2 = getWriter(pathJS);
		try {
			Coordinate coorDep = ((Vehicle)vrp.getVehicles().toArray()[0]).getStartLocation().getCoordinate();
			// Draw all vehicles
			writer2.write("\tvar colors = ['00B000', 'FFD700','400080', '0097ff', 'f6546a', 'ff9338', '3c3c3c', '6AB300']; \n");
			writer2.write("\tvar txtColors = ['FFFFFF', '000000','FFFFFF', '000000', '000000', '000000', 'FFFFFF', '000000']; \n");
			writer2.write("\tvar nColors = 8; \n");
			writer2.write("function initialize_0() {\n");
			writer2.write("\tvar myOptions = {\n");
			writer2.write("\t\tcenter: new google.maps.LatLng(45.070745,7.686054),\n");
			writer2.write("\t\tzoom: 13,\n");
			writer2.write("\t\tmapTypeId: google.maps.MapTypeId.ROADMAP\n");
			writer2.write("\t};\n");
			writer2.write("\tvar map = new google.maps.Map(document.getElementById(\"map-canvas\"),   myOptions);\n");
			int idR = 0;
			for (VehicleRoute route : sol.getRoutes()) {
				writer2.write("\tvar locations = [ \n");
				int numJob=0;
				for (TourActivity act : route.getActivities()) {
	                if (act instanceof JobActivity) 
	                    writer2.write("\t\t['"+numJob+"', " + ((JobActivity) act).getLocation().getCoordinate().getX() + "," + ((JobActivity) act).getLocation().getCoordinate().getY()+",'"+((JobActivity) act).getJob().getId()+"'],\n");
	                numJob++;
				}
				writer2.write("\t];\n");
				writer2.write("\tvar locationsG = [ \n");
				writer2.write("\t\t{lat: " + coorDep.getX() + ", lng: " + coorDep.getY()+"},\n");
				for (TourActivity act : route.getActivities())
	                if (act instanceof JobActivity) 
	                    writer2.write("\t\t{lat: " + ((JobActivity) act).getLocation().getCoordinate().getX() + ", lng: " + ((JobActivity) act).getLocation().getCoordinate().getY()+"},\n");
				writer2.write("\t\t{lat: " + coorDep.getX() + ", lng: " + coorDep.getY()+"},\n");
				writer2.write("\t];\n");
				writer2.write("\tsetMarkers1(map,locations, locationsG,"+ idR +");\n");	
				idR++;
			}
			writer2.write("}\n\n");
			// Draw a single vehicle
			int routeNu = 1;
			for (VehicleRoute route : sol.getRoutes()) {
				writer2.write("function initialize_"+routeNu + "() {\n");
				writer2.write("\tvar locations = [ \n");
				int numJob=0;
				for (TourActivity act : route.getActivities()) {
	                if (act instanceof JobActivity) 
	                    writer2.write("\t\t['"+numJob+"', " + ((JobActivity) act).getLocation().getCoordinate().getX() + "," + ((JobActivity) act).getLocation().getCoordinate().getY()+",'"+((JobActivity) act).getJob().getId()+"'],\n");
	                numJob++;
				}
				writer2.write("\t];\n");
				writer2.write("\tvar locationsG = [ \n");
				writer2.write("\t\t{lat: " + coorDep.getX() + ", lng: " + coorDep.getY()+"},\n");
				for (TourActivity act : route.getActivities())
	                if (act instanceof JobActivity) 
	                    writer2.write("\t\t{lat: " + ((JobActivity) act).getLocation().getCoordinate().getX() + ", lng: " + ((JobActivity) act).getLocation().getCoordinate().getY()+"},\n");
				writer2.write("\t\t{lat: " + coorDep.getX() + ", lng: " + coorDep.getY()+"},\n");
				writer2.write("\t];\n");
				writer2.write("\tvar myOptions = {\n");
				writer2.write("\t\tcenter: new google.maps.LatLng(45.070745,7.686054),\n");
				writer2.write("\t\tzoom: 13,\n");
				writer2.write("\t\tmapTypeId: google.maps.MapTypeId.ROADMAP\n");
				writer2.write("\t};\n");
				writer2.write("\tvar map = new google.maps.Map(document.getElementById(\"map-canvas\"),   myOptions);\n");
				writer2.write("\tsetMarkers1(map,locations, locationsG, 0);\n");	
				writer2.write("}\n\n");
				routeNu++;
			}
			
			writer2.write("function setMarkers1(map, locations, locationsG, idR){\n");
			writer2.write("\tvar marker, i;\n");
			writer2.write("\tlatlngset = new google.maps.LatLng("+coorDep.getX()+", "+coorDep.getY()+");\n");
			writer2.write("\tvar marker = new google.maps.Marker({  \n");
			writer2.write("\t\tmap: map, position: latlngset, \n");
			writer2.write("\t\ticon: 'https://chart.googleapis.com/chart?chst=d_map_pin_icon&chld=home|F0F000' });\n");
			writer2.write("\tmap.setCenter(marker.getPosition())\n");
			writer2.write("\tfor (i = 0; i < locations.length; i++){\n");  
			writer2.write("\t\tvar loan = locations[i][0]\n");
			writer2.write("\t\tvar lat = locations[i][1]\n");
			writer2.write("\t\tvar long = locations[i][2]\n");
			writer2.write("\t\tvar add =  locations[i][3]\n");
			writer2.write("\t\tlatlngset = new google.maps.LatLng(lat, long);\n");
			writer2.write("\t\tvar marker = new google.maps.Marker({  \n");
			writer2.write("\t\t\tmap: map, title: add , position: latlngset, arrPos : i, \n");
			writer2.write("\t\t\tinfoW : new google.maps.InfoWindow({ content: 'Client '+ add}), \n");
			writer2.write("\t\t\tinfoW2: new google.maps.InfoWindow({ content: 'Address'}), \n");
			writer2.write("\t\t\ticon: 'https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld='+loan+'|'+colors[idR%nColors]+'|'+txtColors[idR%nColors]});\n");
			writer2.write("\t\tmarker.addListener('mouseover', function() {this.infoW.open(map, this);});\n");
			writer2.write("\t\tmarker.addListener('click', function() {this.infoW.close(); this.infoW2.open(map, this);});\n");
			writer2.write("\t\tmarker.addListener('mouseout', function() {this.infoW.close(); this.infoW2.close();});\n");
			writer2.write("\t\tmap.setCenter(marker.getPosition())\n");
			writer2.write("\t}\n");
			
			// Draw polyline
			writer2.write("\tvar path = new google.maps.Polyline({\n");
			writer2.write("\t\tpath: locationsG,\n");
		    writer2.write("\t\tgeodesic: true,\n");
		    writer2.write("\t\tstrokeColor: '#3c3c3c',\n");
		    writer2.write("\t\tstrokeOpacity: 1.0,\n");
		    writer2.write("\t\tstrokeWeight: 2\n");
		    writer2.write("\t});\n");
		    writer2.write("\tpath.setMap(map);\n");
			writer2.write("}\n");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}	
		
		close(writer2);
	}
	
	private static BufferedWriter getWriter(String file) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return writer;
	}
	
	private static BufferedWriter getAppender(String file) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file, true));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return writer;
	}
	
	private static void close(BufferedWriter writer)  {
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
