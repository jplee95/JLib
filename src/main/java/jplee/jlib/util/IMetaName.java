package jplee.jlib.util;

import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface IMetaName {

	public String getSpecialName(ItemStack stack);
}
