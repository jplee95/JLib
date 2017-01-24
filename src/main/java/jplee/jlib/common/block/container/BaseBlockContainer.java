package jplee.jlib.common.block.container;

import jplee.jlib.common.block.tile.BaseTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

public class BaseBlockContainer extends BaseContainer {

	protected InventoryPlayer inventory;
	protected BaseTileEntity tile;
	
	public BaseBlockContainer(InventoryPlayer inventory, BaseTileEntity tile) {
		this.inventory = inventory;
		this.tile = tile;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return playerIn.getDistanceSq(this.tile.getPos().add(0.5d, 0.5d, 0.5d)) <= 64.0d;
	}

}
