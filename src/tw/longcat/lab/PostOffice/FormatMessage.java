package tw.longcat.lab.PostOffice;

import org.bukkit.ChatColor;

public class FormatMessage {
	static String pluginName = "FormatMessage";
	public static String info(String innerText){
		return "[" + pluginName + "] " + ChatColor.ITALIC + innerText;
	}
	public static String warning(String innerText){
		return "[" + pluginName + "] " + ChatColor.YELLOW + innerText;
	}
	public static String error(String innerText){
		return "[" + pluginName + "] " + ChatColor.RED + innerText;
	}
}
