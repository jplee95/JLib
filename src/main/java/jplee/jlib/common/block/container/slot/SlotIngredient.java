package jplee.jlib.common.block.container.slot;

import jplee.jlib.common.block.container.ContainerCrafting;
import jplee.jlib.util.Log;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotIngredient extends Slot {

	private ContainerCrafting eventHandeler;
	
	public SlotIngredient(IInventory inventoryIn, ContainerCrafting container, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
		eventHandeler = container;
	}

	@Override
	public void onSlotChange(ItemStack p_75220_1_, ItemStack p_75220_2_) {
		// TODO Auto-generated method stub
		super.onSlotChange(p_75220_1_, p_75220_2_);
	}
	
	@Override
	protected void onCrafting(ItemStack stack, int amount) {
		Log.defaultInfo("onCrafting", new Object[0]);
		super.onCrafting(stack, amount);
	}
	
	@Override
	public void onSlotChanged() {
		this.eventHandeler.onCraftMatrixChanged(this.inventory);
		this.inventory.markDirty();
	}
	
	
}
