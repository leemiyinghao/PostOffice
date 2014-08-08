package tw.longcat.lab.PostOffice;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import tw.longcat.lab.PostOffice.DataBase.FileDataBase;

public class MailSystem {
	FileDataBase mailBoxDB;
	FileDataBase mailQueueDB;
	Plugin po;
	MailSystem(Plugin po){
		this.po = po;
		mailBoxDB = new FileDataBase(po.getDataFolder() + "/" + "mailBox.db");
		mailQueueDB = new FileDataBase(po.getDataFolder() + "/" + "mailQueue.db");
	}
	public MailBox getMailBox(OfflinePlayer offlinePlayer){
		if(mailBoxDB.hasKey(offlinePlayer.getName())){
			String[] locArgs = mailBoxDB.getValue(offlinePlayer.getName()).split(",");
			Location loc = new Location(po.getServer().getWorld(locArgs[0]),
											Double.parseDouble(locArgs[1]),
											Double.parseDouble(locArgs[2]),
											Double.parseDouble(locArgs[3]));
			try{
				if(loc.getBlock().getType() != Material.CHEST)
					throw new NotChestException();
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
				MailBox mailBox = new MailBox(((Chest)loc.getBlock().getState()).getBlockInventory(),chestNear!=null?chestNear.getBlockInventory():null);
				if(mailBox.canMail()){
					return mailBox;
				}else{
					MailQueue mailQueue = new MailQueue(mailQueueDB,offlinePlayer);
					return mailQueue;
				} 
			}catch(NotChestException e){
				mailBoxDB.clearRow(offlinePlayer.getName());
			}catch(Exception e){
			}
		}
		return null;
	}
	public boolean setMailBox(Player player,Location loc){
		mailBoxDB.setValue(player.getName(),
							 String.format("%s,%d,%d,%d",loc.getWorld().getName(),loc.getBlockX(),loc.getBlockY(),loc.getBlockZ()));
		return true;
	}
	public boolean resetMailBox(Player player){
		mailBoxDB.clearRow(player.getName());
		return true;
	}
	public ItemStack pullQueue(Player player){
		String[] data = mailQueueDB.searchInnerValue(player.getName());
		if(data == null)
			return null;
		mailQueueDB.clearRow(data[0]);
		return MailQueue.stringToItemStack(data[1].split(",")[1]);
	}
	public int countQueue(Player player){
		return mailQueueDB.countInnerValue(player.getName());
	}
}
