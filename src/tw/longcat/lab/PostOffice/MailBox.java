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
		if(boxChest.firstEmpty() == -1 && boxChestNear != null){
			boxChestNear.addItem(items);
		}else{
			boxChest.addItem(items);
		}
		return true;
	}
	public boolean canMail(){
		if(boxChestNear == null){
			if(boxChest.firstEmpty() != -1)
				return true;
		}else{
			if(boxChest.firstEmpty() != -1 || boxChestNear.firstEmpty() != -1)
				return true;
		}
		return false;
	}
	public int countItemInChest(){
		int count = 0;
		for(ItemStack items : boxChest.getContents()){
			if(items.getAmount() > 0)
				count++;
		}
		if(boxChestNear != null){
			for(ItemStack items : boxChestNear.getContents()){
				if(items.getAmount() > 0)
					count++;
			}
		}
		return count;
	}
}
