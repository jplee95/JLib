package jplee.jlib.common.block.container.slot;

import jplee.jlib.common.block.container.ContainerCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class SlotResult extends Slot {

	private ContainerCrafting eventHandeler;
	private EntityPlayer player;
	private int amountCrafted;
	
	public SlotResult(IInventory inventoryIn, EntityPlayer player, ContainerCrafting container, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
		this.eventHandeler = container;
		this.player = player;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}
	
	@Override
	public ItemStack decrStackSize(int amount) {
		if(this.getHasStack())
			this.amountCrafted += this.getStack().stackSize;
		return super.decrStackSize(this.getStack().stackSize);
	}
	
	@Override
	protected void onCrafting(ItemStack stack, int amount) {
		this.amountCrafted += amount;
		this.onCrafting(stack);
	}
	
	@Override
	protected void onCrafting(ItemStack stack) {
		if (this.amountCrafted > 0)
	        stack.onCrafting(this.player.worldObj, this.player, this.amountCrafted);
	
	    this.amountCrafted = 0;
	
	    if (stack.getItem() == Item.getItemFromBlock(Blocks.CRAFTING_TABLE))
	        this.player.addStat(AchievementList.BUILD_WORK_BENCH);
	
	    if (stack.getItem() instanceof ItemPickaxe)
	        this.player.addStat(AchievementList.BUILD_PICKAXE);
	
	    if (stack.getItem() == Item.getItemFromBlock(Blocks.FURNACE))
	        this.player.addStat(AchievementList.BUILD_FURNACE);
	
	    if (stack.getItem() instanceof ItemHoe)
	        this.player.addStat(AchievementList.BUILD_HOE);
	
	    if (stack.getItem() == Items.BREAD)
	        this.player.addStat(AchievementList.MAKE_BREAD);
	
	    if (stack.getItem() == Items.CAKE)
	        this.player.addStat(AchievementList.BAKE_CAKE);
	
	    if (stack.getItem() instanceof ItemPickaxe && ((ItemPickaxe)stack.getItem()).getToolMaterial() != Item.ToolMaterial.WOOD)
	        this.player.addStat(AchievementList.BUILD_BETTER_PICKAXE);
	
	    if (stack.getItem() instanceof ItemSword)
	        this.player.addStat(AchievementList.BUILD_SWORD);
	
	    if (stack.getItem() == Item.getItemFromBlock(Blocks.ENCHANTING_TABLE))
	        this.player.addStat(AchievementList.ENCHANTMENTS);
	
	    if (stack.getItem() == Item.getItemFromBlock(Blocks.BOOKSHELF))
	        this.player.addStat(AchievementList.BOOKCASE);
	}
	
	@Override
	public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
		FMLCommonHandler.instance().firePlayerCraftingEvent(playerIn, stack, this.inventory);
		this.onCrafting(stack);
		
		eventHandeler.onSuccessfulCraft();
	}
}