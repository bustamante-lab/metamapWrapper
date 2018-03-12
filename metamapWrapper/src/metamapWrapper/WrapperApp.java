package metamapWrapper;

import java.io.File;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;

/*
 * 
 * Project: Clinical Note Tagger
 * 
 * author: Arturo Lopez Pineda
 * 
 * Mar 7, 2018
 * 
 */


import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FilenameUtils;

public class WrapperApp {
	
	
	private static Options options;
	public static String WrapperVersion = "1.0";
	public static String MetaMapVersion = "Lite 3.6.1";
	public static String CLIVersion = "1.4";
	public static String IOVersion = "2.6";
	public static String OpenCSVVersion = "4.1";

	public static void createOptions() {
		// Create Options object
		options = new Options();
		
		// Help options
		Option help = new Option( "help", "Print this message" );
		Option version = new Option( "version", "Print the version information and exit" );

		// App specific options
		Option input = Option.builder("input")
				.argName("inputFile")
				.hasArg()
				.required(true)
				.desc("Is the input file with three columns including patientID, clinical narrative and disease code (for training)")
				.build();
		Option output = Option.builder("output")
				.argName("eavFileName")
				.valueSeparator(' ')
				.numberOfArgs(2)
				.desc("The name of output file in EAV format. Default is the same as input with a modifier \"_eav\"")
				.build();
		Option patientCol = Option.builder("patient")
				.argName("columnNumber")
				.valueSeparator(' ')
				.numberOfArgs(2)
				.desc("The column number where the patient ID is located. Default is 0")
				.build();
		Option narrativeCol = Option.builder("narrative")
				.argName("columnNumber")
				.valueSeparator(' ')
				.numberOfArgs(2)
				.desc("The column number where the narrative text is located. Default is 1")
				.build();
		Option diseaseCol = Option.builder("disease")
				.argName("columnNumber")
				.valueSeparator(' ')
				.numberOfArgs(2)
				.desc("The column number where the disease or top level code is located (only for training). Default is 2")
				.build();

		// add default option
		options.addOption(help);
		options.addOption(version);

		//add custom options
		options.addOption(input);
		options.addOption(output);
		options.addOption(patientCol);
		options.addOption(narrativeCol);
		options.addOption(diseaseCol);
		

	}


	public static void main(String[] args){
		
		//String inputFile = "/Users/arturolp/Documents/Stanford/CNT-MetaMap/data/mimic_small.csv";
		//String eavFile = "/Users/arturolp/Documents/Stanford/CNT-MetaMap/data/mimic_small_eav.csv";
		
		createOptions();
		
		//Default values
		String inputFile = "";
		String outputFile = "";
		int patientCol = 0;
		int narrativeCol = 1; 
		int diseaseCol = 2;
		
		// Parsing the command line arguments
				CommandLineParser parser = new DefaultParser();
				try {
					CommandLine line = parser.parse( options, args);
					
					if (line.hasOption("help")) {
			            HelpFormatter formatter = new HelpFormatter();
			            
			            formatter.printHelp("HELP:", options, true);
			        }
					else if (line.hasOption("version")) {
						System.out.println("=====================================");
			            System.out.println("Wrapper version: \t" + WrapperVersion);
			            System.out.println("=====================================");
			            System.out.println("\nUsing the following packages>");
			            System.out.println("\tNLM MetaMap version: " + MetaMapVersion);
			            System.out.println("\tApache Commons CLI version: " + CLIVersion);
			            System.out.println("\tApache Commons IO version: " + IOVersion);
			            System.out.println("\tOpenCSV version: " + OpenCSVVersion);
			            System.out.println("\nGet current version at: \n\thttps://github.com/bustamante-lab/metamapWrapper");
			        } else {
						
						if (line.hasOption("input")) {
							System.out.println("=====================================");
				            System.out.println("Running MetaMap Wrapper");
				            System.out.println("=====================================\n");
				           
				            //Read input filename
				            	inputFile = line.getOptionValue("input");
				            	System.out.println("Reading input file: " + inputFile);
				            	
				            	//Read output filename
				            	if (line.hasOption("output")) {
				            		outputFile = line.getOptionValue("output");   	
				            	}
				            	else {
				            		File file = new File(inputFile);
				            		String filename = file.getName();
				            		String path = FilenameUtils.getFullPath(file.getPath());
				            		String base = FilenameUtils.removeExtension(filename);
				            		String extension = FilenameUtils.getExtension(filename);
				            		outputFile = path + base + "_eav" + "." + extension;
				            	}
				            	System.out.println("Reading input file: " + outputFile);
				            	
				            	
				            	//Read patientCol
				            	if (line.hasOption("patientCol")) {
				            		patientCol = Integer.parseInt(line.getOptionValue("patientCol"));
				            	}
				            	System.out.println("Patient information in column: " + patientCol);
				            	
				            	//Read discretizeCol
				            	if (line.hasOption("narrativeCol")) {
				            		narrativeCol = Integer.parseInt(line.getOptionValue("narrativeCol"));
				            	}
				            	System.out.println("Narrative information in column: " + narrativeCol);
				            	
				            	//Read diseaseCol
				            	if (line.hasOption("diseaseCol")) {
				            		diseaseCol = Integer.parseInt(line.getOptionValue("diseaseCol"));
				            	}
				            	System.out.println("Disease (target) information in column: " + diseaseCol);
				            	
				            	//-------------------------------
				            	//Calling MetaMap NoteTagger
				            	//-------------------------------
				            	System.out.println("\nRunning MetaMap...");
				            	NoteTagger nt = new NoteTagger();
				        		nt.callMetaMap(inputFile, outputFile, patientCol, narrativeCol, diseaseCol);
				            	
				        }
						
					}
					
					
				} catch (ParseException exp) {
					System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
				}

		



	}
	


}
