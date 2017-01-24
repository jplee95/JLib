package jplee.jlib.server.network.message;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import jplee.jlib.server.network.MessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSendChatMessage implements IMessage {

	private ITextComponent chatLine;
	boolean deletable;
	
	public MessageSendChatMessage() { }
	
	public MessageSendChatMessage(ITextComponent component, boolean deletable) {
		this.chatLine = component;
		this.deletable = deletable;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			PacketBuffer packetBuf = new PacketBuffer(buf);
			this.chatLine = packetBuf.readTextComponent();
			this.deletable = packetBuf.readBoolean();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packetBuf = new PacketBuffer(buf);
		packetBuf.writeTextComponent(this.chatLine);
		packetBuf.writeBoolean(this.deletable);
	}

	public static class Handler extends MessageHandler.Client<MessageSendChatMessage> {

		@Override
		public IMessage handleClientMessage(EntityPlayer player, MessageSendChatMessage message, MessageContext context) {
			Minecraft mc = Minecraft.getMinecraft();
			if(message.deletable) {
				mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(message.chatLine, 1);
			} else {
				mc.ingameGUI.getChatGUI().printChatMessage(message.chatLine);
			}
			return null;
		}

	}

}
