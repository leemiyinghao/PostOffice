package tw.longcat.lab.PostOffice.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;

import tw.longcat.lab.PostOffice.FormatMessage;
import tw.longcat.lab.PostOffice.MailBox;
import tw.longcat.lab.PostOffice.MailSystem;

public class PlayerLoginListener implements Listener{
	Plugin plugin;
	MailSystem mailSys;
	public PlayerLoginListener(Plugin plugin,MailSystem mailSys){
		this.plugin = plugin;
		this.mailSys = mailSys;
	}
	@EventHandler
	public boolean onPlayerLogin(PlayerLoginEvent e){
		Player player = e.getPlayer();
		MailBox mailbox = mailSys.getMailBox(player);
		System.out.println("Player Logged.");
		if(mailbox != null){
			if(mailbox.canMail())
				player.sendMessage(FormatMessage.info(String.format("You have %d mail(s) in your mailbox.",mailbox.countItemInChest())));
			else if(mailSys.countQueue(player) == 0)
				player.sendMessage(FormatMessage.warning("Your mailbox is full."));
			else
				player.sendMessage(FormatMessage.warning(String.format("Your mailbox is full, %d mail(s) in queue.",mailSys.countQueue(player))));
		}
		return true;
	}
}
