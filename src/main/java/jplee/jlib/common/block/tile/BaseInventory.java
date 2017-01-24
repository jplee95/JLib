package jplee.jlib.common.block.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public abstract class BaseInventory extends BaseTileEntity implements ISidedInventory {

	public ItemStack[] storedInventory = new ItemStack[this.getSizeInventory()];
	
	public BaseInventory() {
		super();
	}
	
	@Override
	public ItemStack getStackInSlot(int index) {
		if(index >= 0 && index < this.getSizeInventory())
			return this.storedInventory[index];
		return null;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		if(this.storedInventory[index] != null) {
			ItemStack itemStack;
			
			if(this.storedInventory[index].stackSize <= count) {
				itemStack = this.storedInventory[index];
				this.storedInventory[index] = null;
				
				this.markDirty();
				return itemStack;
			} else {
				itemStack = this.storedInventory[index].splitStack(count);
				
				if(this.storedInventory[index].stackSize == 0) {
					this.storedInventory[index] = null;
				}
				
				this.markDirty();
				return itemStack;
			}
		}
		return null;
	}
	
	@Override
	public void getNBT(NBTTagCompound compound) {
		NBTTagList tagList = compound.getTagList("inventory", 10);
		for(int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = tagList.getCompoundTagAt(i);
			int slot = tag.getByte("slot") & 255;
			if(slot >= 0 && slot < this.getSizeInventory()) {
				this.storedInventory[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
	}
	
	@Override
	public void setNBT(NBTTagCompound compound) {
		NBTTagList itemList = new NBTTagList();
		for(int i = 0; i < this.getSizeInventory(); i++) {
			ItemStack stack = storedInventory[i];
			if(stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}
		compound.setTag("inventory", itemList);
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack stack = getStackInSlot(index);
		if(stack != null) {
			this.storedInventory[index] = null;
			this.markDirty();
		}
		return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.storedInventory[index] = stack;
		this.markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.getPos()) == this && player.getDistanceSq(this.pos.add(0.5d, 0.5d, 0.5d)) <= 64.0d;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public void clear() {
		for(int i = 0; i < this.getSizeInventory(); i++)
			this.storedInventory[i] = null;
	}

	@Override
	public boolean hasCustomName() {
		if(this.getName() != null)
			return this.getName().length() > 0;
		return false;
	}

	@Override
	public ITextComponent getDisplayName() { 
		return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
	}
	
//	@Override
//	public int[] getSlotsForFace(EnumFacing side) {
//		return null;
//	}
//
//	@Override
//	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
//		return false;
//	}
//
//	@Override
//	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
//		return false;
//	}
	
	@Override public void openInventory(EntityPlayer player) { } // NO-OP
	@Override public void closeInventory(EntityPlayer player) { } // NO-OP
	@Override public int getField(int id) { return 0; } // NO-OP
	@Override public void setField(int id, int value) { } // NO-OP
	@Override public int getFieldCount() { return 0; } // NO-OP

}
