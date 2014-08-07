package tw.longcat.lab.PostOffice;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MailBox {
	private Inventory boxChest;
	private Inventory boxChestNear;
	MailBox(Inventory boxChest,Inventory boxChestNear){
		this.boxChest = boxChest;
		this.boxChestNear = boxChestNear;
	}
	public boolean mail(ItemStack items){
		boxChest.addItem(items);
		System.out.println("a");
		return true;
	}
	public boolean canMail(){
		if(boxChest.firstEmpty() != -1){
			return true;
		}
		return false;
	}
}
