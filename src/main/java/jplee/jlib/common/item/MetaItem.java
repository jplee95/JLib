package jplee.jlib.common.item;

import java.util.List;

import jplee.jlib.util.IMetaName;
import jplee.jlib.util.ModelRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class MetaItem extends BaseItem implements IMetaName {

	public MetaItem(String registryName) {
		super(registryName);
		
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public abstract String getSpecialName(ItemStack stack);
	@Override
	public abstract void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems);
	@Override
	public abstract void initModel(ModelRegister register);
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName(stack) + "_" + this.getSpecialName(stack);
	}
}
