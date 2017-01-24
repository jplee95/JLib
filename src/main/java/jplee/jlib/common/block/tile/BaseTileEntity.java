package jplee.jlib.common.block.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class BaseTileEntity extends TileEntity {
	
	public BaseTileEntity() {
	}
	
	/** Gets the current stored NBT tags on this tile **/
	public void getNBT(NBTTagCompound compound) { 
	}

	/** Sets the current stored NBT tags on this tile **/
	public void setNBT(NBTTagCompound compound) {
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound compound = new NBTTagCompound();
		this.writeToNBT(compound);
		if(!compound.hasNoTags() || !this.getTileData().hasNoTags()) {
			compound.setTag("ForgeData", this.getTileData());
			return new SPacketUpdateTileEntity(this.getPos(), this.getBlockMetadata(), compound);
		}
		return null;
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
	}
	
	@Override /** Do not over write these */
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.getNBT(compound);
	}
	
	@Override /** Do not over write these */
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		this.setNBT(compound);
		return compound;
	}
	
	@SuppressWarnings("unused")
	protected void updateSurroundingPlayers() {
		if(!this.worldObj.isRemote) {
			BlockPos pos = this.getPos();
			int worldId = this.getWorld().provider.getDimension();
			SPacketUpdateTileEntity t = (SPacketUpdateTileEntity) this.getUpdatePacket();
//			MerchantsBench.packetHandler.sendToAllAround(new MessageUpdateTile(pos, worldId, t.getNbtCompound()), pos.getX(), pos.getY(), pos.getZ(), worldId, 200);
		}
	}
}
