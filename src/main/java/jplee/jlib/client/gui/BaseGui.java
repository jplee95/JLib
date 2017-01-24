package jplee.jlib.client.gui;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.google.common.collect.Lists;

import jplee.jlib.client.gui.object.GuiObject;
import jplee.jlib.client.gui.object.MouseState;
import jplee.jlib.common.block.container.BaseContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;

public abstract class BaseGui extends GuiContainer {

	protected static Comparator<GuiObject> sortObject = new Comparator<GuiObject>() {
		@Override
		public int compare(GuiObject o1, GuiObject o2) {
			return o1.depth < o2.depth ? -1 : o1.depth > o2.depth ? 1 : 0;
		}
	};
	
	private List<GuiObject> guiObjects;
	
	protected GuiObject hoveredObject;
	private GuiObject heldObject;
	private GuiObject lastSelected;
	
	protected BaseContainer container;
	protected GuiScreen lastScreen;
	
	private int touchValue;
	private int eventButton;
	private long lastMouseEvent;
	
	public BaseGui(BaseContainer container, @Nullable GuiScreen lastGui) {
		super(container);
		
		this.container = (BaseContainer) this.inventorySlots;
		guiObjects = Lists.newArrayList();
	}
	
	public BaseGui(BaseContainer container) {
		this(container, null);
	}
	
	protected void add(GuiObject component) {
		if(component != null) {
			component.attatchGui(this);
			this.guiObjects.add(component);
			this.guiObjects.sort(sortObject);
		}
	}
	
	protected void remove(GuiObject component) {
		if(component != null) {
			this.guiObjects.remove(component);
		}
	}
	
	public int getXShift() {
		return this.guiLeft;
	}
	
	public int getYShift() {
		return this.guiTop;
	}
	
	public GuiObject getObjectUnderMouse() {
		if(this.heldObject != null)
			return this.heldObject.getObject();
		return this.hoveredObject.getObject();
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
        int relativeX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int relativeY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
		for(GuiObject object : guiObjects) {
			if(object.isEnabled()) {
				object.update();
			}
		}
		if(this.hoveredObject != null) {
	        int mouseButton = Mouse.getEventButton();
	        long lastEvent = Minecraft.getSystemTime() - this.lastMouseEvent;
        	try {
				this.hoveredObject.handleMouseInput(relativeX, relativeY, mouseButton, lastEvent, MouseState.HOVER);
			} catch(IOException e) {
				e.printStackTrace();
			}
        }
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)) {
			if(lastScreen != null) {
				this.mc.displayGuiScreen(this.lastScreen);
			} else {
				this.mc.thePlayer.closeScreen();
			}
        } else {
        	super.keyTyped(typedChar, keyCode);
        }
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
	}
	
	@Override protected abstract void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY);
	@Override protected abstract void drawGuiContainerForegroundLayer(int mouseX, int mouseY);

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);

		this.hoveredObject = null;
		
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
		for(GuiObject object : guiObjects) {
			if(!object.isHidden()) {
				object.drawBackground(mouseX, mouseY, partialTicks);
		        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				object.drawForeground(mouseX, mouseY);
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			}
			if(object.isOver(mouseX, mouseY)) {
				if(hoveredObject == null) {
					this.hoveredObject = object.getObject();
				} else if(object.getDepth() > hoveredObject.getDepth()) {
					this.hoveredObject = object.getObject();
				}
			}
		}
		System.out.println(hoveredObject);

        GlStateManager.enableRescaleNormal();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
	}
	
	@Override
	public void handleMouseInput() throws IOException {
        int relativeX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int relativeY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        int mouseButton = Mouse.getEventButton();
        long lastEvent = Minecraft.getSystemTime() - this.lastMouseEvent;

        
        if(Mouse.getEventButtonState() && hoveredObject != null) {
            if (this.mc.gameSettings.touchscreen && this.touchValue++ > 0) {
                return;
            }
            this.eventButton = mouseButton;
            this.lastMouseEvent = Minecraft.getSystemTime();
            if(this.hoveredObject != null) {
            	this.lastSelected = hoveredObject;
            	this.hoveredObject.handleMouseInput(relativeX, relativeY, this.eventButton, lastEvent, MouseState.CLICK);
            	this.heldObject = this.hoveredObject;
            }
        	if(hoveredObject == null)
        		lastSelected = null;
        } else if(mouseButton != -1 && (hoveredObject != null || heldObject != null)) {
            if (this.mc.gameSettings.touchscreen && --this.touchValue > 0) {
                return;
            }

            this.eventButton = -1;
            if(this.hoveredObject != null)
            	this.hoveredObject.handleMouseInput(relativeX, relativeY, this.eventButton, lastEvent, MouseState.RELEASE);
        	if(this.heldObject != null && this.hoveredObject == null)
        		this.heldObject.handleMouseInput(relativeX, relativeY, this.eventButton, lastEvent, MouseState.RELEASE);
    		this.heldObject = null;
        } else if(this.eventButton != -1 && this.lastMouseEvent > 0L) {
            if(this.heldObject != null)
            	this.heldObject.handleMouseInput(relativeX, relativeY, this.eventButton, lastEvent, MouseState.DRAG);
        } else {
        	super.handleMouseInput();
        }
	}
	
	@Override
	public void handleKeyboardInput() throws IOException {
        char c0 = Keyboard.getEventCharacter();
        int keyCode = Keyboard.getEventKey();

        if (keyCode == 0 && c0 >= 32 || Keyboard.getEventKeyState()) {
            this.keyTyped(c0, keyCode);
    		if(this.lastSelected != null) {
    			this.lastSelected.handleKeyboardInput(c0, keyCode, 0);
            }
        }
        this.mc.dispatchKeypresses();
	}
	
	@Override
	public void setWorldAndResolution(Minecraft mc, int width, int height) {
		super.setWorldAndResolution(mc, width, height);
		for(GuiObject object : guiObjects) {
			object.setMinecraft(mc);
		}
	}
}
