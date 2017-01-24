package jplee.jlib.server.network.message;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import jplee.jlib.server.network.MessageHandler;
import jplee.jlib.util.InventoryUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageChangePlayerInventory implements IMessage {

	public static final int MODE_ADD = 0;
	public static final int MODE_REMOVE = 1;
	public static final int MODE_INSERT = 2;
	public static final int MODE_PULL = 3;
	public static final int MODE_SET_HAND = 4;
	
	private int mode;
	private String playerUUID;
	private int slot;
	private ItemStack stack;
	
	public MessageChangePlayerInventory() { }
	
	public MessageChangePlayerInventory(String playerUUID, int slot, ItemStack stack, int mode) {
		this.playerUUID = playerUUID;
		this.slot = slot;
		this.stack = stack;
		this.mode = mode;
	}
	
	public MessageChangePlayerInventory(EntityPlayer player, int slot, ItemStack stack, int mode) {
		this(player.getUniqueID().toString(), slot, stack, mode);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		playerUUID = ByteBufUtils.readUTF8String(buf);
		slot = buf.readInt();
		stack = ByteBufUtils.readItemStack(buf);
		mode = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, playerUUID);
		buf.writeInt(slot);
		ByteBufUtils.writeItemStack(buf, stack);
		buf.writeInt(mode);
	}

	public static class Handler extends MessageHandler.Bidirectional<MessageChangePlayerInventory> {

		@Override
		public IMessage handleClientMessage(final EntityPlayer player, final MessageChangePlayerInventory message, MessageContext context) {
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					EntityPlayer p = player.getServer().getPlayerList().getPlayerByUUID(UUID.fromString(message.playerUUID));
					if(message.mode == MODE_ADD) {
						p.inventory.setInventorySlotContents(message.slot, message.stack);
					} else if(message.mode == MODE_REMOVE) {
						p.inventory.setInventorySlotContents(message.slot, message.stack);
					} else if(message.mode == MODE_INSERT) {
						p.inventory.addItemStackToInventory(message.stack);
					} else if(message.mode == MODE_PULL) {
						InventoryUtils.removeItemFrom(p.inventory, message.stack);
					} else if(message.mode == MODE_SET_HAND) {
						p.inventory.setItemStack(message.stack);
					}
				}
			});
			return null;
		}

		@Override
		public IMessage handleServerMessage(final EntityPlayer player, final MessageChangePlayerInventory message, MessageContext context) {
			player.getServer().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					EntityPlayer p = player.getServer().getPlayerList().getPlayerByUUID(UUID.fromString(message.playerUUID));
					if(message.mode == MODE_ADD) {
						p.inventory.setInventorySlotContents(message.slot, message.stack);
					} else if(message.mode == MODE_REMOVE) {
						p.inventory.setInventorySlotContents(message.slot, message.stack);
					} else if(message.mode == MODE_INSERT) {
						p.inventory.addItemStackToInventory(message.stack);
					} else if(message.mode == MODE_PULL) {
						InventoryUtils.removeItemFrom(p.inventory, message.stack);
					} else if(message.mode == MODE_SET_HAND) {
						p.inventory.setItemStack(message.stack);
					}
				}
			});
			return null;
		}
		
	}
}
