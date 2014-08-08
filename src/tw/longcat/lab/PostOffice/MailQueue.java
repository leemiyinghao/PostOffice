package tw.longcat.lab.PostOffice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;

import net.minecraft.server.v1_4_6.NBTBase;
import net.minecraft.server.v1_4_6.NBTTagCompound;

import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_4_6.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import tw.longcat.lab.PostOffice.DataBase.FileDataBase;


public class MailQueue extends MailBox{
	FileDataBase mailQueueDB;
	OfflinePlayer offlinePlayer;
	MailQueue(FileDataBase mailQueueDB,OfflinePlayer offlinePlayer) {
		super(null, null);
		this.mailQueueDB = mailQueueDB;
		this.offlinePlayer = offlinePlayer;
	}
	public boolean mail(ItemStack items){
		int key;
		for(key = 0;mailQueueDB.hasKey(String.valueOf(key));key++);
		mailQueueDB.setValue(String.valueOf(key), offlinePlayer.getName() + "," + itemstackToString(items));
		System.out.println("Queue");
		return true;
	}
	public boolean canMail(){
		return false;
	}
	static public String itemstackToString(ItemStack is) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutput = new DataOutputStream(outputStream);
		NBTTagCompound outputObject = new NBTTagCompound();
		CraftItemStack craft = getCraftVersion(is);
		if (craft != null)
			CraftItemStack.asNMSCopy(craft).save(outputObject);
		NBTBase.a(outputObject, dataOutput);
		return new BigInteger(1, outputStream.toByteArray()).toString(32);
	}
	static public ItemStack stringToItemStack(String str) {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(new BigInteger(str, 32).toByteArray());
		NBTTagCompound item = (NBTTagCompound) NBTBase.b(new DataInputStream(inputStream));
		return CraftItemStack.asCraftMirror(net.minecraft.server.v1_4_6.ItemStack.a(item));
	}
	private static CraftItemStack getCraftVersion(ItemStack stack) {
		if (stack instanceof CraftItemStack)
			return (CraftItemStack) stack;
		else if (stack != null)
			return CraftItemStack.asCraftCopy(stack);
		else
			return null;
	}
}
