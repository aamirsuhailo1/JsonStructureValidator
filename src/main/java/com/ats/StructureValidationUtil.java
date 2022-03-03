/**
 * 
 */
package com.ats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author suhail
 *
 */
public class StructureValidationUtil {
	
	static List<String> masterList = new ArrayList<String>();
	
	public static List<String> validateJsonStructure(String actualMessageFromKafka, String expectedJsonMessage){
		
		List<String> pathexp = JsonParserUtil.getJsonPathsForGivenJson(expectedJsonMessage);
		
		List<String> pathact = JsonParserUtil.getJsonPathsForGivenJson(actualMessageFromKafka);
		
		List<String> copyExpListnewc = new ArrayList<String>();
		copyExpListnewc.addAll(pathexp);		
		List<String> copyActListnewc = new ArrayList<String>();
		copyActListnewc.addAll(pathact);
		
		List<String> copyExpList = new ArrayList<String>();
		copyExpList.addAll(pathexp);	
		List<String> copyActList = new ArrayList<String>();
		copyActList.addAll(pathact);
		
		List<String> copyExpListnew = new ArrayList<String>();
		copyExpListnew.addAll(pathexp);		
		List<String> copyActListnew = new ArrayList<String>();
		copyActListnew.addAll(pathact);
		
		String equalMsg = checkForJsonStructuresEquality(copyExpListnewc,copyActListnewc);
		if(equalMsg == null) {
			getMissingElements(pathexp,pathact);
			getAdditionalElements(copyExpList,copyActList);
		} else {
			masterList.add(equalMsg);
		}
		return masterList;
	}

	private static void getAdditionalElements(List<String> pathexp, List<String> pathact) {
		String message = null;
		pathact.removeAll(pathexp);
		message = "Additional elements in actual message : "+pathact;
		if(!pathact.toString().trim().equalsIgnoreCase("[]")) {
			masterList.add(message);
		}
		
		
	}

	private static void getMissingElements(List<String> pathexp, List<String> pathact) {
		String message = null;
		pathexp.removeAll(pathact);
		message = "Missing element from actual message : "+ pathexp;
		if(!pathexp.toString().trim().equalsIgnoreCase("[]")) {
			masterList.add(message);
		}
		
	}

	private static String checkForJsonStructuresEquality(List<String> copyExpListnewc, List<String> copyActListnewc) {
		String message = null;
		if(copyExpListnewc.size() != copyActListnewc.size()) {
			return message;
		}
		if(copyActListnewc.containsAll(copyExpListnewc)) {
			message = "Json structures are equal";
		}
		return message;
	}

	public static void getResultOfStructureValidation(List<String> resultOnStructureList) {
		System.out.println("Result of structure validation : ");
		if(resultOnStructureList.size() == 1 && resultOnStructureList.get(0).equalsIgnoreCase("Json structures are equal")) {
			System.out.println("Both structures are equal");			
		}
		else {
			StructureValidationUtil.displayMismatchedAndAdditionalFields(resultOnStructureList);
		}
		
	}

	private static void displayMismatchedAndAdditionalFields(List<String> resultOnStructureList) {
		for(String resultOnStructure : resultOnStructureList) {
			String vals = resultOnStructure.split(":")[1].trim();
			if(!vals.equalsIgnoreCase("[]")) {
				List<String> postmsg = Arrays.asList(resultOnStructure.split(":")[1].split(","));
				System.out.println(resultOnStructure.split(":")[0]);
				for(String path : postmsg) {
					System.out.println(path+"\n");
				}
			}
		}
		
	}

}
