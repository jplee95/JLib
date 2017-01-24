package jplee.jlib.common.item;

import jplee.jlib.util.IModelProvider;
import jplee.jlib.util.ModelRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaseItem extends Item implements IModelProvider {

	public BaseItem(String registryName) {
		this.setUnlocalizedName(registryName);
		this.setRegistryName(registryName);
	}

	public BaseItem setDamageable(int maxDamage) {
		this.setMaxDamage(maxDamage);
		this.setMaxStackSize(1);
		return this;
	}
	
	@Override
	public BaseItem setCreativeTab(CreativeTabs tab) {
		return (BaseItem) super.setCreativeTab(tab);
	}
	
	@Override
	public BaseItem setMaxStackSize(int maxStackSize) {
		return (BaseItem) super.setMaxStackSize(maxStackSize);
	}
	
	@Override
	public BaseItem setNoRepair() {
		return (BaseItem) super.setNoRepair();
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void initModel(ModelRegister register) {
		register.registerItemModel(this, this.getRegistryName().toString());
    }
}
