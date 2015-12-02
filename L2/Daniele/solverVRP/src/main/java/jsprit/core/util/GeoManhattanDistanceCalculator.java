package jsprit.core.util;


public class GeoManhattanDistanceCalculator {

	public static double calculateDistance(Coordinate coord1, Coordinate coord2) {
		
		double R	= 6371 * 1000; // m
		double phi1	= deg2rad(coord1.getX());
		double phi2	= deg2rad(coord2.getX());
		double dPhi	= deg2rad(coord2.getX()-coord1.getX());
		double dLam	= deg2rad(coord2.getY()-coord1.getY());

		double a = Math.sin(dPhi/2) * Math.sin(dPhi/2) + Math.cos(phi1) * Math.cos(phi2) * Math.sin(dLam/2) * Math.sin(dLam/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		double dist = R * c;

		return dist;
	}
	
	/** This function converts decimal degrees to radians */
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}
}
