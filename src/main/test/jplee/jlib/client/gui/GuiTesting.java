package jplee.jlib.client.gui;

import java.util.Map;

import com.google.common.collect.Maps;

import jplee.jlib.client.gui.object.GuiButton;
import jplee.jlib.client.gui.object.GuiContainer;
import jplee.jlib.client.gui.object.GuiRadioButton;
import jplee.jlib.client.gui.object.GuiWindow;
import jplee.jlib.client.gui.object.MouseState;
import jplee.jlib.client.gui.object.RadioGroup;
import jplee.jlib.common.block.container.ContainerTesting;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.ContainerWorkbench;

public class GuiTesting extends BaseGui {

	private Map<GuiButton,GuiRadioButton> rbs;
	private GuiButton addButton;
	private RadioGroup rGroup;
	
	private int spacer = 15;
	
	public GuiTesting(GuiScreen lastGui) {
		super(new ContainerTesting(), lastGui);
		
//		GuiContainer container = new GuiContainer(0, 0, 100, 100);
//		container.setScaleable(true);
//		this.add(container);

		this.add(new GuiButton(50, 50, 44, 13));
		
		GuiWindow window = new GuiWindow(0, 0, 100, 100);
		window.setScaleable(true);
		GuiButton bt = new GuiButton(100, 50, 44, 13);
		bt.setLabel("HELLO");
		bt.attachMouseEvent(MouseState.RELEASE, (gui, x, y, button) -> {
			this.sendChatMessage("HELLO");
		});
		window.add(bt);
		window.add(new GuiRadioButton(150, 150));
		this.add(window);
	}

	private void listing() {
		rbs = Maps.newLinkedHashMap();
		addButton = new GuiButton(-15, 0, 11, 11);
		rGroup = new RadioGroup();
		
		addButton.setLabel("+");
		addButton.setLabelColor(0x00cc00);
		addButton.setLabelHoverColor(0x00ee00);
		addButton.attachMouseEvent(MouseState.RELEASE, (gui, x, y, button) -> {
			GuiRadioButton rb = new GuiRadioButton(0, rbs.size() * spacer);
			rb.setLabel("" + (int)(Math.random() * 100));
			gui.add(rb);
			rGroup.addRadioButton(rb);
			
			GuiButton bt = new GuiButton(-15, rbs.size() * spacer - 2, 11, 11);
			bt.setLabel("-");
			bt.setLabelColor(0xcc0000);
			bt.setLabelHoverColor(0xee0000);
			bt.attachMouseEvent(MouseState.RELEASE, (guiB, xB, yB, buttonB) -> {
				boolean found = false;
				
				Map<GuiButton,GuiRadioButton> tempRbs = Maps.newLinkedHashMap(rbs);
				for(GuiButton b : tempRbs.keySet()) {
					if(!found && b.hashCode() == bt.hashCode()) {
						found = true;
						rGroup.removeRadioButton(rbs.get(b));
						guiB.remove(rbs.get(b));
						guiB.remove(b);
						rbs.remove(b);
						
					} else if(found) {
						rbs.get(b).y -= spacer;
						b.y -= spacer;
					}
				}
				addButton.y -= spacer;
			});
			gui.add(bt);
			rbs.put(bt,rb);
			
			addButton.y += spacer;
		});
		
		this.add(addButton);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.drawString(this.fontRendererObj, String.format("X:%4d Y:%4d", mouseX, mouseY), -this.guiLeft + 10, -this.guiTop + 10, 0xffffff);
	}

}
