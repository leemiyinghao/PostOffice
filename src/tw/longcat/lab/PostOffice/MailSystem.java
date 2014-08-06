package tw.longcat.lab.PostOffice;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import tw.longcat.lab.PostOffice.DataBase.FileDataBase;

public class MailSystem {
	FileDataBase mailBoxDB;
	Plugin po;
	MailSystem(Plugin po,String dbFilaName){
		this.po = po;
		mailBoxDB = new FileDataBase(po.getDataFolder() + "/" + dbFilaName);
	}
	public MailBox getMailBox(Player player){
		if(mailBoxDB.hasKey(player.getName())){
			String[] locArgs = mailBoxDB.getValue(player.getName()).split(",");
			Location loc = new Location(po.getServer().getWorld(locArgs[0]),
											Double.parseDouble(locArgs[1]),
											Double.parseDouble(locArgs[2]),
											Double.parseDouble(locArgs[3]));
			try{
				Chest chest = (Chest)loc.getBlock().getState();
				Chest chestNear = null;
				if(chest.getLocation().clone().add(0, 0, 1).getBlock().getType() == Material.CHEST){
					chestNear = (Chest)chest.getLocation().clone().add(0, 0, 1).getBlock().getState();
				}else if(chest.getLocation().clone().add(0, 0, -1).getBlock().getType() == Material.CHEST){
					chestNear = (Chest)chest.getLocation().clone().add(0, 0, -1).getBlock().getState();
				}else if(chest.getLocation().clone().add(1, 0, 0).getBlock().getType() == Material.CHEST){
					chestNear = (Chest)chest.getLocation().clone().add(1, 0, 0).getBlock().getState();
				}else if(chest.getLocation().clone().add(-1, 0, 0).getBlock().getType() == Material.CHEST){
					chestNear = (Chest)chest.getLocation().clone().add(-1, 0, 0).getBlock().getState();
				}
				return new MailBox(((Chest)loc.getBlock().getState()).getBlockInventory(),chestNear!=null?chestNear.getBlockInventory():null);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}
	public boolean setMailBox(Player player,Location loc){
			mailBoxDB.setValue(player.getName(),
								 String.format("%s,%d,%d,%d",loc.getWorld().getName(),loc.getBlockX(),loc.getBlockY(),loc.getBlockZ()));
			return true;
	}
}
