package jplee.jlib.server.network.message;


import io.netty.buffer.ByteBuf;
import jplee.jlib.server.network.MessageHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class MessageBytes implements IMessage {

	private byte[] bytes;
	
	public MessageBytes() { }
	
	public MessageBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		bytes = buf.array();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBytes(bytes);
	}

	public static class Handler extends MessageHandler.Bidirectional<MessageBytes> {

		@Override
		public IMessage handleClientMessage(EntityPlayer player, MessageBytes message, MessageContext context) {
			return null;
		}

		@Override
		public IMessage handleServerMessage(EntityPlayer player, MessageBytes message, MessageContext context) {
			return null;
		}
		
	}
}
