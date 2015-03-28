package voxel.client.core.launcher;

public class ConstantsLauncher {

	public static String gameName = "Voxel";// Name of your app
	public static String launcherVersion = "Launcher Version 1.0.2 Beta";// Version of the launcher
	public static String url = "http://guerra24.tumblr.com/";// Web page
	
	public static String appdata = System.getenv("APPDATA");
	public static String iconPath1 = appdata +  "\\Assets\\LAUNCHER_icon.png";
	public static String iconPath2 = appdata +  "\\Assets\\LAUNCHER_icon1.png";
	public static String optionsPath = appdata + "\\Assets\\options.conf";// Location of the user info
	public static String versionpath = appdata + "\\Assets\\Version.txt";// Version file
	public static String assetspath = appdata + "\\Assets\\VersionAssets.txt";// Version file
	public static String logpath = appdata + "\\Assets\\Log.txt";// Version file
	
	
	public static String download1 = "https://dl.dropboxusercontent.com/u/64652561/LAUNCHER_icon.png";// Launcher Icon
	public static String download2 = "https://dl.dropboxusercontent.com/u/64652561/LAUNCHER_icon1.png";// Launcher background image
	public static String download3 = "https://dl.dropboxusercontent.com/u/64652561/update.txt";// Version assigner a.k.a Check the version
	public static String download4 = "https://dl.dropboxusercontent.com/u/64652561/updateAssets.txt";// Version assigner a.k.a Check the version
	public static String userInfo = "https://dl.dropboxusercontent.com/u/64652561/Info.txt";// Location of the user info
}