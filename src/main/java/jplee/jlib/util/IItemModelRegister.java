package jplee.jlib.util;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public interface IItemModelRegister {

	public default void registerItemModel(Item item, int meta, ResourceLocation location) {
		this.registerItemModel(item, meta, location.toString());
	}
	
	public void registerItemModel(Item item, int meta, String id);
	
	
}
