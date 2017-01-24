package jplee.jlib.server.network;

import jplee.jlib.server.network.MessageHandler.MessageType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public final class PacketHandler {

	private byte nextPacketId;
	private SimpleNetworkWrapper wrapper;
	private String channelId;
	
	public PacketHandler(String channelId) {
		this.wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(channelId);
		this.channelId = channelId;
		this.nextPacketId = 0;
	}
	
	public void addBasicMessages() {
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean registerPacket(Class<? extends IMessage> clazz, IMessageHandler handler, MessageType type) {
		if(this.nextPacketId == -1) {
			throw new IllegalStateException("Too many packets registerd for channel " + this.channelId);
		}
		if(type == MessageType.CLIENT || type == MessageType.BIDIRECTIONAL)
			this.wrapper.registerMessage(handler, clazz, this.nextPacketId, Side.CLIENT);
		if(type == MessageType.SERVER || type == MessageType.BIDIRECTIONAL)
			this.wrapper.registerMessage(handler, clazz, this.nextPacketId, Side.SERVER);

		this.nextPacketId++;
		return true;
	}
	
	public void sendToPlayer(IMessage message, EntityPlayerMP player) {
		if(player != null) {
			this.wrapper.sendTo(message, player);
		}
	}
	
	public void sendToAll(IMessage message) {
		this.wrapper.sendToAll(message);
	}
	
	public void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
		this.wrapper.sendToAllAround(message, point);
	}
	
	public void sendToAllAround(IMessage message, int dimension, BlockPos pos, double range) {
		this.sendToAllAround(message, dimension, pos.getX(), pos.getY(), pos.getZ(), range);
	}
	
	public void sendToAllAround(IMessage message, int dimension, double x, double y, double z, double range) {
		this.sendToAllAround(message, new NetworkRegistry.TargetPoint(dimension, x, y, z, range));
	}
	
	public void sendToAllAround(IMessage message, Entity entity, double range) {
		this.sendToAllAround(message, entity.worldObj.provider.getDimension(), entity.posX, entity.posY, entity.posZ, range);
	}
	
	public void sendToDimension(IMessage message, int dimensionId) {
		this.wrapper.sendToDimension(message, dimensionId);
	}
	
	public void sendToServer(IMessage message) {
		this.wrapper.sendToServer(message);
	}
}
