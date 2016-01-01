package jsprit.core.problem.constraint;

import jsprit.core.problem.VehicleRoutingProblem;
import jsprit.core.problem.misc.JobInsertionContext;

/**
 * Constraint soft per effettuare il bilanciamento del carico per ogni route
 * 
 * Ad ogni inserimento viene calcolata una penalità o una ricompensa per quell'inserimento, facendo la differenza tra il numero di
 * job nella route attuale e il numero di job ideale calcolato come n° di job su n° di veicoli
 * 
 * 
 */


public class BalancerSoftConst implements SoftRouteConstraint {
	
	private VehicleRoutingProblem vrp;
	private double meanValue;

	public BalancerSoftConst(VehicleRoutingProblem vrp) {
		super();
		this.vrp = vrp;
		meanValue = vrp.getJobs().size() / vrp.getVehicles().size();
	}

	@Override
	public double getCosts(JobInsertionContext insertionContext) {
		double value = (insertionContext.getRoute().getActivities().size() - meanValue);
		if(value < 0)
			return value;
		else
		return value * 10;
	}

}
