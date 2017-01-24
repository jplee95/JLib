package jplee.jlib.client.gui.object;

import net.minecraft.client.Minecraft;

public interface IGuiContainer {

	public GuiObject getGuiContainer();
	public int xShift();
	public int yShift();
	
	public Minecraft getMinecraft();
}
