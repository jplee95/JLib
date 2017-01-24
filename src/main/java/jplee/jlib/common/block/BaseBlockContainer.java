package jplee.jlib.common.block;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import jplee.jlib.util.IModelProvider;
import jplee.jlib.util.ModelRegister;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BaseBlockContainer extends BlockContainer implements IModelProvider {

	public BaseBlockContainer(Material materialIn, String registryName) {
		super(materialIn);
		this.setUnlocalizedName(registryName);
		this.setRegistryName(registryName);
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof IInventory) {
			InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tile);
		}
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void initModel(ModelRegister register) {
    	register.registerItemModel(Item.getItemFromBlock(this), this.getRegistryName().toString());
	}
	
	@Nullable
	public static RayTraceResult raytraceMultiAABB(List<AxisAlignedBB> aabbs, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
        List<RayTraceResult> list = Lists.<RayTraceResult>newArrayList();

        for (AxisAlignedBB axisalignedbb : aabbs) {
            list.add(rayTrace2(pos, start, end, axisalignedbb));
        }

        RayTraceResult raytraceresult1 = null;
        double d1 = 0.0D;

        for (RayTraceResult raytraceresult : list)
        {
            if (raytraceresult != null)
            {
                double d0 = raytraceresult.hitVec.squareDistanceTo(end);

                if (d0 > d1)
                {
                    raytraceresult1 = raytraceresult;
                    d1 = d0;
                }
            }
        }

        return raytraceresult1;
    }

	@Nullable
	public static RayTraceResult rayTrace2(BlockPos pos, Vec3d start, Vec3d end, AxisAlignedBB boundingBox) {
	    Vec3d vec3d = start.subtract((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
	    Vec3d vec3d1 = end.subtract((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
	    RayTraceResult raytraceresult = boundingBox.calculateIntercept(vec3d, vec3d1);
	    return raytraceresult == null ? null : new RayTraceResult(raytraceresult.hitVec.addVector((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()), raytraceresult.sideHit, pos);
	}

}