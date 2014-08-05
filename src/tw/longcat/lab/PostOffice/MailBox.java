package tw.longcat.lab.PostOffice;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MailBox {
	private Inventory boxChest;
	MailBox(Inventory boxChest){
		this.boxChest = boxChest;
	}
	public boolean mail(ItemStack items){
		boxChest.addItem(items);
		return true;
	}
	public boolean canMail(){
		if(boxChest.getSize() == boxChest.getMaxStackSize())
			return false;
		return true;
	}
}
