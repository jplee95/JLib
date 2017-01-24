package jplee.jlib.common.block;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import jplee.jlib.util.IModelProvider;
import jplee.jlib.util.ModelRegister;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaseBlock extends Block implements IModelProvider {

	private Item dropedItem = null;
	private int itemAmountMax = 1;
	private int itemAmountMin = 1;
	
	public BaseBlock(Material materialIn, String registryName) {
		super(materialIn);
		this.setUnlocalizedName(registryName);
		this.setRegistryName(registryName);
	}

	@Override
	public BaseBlock setCreativeTab(CreativeTabs tab) {
		return (BaseBlock) super.setCreativeTab(tab);
	}
	
	@Override
	public BaseBlock setHardness(float hardness) {
		return (BaseBlock) super.setHardness(hardness);
	}
	
	@Override
	public BaseBlock setResistance(float resistance) {
		return (BaseBlock) super.setResistance(resistance);
	}
	
	/**
     * Sets or removes the tool and level required to harvest this block.
     *
     * @param toolClass Class
     * @param level Harvest level:
     *     Wood:    0
     *     Stone:   1
     *     Iron:    2
     *     Diamond: 3
     *     Gold:    0
     */
	public BaseBlock setHarvestability(String toolClass, int level) {
		super.setHarvestLevel(toolClass, level);
		return this;
	}
	
	@Override
	public BaseBlock setLightLevel(float value) {
		return (BaseBlock) super.setLightLevel(value);
	}
	
	@Override
	public BaseBlock setLightOpacity(int opacity) {
		return (BaseBlock) super.setLightOpacity(opacity);
	}
	
	@Override
	public BaseBlock setBlockUnbreakable() {
		return (BaseBlock) super.setBlockUnbreakable();
	}
	
	public BaseBlock setDropedItem(Item item, int min, int max) {
		this.dropedItem = item;
		this.itemAmountMax = max;
		this.itemAmountMin = min;
		return this;
	}
	
	public BaseBlock setDropedItem(Item item, int amount) {
		this.dropedItem = item;
		this.itemAmountMax = amount;
		this.itemAmountMin = amount;
		return this;
	}

	public BaseBlock setDropedItem(Item item) {
		this.dropedItem = item;
		this.itemAmountMax = 1;
		this.itemAmountMin = 1;
		return this;
	}
	
    public int quantityDropped(Random random) {
    	if(itemAmountMax > 0 && itemAmountMin > 0) {
	    	if(itemAmountMax == itemAmountMin) {
	    		return itemAmountMax;
	    	} else if(itemAmountMin > itemAmountMax) {
	    		return random.nextInt(itemAmountMin - (itemAmountMax - 1)) + itemAmountMax;
	    	} else if(itemAmountMin < itemAmountMax) {
	    		return random.nextInt(itemAmountMax - (itemAmountMin - 1)) + itemAmountMin;
	    	}
    	}
        return 1;
    }
    
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    	if(dropedItem != null) {
    		return dropedItem;
    	}
    	return super.getItemDropped(state, rand, fortune);
    }
    
    @Override
	@SideOnly(Side.CLIENT)
	public void initModel(ModelRegister register) {
    	register.registerBlockItemModelForMeta(this, 0, "inventory");
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
