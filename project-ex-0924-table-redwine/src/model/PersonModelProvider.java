package model;

import java.util.ArrayList;
import java.util.List;

import util.UtilFile;

public enum PersonModelProvider {
	INSTANCE(getFilePath());

	private List<Person> persons;

	private PersonModelProvider(String filePath) {
		List<String> contents = UtilFile.readFile(filePath);
		List<List<String>> tableContents = UtilFile.convertTableContents(contents);
		persons = new ArrayList<Person>();
		for (List<String> iList : tableContents) {
			persons.add(new Person(iList.get(0), iList.get(1), iList.get(2), iList.get(3)));
		}
	}

	public List<Person> getPersons() {
		return persons;
	}

	public void addPersons(List<Person> newPersons) {
		persons.addAll(newPersons);
	}

	private static String getFilePath() {
		return "/Users/SCR/Documents/College/Modern SW Development Methods/workspaceCSCI8710-Redwine/project-ex-0924-table-redwine/input_init.csv";
	}
}
