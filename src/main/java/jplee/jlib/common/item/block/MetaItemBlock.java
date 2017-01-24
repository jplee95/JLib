package jplee.jlib.common.item.block;

import java.util.List;

import jplee.jlib.util.IMetaName;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public abstract class MetaItemBlock extends ItemBlock {

	public MetaItemBlock(Block block) {
		super(block);
		
		if(!(block instanceof IMetaName)) 
			throw new IllegalArgumentException("The given Block " + block.getRegistryName() + " is not an instance of " + IMetaName.class.getName() + "!");
		
		this.setRegistryName(block.getRegistryName());
		
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName(stack) + "_" + ((IMetaName)this.block).getSpecialName(stack);
	}
	
	@Override
	public abstract void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems);
	
}
