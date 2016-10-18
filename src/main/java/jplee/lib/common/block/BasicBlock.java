package jplee.lib.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BasicBlock extends Block {

	private Item dropedItem = null;
	private int itemAmountMax = 1;
	private int itemAmountMin = 1;
	
	public BasicBlock(Material materialIn, String registryName, ItemBlock itemBlock) {
		super(materialIn);
		this.setUnlocalizedName(registryName);
		this.setRegistryName(registryName);
		GameRegistry.register(this);
		GameRegistry.register(itemBlock, this.getRegistryName());
	}
	
	public BasicBlock(Material materialIn, String registryName) {
		super(materialIn);
		this.setUnlocalizedName(registryName);
		this.setRegistryName(registryName);
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), this.getRegistryName());
	}

	public BasicBlock setDropedItem(Item item, int min, int max) {
		this.dropedItem = item;
		this.itemAmountMax = max;
		this.itemAmountMin = min;
		return this;
	}
	
	public BasicBlock setDropedItem(Item item, int amount) {
		this.dropedItem = item;
		this.itemAmountMax = amount;
		this.itemAmountMin = amount;
		return this;
	}

	public BasicBlock setDropedItem(Item item) {
		this.dropedItem = item;
		this.itemAmountMax = 1;
		this.itemAmountMin = 1;
		return this;
	}
	
    public int quantityDropped(Random random) {
    	if(itemAmountMax > 0 && itemAmountMin > 0) {
	    	if(itemAmountMax == itemAmountMin) {
	    		return itemAmountMax;
	    	} else if(itemAmountMin > itemAmountMax) {
	    		return random.nextInt(itemAmountMin - (itemAmountMax - 1)) + itemAmountMax;
	    	} else if(itemAmountMin < itemAmountMax) {
	    		return random.nextInt(itemAmountMax - (itemAmountMin - 1)) + itemAmountMin;
	    	}
    	}
        return 1;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    	if(dropedItem != null) {
    		return dropedItem;
    	}
    	return super.getItemDropped(state, rand, fortune);
    }
    
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
	}
}
