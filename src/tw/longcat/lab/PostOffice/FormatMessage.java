package tw.longcat.lab.PostOffice;

import org.bukkit.ChatColor;

public class FormatMessage {
	static String pluginName = "PostOffice";
	public static String info(String innerText){
		return "[" + pluginName + "] " + ChatColor.AQUA + innerText;
	}
	public static String warning(String innerText){
		return "[" + pluginName + "] " + ChatColor.YELLOW + innerText;
	}
	public static String error(String innerText){
		return "[" + pluginName + "] " + ChatColor.RED + innerText;
	}
}
