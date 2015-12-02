/*******************************************************************************
 * Copyright (C) 2015  ORO e ISMB
 * Questo e' il main del programma di ottimizzazione VRPTW
 * Il programma prende in input un file csv con i clienti ed un csv di configurazione (deposito e veicoli) e restituisce in output il file delle route ed un file sintetico di dati dei viaggi.
 * L'algoritmo ultizzato e' un Large Neighborhood 
 ******************************************************************************/

package main;

import java.util.Collection;

import jsprit.analysis.toolbox.GraphStreamViewer;
import jsprit.analysis.toolbox.GraphStreamViewer.Label;
import jsprit.core.algorithm.VehicleRoutingAlgorithm;
import jsprit.core.algorithm.io.VehicleRoutingAlgorithms;
import jsprit.core.algorithm.selector.SelectBest;
import jsprit.core.algorithm.termination.TimeTermination;
import jsprit.core.problem.VehicleRoutingProblem;
import jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import jsprit.core.reporting.SolutionPrinter;
import jsprit.instance.reader.SolomonReader;
import jsprit.util.*;
import main.OROoptions.CONSTANTS;
import main.OROoptions.PARAMS;

public class Main {
			
	public static void main(String[] args) {
		// Some preparation - create output folder
		Examples.createOutputFolder();
		
		// Read input parameters
		OROoptions options = new OROoptions(args);
		
		for(int r=0; r<(int)options.get(CONSTANTS.REPETITION); r++) {
			// Time tracking
			long startTime = System.currentTimeMillis();
			// Create a vrp problem builder
			VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
			// A solomonReader reads solomon-instance files, and stores the required information in the builder.
			new SolomonReader(vrpBuilder).read("input/" + options.get(PARAMS.INSTANCE));
			VehicleRoutingProblem vrp = vrpBuilder.build();
			// Create the instace and solve the problem
			VehicleRoutingAlgorithm vra = VehicleRoutingAlgorithms.readAndCreateAlgorithm(vrp, 
					(int)options.get(CONSTANTS.THREADS), (String)options.get(CONSTANTS.CONFIG));
			setTimeLimit(vra, (long)options.get(CONSTANTS.TIME));
			
			// Solve the problem
			Collection<VehicleRoutingProblemSolution> solutions = vra.searchSolutions();
			// Extract the best solution
			VehicleRoutingProblemSolution solution = new SelectBest().selectSolution(solutions);
									
			// Print solution on a file
			OROutils.write(solution, (String)options.get(PARAMS.INSTANCE), System.currentTimeMillis()-startTime, (String)options.get(CONSTANTS.OUTPUT));
			// Print solution on the screen (optional)
			//SolutionPrinter.print(vrp, solution, SolutionPrinter.Print.VERBOSE);
			// Draw solution on the screen (optional)
			//new GraphStreamViewer(vrp, solution).labelWith(Label.ID).setRenderDelay(10).display();
		}
	}
	
	private static void setTimeLimit(VehicleRoutingAlgorithm vra, long timeMilliSec) {
		TimeTermination tterm = new TimeTermination(timeMilliSec);
		vra.setPrematureAlgorithmTermination(tterm);
		vra.addListener(tterm);
	}
}
