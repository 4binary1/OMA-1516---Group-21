package main;

import java.util.EnumMap;
import java.util.HashMap;

/**
 * 
 */
public class OROoptions {
	public enum PARAMS{	INSTANCE, FLEET; }
	public enum CONSTANTS{ CONFIG, THREADS, HTML, OUTPUT, TIME, REPETITION; }

	private final String INSTANCE_ARG 	= "i";
	private final String INSTANCE_TXT 	= "Instance file (Solomon Format)";
	private final String INSTANCE_FILE	= "C101.txt";
	
	//+
	private final String FLEET_ARG		= "f";
	private final String FLEET_TXT		= "Number of vehicles";
	private final String FLEET_VALUE	= "0";
	//-
	
	private final String CONFIG_ALG_C	= "input/algorithmConfig.xml";
	private final String SOL_PATH_C		= "output/solutions.csv";
	private final String HTML_PATH_C	= "output/solution";
	private final long TIME_C			= 120*1000;
	private final int THREADS_C			= 1;
	private final int REPETITION_C		= 1;
	
	// Command line help
	//+
	private String CMD_MSG = "Available commands are:\n" +
		"-" + INSTANCE_ARG 	+": "+ INSTANCE_TXT + "\n" +
		"-" + FLEET_ARG + ": "+ FLEET_TXT + "\n";
	//-
	
	HashMap<String, String> options;
	EnumMap<PARAMS, String> references;
	HashMap<CONSTANTS, Object> constants;
			
	public OROoptions(String[] args) {
		options = new HashMap<String, String>();
		options.put(INSTANCE_ARG, INSTANCE_FILE);
		//+
		options.put(FLEET_ARG, FLEET_VALUE);
		//-
		
		references = new EnumMap<PARAMS, String>(PARAMS.class);
		references.put(PARAMS.INSTANCE, INSTANCE_ARG);
		//+
		references.put(PARAMS.FLEET, FLEET_ARG);
		//-
		
		constants = new HashMap<CONSTANTS, Object>();
		constants.put(CONSTANTS.CONFIG, CONFIG_ALG_C);
		constants.put(CONSTANTS.HTML, HTML_PATH_C);
		constants.put(CONSTANTS.THREADS, THREADS_C);
		constants.put(CONSTANTS.TIME, TIME_C);
		constants.put(CONSTANTS.OUTPUT, SOL_PATH_C);
		constants.put(CONSTANTS.REPETITION, REPETITION_C);
			
		for(int i=0; i<args.length; i++){
			String arg = "";
			// Manage error in command line
			if(args[i].charAt(0) != '-'){
				System.out.println("No command exists for " + args[i]);
				System.out.println(CMD_MSG);
				System.exit(1);
			}
			// Manage double and single -
			if(args[i].substring(0,1).equals("--"))
				arg = args[i].substring(2).toLowerCase();
			else if(args[i].charAt(0) == '-')
				arg = args[i].substring(1).toLowerCase();
			// Check command
			if(!options.containsKey(arg)) {
				System.out.println("No command exists for " + args[i]);
				System.out.println(CMD_MSG);
				System.exit(1);
			}
			// Change default configuration
			options.put(arg, args[i+1]);
			i++;
		}
	}
	/**
	 * Return the value of parameter
	 * @param param the identifier of the parameter
	 * @return value of parameter
	 */
	public String get(PARAMS param) {
		return options.get(references.get(param));
	}
	/**
	 * Return the value of selected constant. 
	 * The value must be casted.
	 * @param c the idetifier of the constant
	 * @return value of constant
	 */
	public Object get(CONSTANTS c){
		return constants.get(c);
	}
}
