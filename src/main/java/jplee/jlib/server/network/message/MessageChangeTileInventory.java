package jplee.jlib.server.network.message;

import io.netty.buffer.ByteBuf;
import jplee.jlib.server.network.MessageHandler;
import jplee.jlib.util.InventoryUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageChangeTileInventory implements IMessage {

	public static final int MODE_ADD = 0;
	public static final int MODE_REMOVE = 1;
	public static final int MODE_INSERT = 2;
	public static final int MODE_PULL = 3;
	
	private BlockPos blockPos;
	private int dimensionId;
	private int slotId;
	private int min;
	private int max;
	private ItemStack stack;
	private int mode;
	
	public MessageChangeTileInventory() { }

	public MessageChangeTileInventory(BlockPos pos, int dimensionId, int slotId, int min, int max, ItemStack stack, int mode) { 
		this.blockPos = pos;
		this.dimensionId = dimensionId;
		this.slotId = slotId;
		this.stack = stack;
		this.mode = mode;
	}
	
	public MessageChangeTileInventory(int x, int y, int z, int dimensionId, int slotId, int min, int max, ItemStack stack, int mode) {
		this(new BlockPos(x, y, z), dimensionId, slotId, min, max, stack, mode);
	}
	
	public MessageChangeTileInventory(TileEntity tile, int slot, int min, int max, ItemStack stack, int mode) {
		this(tile.getPos(), tile.getWorld().provider.getDimension(), slot, min, max, stack, mode);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		int x = buf.readInt();
		int y = buf.readInt();
		int z = buf.readInt();
		blockPos = new BlockPos(x, y, z);
		dimensionId = buf.readInt();
		slotId = buf.readInt();
		min = buf.readInt();
		max = buf.readInt();
		mode = buf.readInt();
		stack = ByteBufUtils.readItemStack(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(blockPos.getX());
		buf.writeInt(blockPos.getY());
		buf.writeInt(blockPos.getZ());
		buf.writeInt(dimensionId);
		buf.writeInt(slotId);
		buf.writeInt(min);
		buf.writeInt(max);
		buf.writeInt(mode);
		ByteBufUtils.writeItemStack(buf, stack);
	}

	public static class Handler extends MessageHandler.Bidirectional<MessageChangeTileInventory> {

		@Override
		public IMessage handleServerMessage(final EntityPlayer player, final MessageChangeTileInventory message, MessageContext context) {
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					World world = player.getEntityWorld();
					if(world.provider.getDimension() == message.dimensionId) {
						TileEntity tile = world.getTileEntity(message.blockPos);
						if(tile instanceof IInventory) {
							IInventory inv = (IInventory)tile;
							if(message.mode == MODE_ADD) {
								inv.setInventorySlotContents(message.slotId, message.stack);
							} else if(message.mode == MODE_REMOVE) {
								inv.setInventorySlotContents(message.slotId, (ItemStack) null);
							} else if(message.mode == MODE_INSERT) {
								InventoryUtils.addItemTo(inv, message.min, message.max, message.stack);
							} else if(message.mode == MODE_PULL) {
								InventoryUtils.removeItemFrom(inv, message.min, message.max, message.stack);
							}
						}
					}
				}
			});
			return null;
		}

		@Override
		public IMessage handleClientMessage(final EntityPlayer player, final MessageChangeTileInventory message, MessageContext context) {
			player.getServer().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					for(WorldServer world: player.getServer().worldServers) {
						if(world.provider.getDimension() == message.dimensionId) {
							TileEntity tile = world.getTileEntity(message.blockPos);
							if(tile instanceof IInventory) {
								IInventory inv = (IInventory)tile;
								if(message.mode == MODE_ADD) {
									inv.setInventorySlotContents(message.slotId, message.stack);
								} else if(message.mode == MODE_REMOVE) {
									inv.setInventorySlotContents(message.slotId, (ItemStack) null);
								} else if(message.mode == MODE_INSERT) {
									InventoryUtils.addItemTo(inv, message.min, message.max, message.stack);
								} else if(message.mode == MODE_PULL) {
									InventoryUtils.removeItemFrom(inv, message.min, message.max, message.stack);
								}
							}
						}
					}
				}
			});
			return null;
		}
		
	}
	
}
