package jplee.jlib.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public final class InventoryUtils { // TODO: Think whether or not to keep this

	/**
	 *  Removes selected item amount form inventory
	 *  
	 *  @param inventory
	 *  @param min - minimum position of inventory
	 *  @param max - maximum position of inventory
	 *  @param stack - item to remove from inventory
	 *  @param amount - amount to remove from inventory
	 *  
	 *  @return true on success
	 **/
	public static boolean removeItemFrom(IInventory inventory, int min, int max, ItemStack stack, int amount) {
		if(stack != null && containsItemAmount(inventory, min, max, stack, amount) && amount > 0) {
			for(int i = min; i < max; i++) {
				if(inventory.isItemValidForSlot(i, stack)) {
					ItemStack slotStack = inventory.getStackInSlot(i);
					if(slotStack != null) {
						if(areItemsEqual(stack, slotStack, false, false, false)) {
							if(slotStack.stackSize > amount) {
								slotStack.stackSize -= amount;
								if(slotStack.stackSize <= 0) {
									inventory.setInventorySlotContents(i, (ItemStack) null);
								}
								return true;
							} else {
								amount -= slotStack.stackSize;
								inventory.setInventorySlotContents(i, (ItemStack) null);
							}
						}
					}
				}
			}
		}
		return amount <= 0;
	}
	
	public static boolean removeItemFrom(IInventory inventory, int min, int max, ItemStack stack) {
		return removeItemFrom(inventory, min, max, stack, stack.stackSize);
	}
	
	public static boolean removeItemFrom(IInventory inventory, ItemStack stack, int amount) {
		return removeItemFrom(inventory, 0, inventory.getSizeInventory(), stack, amount);
	}

	public static boolean removeItemFrom(IInventory inventory, ItemStack stack) {
		return removeItemFrom(inventory, stack, stack.stackSize);
	}

	/**
	 *  Adds selected item amount to inventory
	 *  
	 *  @param inventory
	 *  @param min - minimum position of inventory
	 *  @param max - maximum position of inventory
	 *  @param stack - item to add to inventory
	 *  @param amount - amount to add to inventory
	 *  
	 *  @return true on success
	 **/
	public static boolean addItemTo(IInventory inventory, int min, int max, ItemStack stack, int amount) {
		int maxStackSize = inventory.getInventoryStackLimit() > stack.getMaxStackSize() ? stack.getMaxStackSize() : inventory.getInventoryStackLimit();
		if(stack != null && hasSpaceFor(inventory, min, max, stack, amount) && amount > 0) {
			for(int i = min; i < max; i++) {
				ItemStack stackSlot = inventory.getStackInSlot(i);
				if(inventory.isItemValidForSlot(i, stackSlot)) {
					if(stackSlot == null) {
						ItemStack stackOut = stack.copy();
						if(amount < maxStackSize) {
							stackOut.stackSize = amount;
						} else {
							stackOut.stackSize = maxStackSize;
						}
						amount -= maxStackSize;
						inventory.setInventorySlotContents(i, stackOut);
					} else if(stackSlot.isItemEqual(stack)) {
						int remaining = maxStackSize - stackSlot.stackSize;
						if(amount < remaining) {
							stackSlot.stackSize += amount;
							amount -= maxStackSize;
						} else {
							stackSlot.stackSize += remaining;
							amount -= remaining;
						}
					}
				}
				if(amount <= 0) {
					break;
				}
			}
		}
		return amount <= 0;
	}
	
	public static boolean addItemTo(IInventory inventory, int min, int max, ItemStack stack) {
		return addItemTo(inventory, min, max, stack, stack.stackSize);
	}
	
	public static boolean addItemTo(IInventory inventory, ItemStack stack, int amount) {
		return addItemTo(inventory, 0, inventory.getSizeInventory(), stack, amount);
	}
	
	public static boolean addItemTo(IInventory inventory, ItemStack stack) {
		return addItemTo(inventory, stack, stack.stackSize);
	}
	
	/**
	 *  Counts the amount of the specified item in the inventory
	 *  
	 *  @param inventory
	 *  @param min - minimum position of inventory
	 *  @param max - maximum position of inventory
	 *  @param stack - item to check for in inventory
	 *  
	 *  @return item amount
	 **/
	public static int getItemCount(IInventory inventory, int min, int max, ItemStack stack, boolean ignoreDamage, boolean ignoreNbt, boolean useOreDictionary) {
		int amount = 0;
		if(stack != null) {
			for(int i = min; i < max; i++) {
				ItemStack slotStack = inventory.getStackInSlot(i);
				if(areItemsEqual(stack, slotStack, ignoreDamage, ignoreNbt, useOreDictionary)) {
					amount += slotStack.stackSize;
				}
			}
		}
		return amount;
	}
	
	public static int getItemCount(IInventory inventory, int min, int max, ItemStack stack) { 
		return getItemCount(inventory, min, max, stack, false, false, false); 
	}
	
	public static int getItemCount(IInventory inventory, ItemStack stack, boolean ignoreDamage, boolean ignoreNbt, boolean useOreDictionary) { 
		return getItemCount(inventory, 0, inventory.getSizeInventory(), stack, ignoreDamage, ignoreNbt, useOreDictionary); 
	}
	
	public static int getItemCount(IInventory inventory, ItemStack stack) { 
		return getItemCount(inventory, stack, false, false, false);
	}
	
	/**
	 *  Checks for space in inventory for selected item
	 *  
	 *  @param inventory
	 *  @param min - minimum position of inventory
	 *  @param max - maximum position of inventory
	 *  @param stack - item to check for in inventory
	 *  @param amount - amount of item in inventory
	 *  
	 *  @return true on success
	 **/
	public static boolean hasSpaceFor(IInventory inventory, int min, int max, ItemStack stack, int amount) {
		int maxStackSize = inventory.getInventoryStackLimit() > stack.getMaxStackSize() ? stack.getMaxStackSize() : inventory.getInventoryStackLimit();
		if(stack != null && amount > 0) {
			for(int i = min; i < max; i++) {
				ItemStack stackSlot = inventory.getStackInSlot(i);
				if(stackSlot == null) {
					amount -= maxStackSize;
				} else if(areItemsEqual(stackSlot, stack, false, false, false)) {
					int remaining = maxStackSize - stackSlot.stackSize;
					amount -= remaining;
				}
				if(amount <= 0) {
					break;
				}
			}
		}
		return amount <= 0;
	}
	
	public static boolean hasSpaceFor(IInventory inventory, int min, int max, ItemStack stack) {
		return hasSpaceFor(inventory, min, max, stack, stack.stackSize);
	}
	
	public static boolean hasSpaceFor(IInventory inventory, ItemStack stack, int amount) {
		return hasSpaceFor(inventory, 0, inventory.getSizeInventory(), stack, amount);
	}

	public static boolean hasSpaceFor(IInventory inventory, ItemStack stack) {
		return hasSpaceFor(inventory, stack, stack.stackSize);
	}
	
	/**
	 *  Checks for open slots in inventory
	 *  
	 *  @param inventory
	 *  @param min - minimum position of inventory
	 *  @param max - maximum position of inventory
	 *  @param amount - amount of open slots in inventory
	 *  
	 *  @return true on success
	 **/
	public static boolean containsSpace(IInventory inventory, int min, int max, int amount) {
		for(int i = min; i < max; i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			if(stack == null) {
				amount--;
				if(amount <= 0) {
					break;
				}
			}
		}
		return amount <= 0;
	}
	
	public static boolean containsSpace(IInventory inventory, int amount) {
		return containsSpace(inventory, 0, inventory.getSizeInventory(), amount);
	}

	/**
	 *  Checks for the specific item amount in inventory
	 *  
	 *  @param inventory
	 *  @param min - minimum position of inventory
	 *  @param max - maximum position of inventory
	 *  @param stack - item to check for in inventory
	 *  @param amount - amount of item in inventory
	 *  
	 *  @return true on success
	 **/
	public static boolean containsItemAmount(IInventory inventory, int min, int max, ItemStack stack, int amount, boolean ignoreDamage, boolean ignoreNbt, boolean useOreDictionary) {
		int count = 0;
		if(stack != null && amount > 0) {
			for(int j = min; j < max; j++) {
				ItemStack slotStack = inventory.getStackInSlot(j);
				if(areItemsEqual(stack, slotStack, ignoreDamage, ignoreNbt, useOreDictionary)) {
					count += slotStack.stackSize;
				}
			}
		}
		return count >= amount;
	}

	public static boolean containsItemAmount(IInventory inventory, int min, int max, ItemStack stack, int amount) {
		return containsItemAmount(inventory, min, max, stack, amount, false, false, false);
	}
	
	public static boolean containsItemAmount(IInventory inventory, int min, int max, ItemStack stack, boolean ignoreDamage, boolean ignoreNbt, boolean useOreDictionary) {
		return containsItemAmount(inventory, min, max, stack, stack.stackSize, ignoreDamage, ignoreNbt, useOreDictionary);
	}

	public static boolean containsitemAmount(IInventory inventory, int min, int max, ItemStack stack) {
		return containsItemAmount(inventory, min, max, stack, false, false, false);
	}
	
	public static boolean containsItemAmount(IInventory inventory, ItemStack stack, int amount, boolean ignoreDamage, boolean ignoreNbt, boolean useOreDictionary) {
		return containsItemAmount(inventory, 0, inventory.getSizeInventory(), stack, amount, ignoreDamage, ignoreNbt, useOreDictionary);
	}

	public static boolean containsItemAmount(IInventory inventory, ItemStack stack, boolean ignoreDamage, boolean ignoreNbt, boolean useOreDictionary) {
		return containsItemAmount(inventory, stack, stack.stackSize, ignoreDamage, ignoreNbt, useOreDictionary);
	}
	
	public static boolean containsItemAmount(IInventory inventory, ItemStack stack, int amount) {
		return containsItemAmount(inventory, 0, inventory.getSizeInventory(), stack, amount, false, false, false);
	}
	
	public static boolean containsItemAmount(IInventory inventory, ItemStack stack) {
		return containsItemAmount(inventory, stack, false, false, false);
	}

	/**
	 * Checks for the specific item in inventory
	 *  
	 *  @param inventory
	 *  @param min - minimum position of inventory
	 *  @param max - maximum position of inventory
	 *  @param stack - item to check for in inventory
	 *  @param amount - amount of item in inventory
	 *  
	 *  @return true on success
	 **/
	public static boolean containsItem(IInventory inventory, int min, int max, ItemStack stack, boolean ignoreDamage, boolean ignoreNbt, boolean useOreDictionary) {
		return containsItemAmount(inventory, min, max, stack, 1, ignoreDamage, ignoreNbt, useOreDictionary);
	}
	
	public static boolean containsItem(IInventory inventory, int min, int max, ItemStack stack) {
		return containsItem(inventory, min, max, stack, false, false, false);
	}
	
	public static boolean containsItem(IInventory inventory, ItemStack stack, boolean ignoreDamage, boolean ignoreNbt, boolean useOreDictionary) {
		return containsItem(inventory, 0, inventory.getSizeInventory(), stack, ignoreDamage, ignoreNbt, useOreDictionary);
	}
	
	public static boolean containsItem(IInventory inventory, ItemStack stack) {
		return containsItem(inventory, stack, false, false, false);
	}
	
	public static boolean areItemsEqual(ItemStack stack1, ItemStack stack2, boolean ignoreDamage, boolean ignoreNbt, boolean useOreDictionary) {
		if(stack1 == null || stack2 == null) {
			return false;
		}
		if(stack1.getItem() == stack2.getItem()) {
			if(stack1.getItemDamage() == stack2.getItemDamage() || ignoreDamage) {
				if(ItemStack.areItemStackTagsEqual(stack1, stack2) || ignoreNbt) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean areItemsEqual(ItemStack stack1, ItemStack stack2) {
		return areItemsEqual(stack1, stack2, false, false, false);
	}
}
