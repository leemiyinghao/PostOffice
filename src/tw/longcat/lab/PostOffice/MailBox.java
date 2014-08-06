package tw.longcat.lab.PostOffice;

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
		if(boxChest.getSize() == 27){
			if(boxChestNear == null){
				return false;
			}else{
				boxChestNear.addItem(items);
			}
		}else{
			boxChest.addItem(items);
		}
		return true;
	}
	public boolean canMail(){
		if(boxChest.getSize() == 27 && boxChestNear == null){
			return false;
		}else if(boxChest.getSize() == 54 && boxChestNear != null){
			return false;
		}
		return true;
	}
}
