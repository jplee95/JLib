package jplee.jlib.server.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class MessageHandler<T extends IMessage> implements IMessageHandler<T, IMessage> {
	
	public static enum MessageType {
		CLIENT,
		SERVER,
		BIDIRECTIONAL;
	}
	
	public static abstract class Client<T extends IMessage> extends MessageHandler<T> {
		@Override
		public IMessage handleServerMessage(EntityPlayer player, T message, MessageContext context) {
			return null;
		}
	}
	
	public static abstract class Server<T extends IMessage> extends MessageHandler<T> {
		@Override
		public IMessage handleClientMessage(EntityPlayer player, T message, MessageContext context) {
			return null;
		}
	}
	
	public static abstract class Bidirectional<T extends IMessage> extends MessageHandler<T> {
		
	}
	
	public abstract IMessage handleClientMessage(final EntityPlayer player, final T message, final MessageContext context);
	public abstract IMessage handleServerMessage(final EntityPlayer player, final T message, final MessageContext context);
	
	@SideOnly(Side.CLIENT)
	private IMessage runHandleClient(T message, MessageContext context) {
		return this.handleClientMessage(getPlayer(context), message, context);
	}
	
	@Override
	public final IMessage onMessage(T message, MessageContext context) {
		if(context.side.isClient()) {
			return this.runHandleClient(message, context);
		} else {
			return this.handleServerMessage(getPlayer(context), message, context);
		}
	}

	private EntityPlayer getPlayer(MessageContext context) {
		EntityPlayer player = null;
		if(context.side.isClient()) player = Minecraft.getMinecraft().thePlayer;
		if(context.side.isServer()) player = context.getServerHandler().playerEntity;
		return player;
	}
}
