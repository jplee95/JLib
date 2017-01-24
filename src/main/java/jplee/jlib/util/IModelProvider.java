package jplee.jlib.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IModelProvider {

	@SideOnly(Side.CLIENT)
	public void initModel(ModelRegister manager);

}
