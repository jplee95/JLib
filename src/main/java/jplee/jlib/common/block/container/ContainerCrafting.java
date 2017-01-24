package jplee.jlib.common.block.container;

import java.util.Set;

import com.google.common.collect.Sets;

import jplee.jlib.common.block.container.slot.SlotIngredient;
import jplee.jlib.common.block.container.slot.SlotResult;
import jplee.jlib.common.block.tile.BaseInventory;
import jplee.jlib.util.Log;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;

public abstract class ContainerCrafting extends BaseInventoryContainer {

	private Set<Integer> matrixSlots;
	private Set<Integer> resultSlots;
	
	public ContainerCrafting(InventoryPlayer inventory, BaseInventory tile) {
		super(inventory, tile);
		
		this.matrixSlots = Sets.<Integer>newHashSet();
		this.resultSlots = Sets.<Integer>newHashSet();
	}
	
	protected void bindCraftingSlots(IInventory inventory, int x, int y, int w, int h, int gap) {
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				this.bindMatrixSlot(inventory, x + j * (18 + gap), y + i * (18 + gap));
			}
		}
	}
	
	protected void bindCraftingSlots(IInventory inventory, int x, int y, int w, int h) {
		this.bindCraftingSlots(inventory, x, y, w, h, 0);
	}
	
	protected void bindMatrixSlot(IInventory inventory, int x, int y) {
		this.bindMatrixSlot(new SlotIngredient(inventory, this, super.getNextSlotId(), x, y));
	}
	
	protected void bindMatrixSlot(SlotIngredient slot) {
		this.matrixSlots.add(super.getNextSlotId());
		Log.defaultInfo("M " + this.getNextSlotId(), new Object[0]); // TODO: REMOVE
		this.bindSlot(slot);
	}
	
	protected void bindResultSlots(IInventory inventory, int x, int y, int w, int h, int gap) {
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				this.bindResultSlot(inventory, x + j * (18 + gap), y + i * (18 + gap));
			}
		}
	}
	
	protected void bindResultSlots(IInventory inventory, int x, int y, int w, int h) {
		this.bindResultSlots(inventory, x, y, w, h, 0);
	}
	
	protected void bindResultSlot(IInventory inventory, int x, int y) {
		this.bindResultSlot(new SlotResult(inventory, this.inventory.player, this, super.getNextSlotId(), x, y));
	}
	
	protected void bindResultSlot(SlotResult slot) {
		this.resultSlots.add(super.getNextSlotId());
		Log.defaultInfo("R " + this.getNextSlotId(), new Object[0]); // TODO: REMOVE
		this.bindSlot(slot);
	}
	
	/** The slots that will be used for crafting **/
	public final Set<Integer> getSlotsForCrafting() {
		return this.matrixSlots;
	}
	
	public final Set<Integer> getSlotsForResults() {
		return this.resultSlots;
	}
	
	@Override
	public abstract void onCraftMatrixChanged(IInventory inventoryIn);
	public abstract void onSuccessfulCraft();
}
