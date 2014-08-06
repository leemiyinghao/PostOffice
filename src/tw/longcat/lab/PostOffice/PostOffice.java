package tw.longcat.lab.PostOffice;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class PostOffice extends JavaPlugin{
	MailSystem mailSys;
	public void onEnable(){
		this.saveDefaultConfig();
		mailSys = new MailSystem(this,"mailBox.db");
	}
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if(command.getLabel().equalsIgnoreCase("po") || 
			command.getLabel().equalsIgnoreCase("postoffice") || 
			command.getLabel().equalsIgnoreCase("mail")){
			if(args.length>1){
				if(args[0].equalsIgnoreCase("send")){
					if(!(sender instanceof Player))
						sender.sendMessage(FormatMessage.error("Only Player can do this."));
					Set<OfflinePlayer> whiteListedPlayer = getServer().getWhitelistedPlayers();
					boolean playerInList = false;
					for(OfflinePlayer offPlayer : whiteListedPlayer){
						if(offPlayer.getName().equalsIgnoreCase(args[1])){
							playerInList = true;
							break;
						}
					}
					if(playerInList){
						MailBox box = mailSys.getMailBox(getServer().getPlayer(args[1]));
						if(box!=null){
							if(!box.canMail())
								sender.sendMessage(FormatMessage.warning("There is no space in mailbox, place into queue."));
							ItemStack items = ((Player)sender).getItemInHand();
							box.mail(items);
							((Player)sender).setItemInHand(new ItemStack(0));
							sender.sendMessage(FormatMessage.info("Mail sent: " + args[1]));
							return true;
						}else{
							sender.sendMessage(FormatMessage.error("Player don't have mailbox: " + args[1]));
							return true;
						}
					}else{
						sender.sendMessage(FormatMessage.error("Player not found: " + args[1]));
						return true;
					}
				}
			}else if(args.length>0){
				if(args[0].equalsIgnoreCase("reload")){
					reload();
				}else if(args[0].equalsIgnoreCase("setmailbox")){
					if(!(sender instanceof Player))
						sender.sendMessage(FormatMessage.error("Only Player can do this."));
					Location loc = ((Player)sender).getTargetBlock(null, 15).getLocation();
					if(loc.getBlock().getType() == Material.CHEST){
						mailSys.setMailBox((Player)sender, loc);
						sender.sendMessage(FormatMessage.info("MailBox set."));
					}else{
						sender.sendMessage(FormatMessage.info("The block is not a Chest"));
					}
				}
			}
			sender.sendMessage(ChatColor.YELLOW + "/po send (receiver)" + ChatColor.WHITE+" : " + ChatColor.ITALIC + "Send item in hand to receiver.");
			sender.sendMessage(ChatColor.YELLOW + "/po check" + ChatColor.WHITE+" : " + ChatColor.ITALIC + "Check mailbox");
			sender.sendMessage(ChatColor.YELLOW + "/po setmailbox" + ChatColor.WHITE+" : " + ChatColor.ITALIC + "Setup a mailbox");
			return true;
		}
		return false;
	}
	public void reload(){
	}
}
