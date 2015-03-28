package voxel.client.core.launcher.login;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Login {
	public static String appdata = System.getenv("APPDATA");
	public static String infoPath = appdata + "\\Assets\\Info.txt";

	@SuppressWarnings("resource")
	public static boolean authenticate(String username, String password) {
		Scanner fileScan;
		try {
			fileScan = new Scanner(new File(infoPath));
			while (fileScan.hasNextLine()) {
				String input = fileScan.nextLine();
				String username1 = input.substring(0, input.indexOf(' '));// Reads
																			// the
																			// username
				String password1 = input.substring(input.indexOf(' ') + 1,
						input.length()); // Reads the password
				new Login();
				if (username.equals(username1) && password.equals(password1)) {
					return true;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
}