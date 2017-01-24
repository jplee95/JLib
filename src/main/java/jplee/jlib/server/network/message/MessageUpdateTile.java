package jplee.jlib.server.network.message;

import io.netty.buffer.ByteBuf;
import jplee.jlib.server.network.MessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageUpdateTile implements IMessage {

	private BlockPos blockPos;
	private int dimensionId;
	private NBTTagCompound compound;
	
	public MessageUpdateTile() { }
	
	public MessageUpdateTile(BlockPos pos, int dimensionId, NBTTagCompound compound) {
		this.blockPos = pos;
		this.dimensionId = dimensionId;
		this.compound = compound;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		int x = buf.readInt();
		int y = buf.readInt();
		int z = buf.readInt();
		blockPos = new BlockPos(x, y, z);
		dimensionId = buf.readInt();
		compound = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(blockPos.getX());
		buf.writeInt(blockPos.getY());
		buf.writeInt(blockPos.getZ());
		buf.writeInt(dimensionId);
		ByteBufUtils.writeTag(buf, compound);
	}
	
	public static class Handler extends MessageHandler.Bidirectional<MessageUpdateTile> {

		@Override
		public IMessage handleClientMessage(EntityPlayer player, final MessageUpdateTile message, MessageContext context) {
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override public void run() {
					Minecraft mc = Minecraft.getMinecraft();
					TileEntity tile = mc.theWorld.getTileEntity(message.blockPos);
					tile.readFromNBT(message.compound);
				}
			});
			return null;
		}

		@Override
		public IMessage handleServerMessage(final EntityPlayer player, final MessageUpdateTile message, MessageContext context) {
			player.getServer().addScheduledTask(new Runnable() {
				@Override public void run() {
					MinecraftServer mcs = player.getServer();
					TileEntity tile = mcs.worldServers[message.dimensionId].getTileEntity(message.blockPos);
					tile.readFromNBT(message.compound);
				}
			});
			return null;
		}
	}

}
