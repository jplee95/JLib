package jplee.jlib.common.block.container;

import jplee.jlib.common.block.tile.BaseInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class BaseInventoryContainer extends BaseBlockContainer {

	protected InventoryPlayer inventory;
	protected BaseInventory tile;
	private int nextSlotId = 0;
	
	public BaseInventoryContainer(InventoryPlayer inventory, BaseInventory tile) {
		super(inventory, tile);
	}

	protected void bindSlots(IInventory inventory, int x, int y, int w, int h) {
		bindSlots(inventory, x, y, w, h, 0);
	}
	
	protected void bindSlots(IInventory inventory, int x, int y, int w, int h, int gap) {
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				this.bindSlot(inventory, x + j * (18 + gap), y + i * (18 + gap));
			}
		}
	}
	
	protected void bindSlot(IInventory inventory, int x, int y) {
		this.addSlotToContainer(new Slot(inventory, this.nextSlotId++, x, y));
	}
	
	protected void bindSlot(Slot slot) {
		slot.slotNumber = nextSlotId++;
		this.addSlotToContainer(slot);
	}
	
	protected int getNextSlotId() {
		return nextSlotId;
	}
	
	protected void bindPlayerInventory(InventoryPlayer inventory, int x, int y) {
		for(int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(inventory, i, x + i * 18, y + 58));
		}
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(inventory, j + i * 9 + 9, x + j * 18, y + i * 18));
			}
		}
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
	    ItemStack itemstack = null;
	    Slot slot = (Slot)this.inventorySlots.get(index);
	
	    if(slot != null && slot.getHasStack()) {
	        ItemStack itemstack1 = slot.getStack();
	        itemstack = itemstack1.copy();
	
	        if(index < this.tile.getSizeInventory()) {
	            if(!this.mergeItemStack(itemstack1, this.tile.getSizeInventory(), this.inventorySlots.size(), true)) {
	                return null;
	            }
	        } else if(!this.mergeItemStack(itemstack1, 8, this.tile.getSizeInventory(), false)) {
	            return null;
	        }
	
	        if(itemstack1.stackSize == 0) {
	            slot.putStack((ItemStack) null);
	        }
	        else {
	            slot.onSlotChanged();
	        }
	    }
	    return itemstack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return tile.isUseableByPlayer(playerIn) && super.canInteractWith(playerIn);
	}
}
