package jplee.jlib.client.gui.object;

import jplee.jlib.client.gui.BaseGui;

@FunctionalInterface
public interface IGuiEvent {

	public void onGuiAction(BaseGui gui, int mouseX, int mouseY, int mouseButton);
}
