package tw.longcat.lab.PostOffice;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.yi.acru.bukkit.Lockette.Lockette;


import tw.longcat.lab.PostOffice.Listener.PlayerLoginListener;

public class PostOffice extends JavaPlugin{
	MailSystem mailSys;
	PlayerLoginListener playerLoginListener;
	Lockette lockette;
	public void onEnable(){
		this.saveDefaultConfig();
		mailSys = new MailSystem(this);
		playerLoginListener = new PlayerLoginListener(this,mailSys);
		getServer().getPluginManager().registerEvents(playerLoginListener, this);
	}
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if(command.getLabel().equalsIgnoreCase("po") || 
			command.getLabel().equalsIgnoreCase("postoffice") || 
			command.getLabel().equalsIgnoreCase("mail")){
			if(args.length>1){
				if(args[0].equalsIgnoreCase("send")){
					if(!(sender instanceof Player)){
						sender.sendMessage(FormatMessage.error("Only Player can do this."));
						return true;
					}
					if(!sender.hasPermission("postoffice.sendmail")){
						sender.sendMessage(FormatMessage.error("You have no permission to do this."));
						return true;
					}
					if(((Player)sender).getItemInHand().getAmount() == 0){
						sender.sendMessage(FormatMessage.error("You have to put something on your hand."));
						return true;
					}
					boolean playerInList = getServer().getOfflinePlayer(args[1]).hasPlayedBefore();
					if(playerInList){
						MailBox box = mailSys.getMailBox(getServer().getOfflinePlayer(args[1]));
						if(box != null){
							if(!(box.canMail()))
								sender.sendMessage(FormatMessage.warning("There is no space in mailbox, place into queue."));
							ItemStack items = ((Player)sender).getItemInHand();
							box.mail(items);
							((Player)sender).setItemInHand(new ItemStack(0));
							sender.sendMessage(FormatMessage.info("Mail sent: " + args[1]));
							//Info Receiver
							if(getServer().getOfflinePlayer(args[1]).isOnline()){
								getServer().getPlayer(args[1]).sendMessage(FormatMessage.info("You've got mail."));
							}
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
					if(sender instanceof Player && !sender.hasPermission("postoffice.reload")){
						sender.sendMessage(FormatMessage.error("You have no permission to do this."));
						return true;
					}
					reload();
					sender.sendMessage(FormatMessage.info("Reload complete."));
					return true;
				}else if(args[0].equalsIgnoreCase("setmailbox")){
					if(!(sender instanceof Player)){
						sender.sendMessage(FormatMessage.error("Only Player can do this."));
						return true;
					}
					if(sender instanceof Player && !sender.hasPermission("postoffice.setmailbox")){
						sender.sendMessage(FormatMessage.error("You have no permission to do this."));
						return true;
					}
					if(mailSys.getMailBox((Player)sender) != null){
						sender.sendMessage(FormatMessage.error("You already have mailbox, resetmailbox if you need a new one."));
						return true;
					}
					Location loc = ((Player)sender).getTargetBlock(null, 15).getLocation();
					if(loc.getBlock().getType() == Material.CHEST){
						mailSys.setMailBox((Player)sender, loc);
						sender.sendMessage(FormatMessage.info("MailBox set."));
					}else{
						sender.sendMessage(FormatMessage.info("The block is not a Chest"));
					}
					return true;
				}else if(args[0].equalsIgnoreCase("pull")){
					if(!(sender instanceof Player))
						sender.sendMessage(FormatMessage.error("Only Player can do this."));
					MailBox box = mailSys.getMailBox((Player)sender);
					if(box!=null){
						while(box.canMail()){
							ItemStack items = mailSys.pullQueue((Player)sender);
							if(items == null){
								sender.sendMessage(FormatMessage.info("There is no more mail in queue."));
								return true;
							}
							box.mail(items);
						}
						sender.sendMessage(FormatMessage.warning("Mailbox is full, can not pull more mail from queue."));
						return true;
					}else{
						sender.sendMessage(FormatMessage.error("You don't have mailbox."));
					}
					return true;
				}else if(args[0].equalsIgnoreCase("check")){
					if(!(sender instanceof Player)){
						sender.sendMessage(FormatMessage.error("Only Player can do this."));
						return true;
					}
					if(mailSys.getMailBox((Player)sender) == null){
						sender.sendMessage(FormatMessage.error("You don't have mailbox."));
						return true;
					}
					int queueNum = mailSys.countQueue((Player)sender);
					int mailboxNum = mailSys.getMailBox((Player)sender).countItemInChest();
					if(mailboxNum == 0){
						sender.sendMessage(FormatMessage.info("You have no mail in mailbox now."));
					}else{
						sender.sendMessage(FormatMessage.info(String.format("You have %d mail(s) in mailbox.", mailboxNum)));
					}
					if(queueNum == 0){
						sender.sendMessage(FormatMessage.info("You have no mail in queue now."));
					}else{
						sender.sendMessage(FormatMessage.info(String.format("You have %d mail(s) in queue, please pull them as soon.", queueNum)));
					}
					return true;
				}else if(args[0].equalsIgnoreCase("resetmailbox")){
					if(!(sender instanceof Player)){
						sender.sendMessage(FormatMessage.error("Only Player can do this."));
						return true;
					}
					if(sender instanceof Player && !sender.hasPermission("postoffice.setmailbox")){
						sender.sendMessage(FormatMessage.error("You have no permission to do this."));
						return true;
					}
					if(mailSys.getMailBox((Player)sender) == null){
						sender.sendMessage(FormatMessage.error("You don't have mailbox."));
						return true;
					}
					mailSys.resetMailBox((Player)sender);
					sender.sendMessage(FormatMessage.info("You have no mailbox now."));
					return true;
				}else if(args[0].equalsIgnoreCase("whereismymailbox")){
					if(!(sender instanceof Player)){
						sender.sendMessage(FormatMessage.error("Only Player can do this."));
						return true;
					}
					if(mailSys.getMailBox((Player)sender) == null){
						sender.sendMessage(FormatMessage.error("You don't have mailbox."));
						return true;
					}
					Location loc = mailSys.whereismymailbox((Player)sender);
					sender.sendMessage(FormatMessage.info(String.format("You mailbox is at %s:(%d,%d,%d)",loc.getWorld().getName(),loc.getBlockX(),loc.getBlockY(),loc.getBlockZ())));
					return true;
				}
			}
			sender.sendMessage(ChatColor.YELLOW + "/po send (receiver)" + ChatColor.WHITE+" : " + ChatColor.ITALIC + "Send item on hand to receiver");
			sender.sendMessage(ChatColor.YELLOW + "/po check" + ChatColor.WHITE+" : " + ChatColor.ITALIC + "Check mailbox");
			sender.sendMessage(ChatColor.YELLOW + "/po pull" + ChatColor.WHITE+" : " + ChatColor.ITALIC + "Pull mail out of queue");
			sender.sendMessage(ChatColor.YELLOW + "/po setmailbox" + ChatColor.WHITE+" : " + ChatColor.ITALIC + "Register mailbox(Type while you point at a chest)");
			sender.sendMessage(ChatColor.YELLOW + "/po resetmailbox" + ChatColor.WHITE+" : " + ChatColor.ITALIC + "Unregister mailbox");
			sender.sendMessage(ChatColor.YELLOW + "/po whereismymailbox" + ChatColor.WHITE+" : " + ChatColor.ITALIC + "Where is my mailbox?");
			return true;
		}
		return false;
	}
	public void reload(){
	}
}
