package jplee.jlib.client.gui.object;

import java.util.Set;

import org.lwjgl.input.Mouse;

import com.google.common.collect.Sets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;

public class GuiWindow extends GuiObject implements IGuiContainer {

	private static final int FINAL_MIN_WIDTH = 100;
	private static final int FINAL_MIN_HEIGHT = 40;
	private static final int ANIMATE_TIME = 50;
	
	private static final String dots = "...";
	
	private Set<GuiObject> guiObjects;
	
	protected GuiObject hoveredObject;
	protected GuiObject lastSelected;
	private GuiObject heldObject;
	
	private int showDragable = 100;
	
	private int minWidth;
	private int minHeight;
	private int maxWidth;
	private int maxHeight;
	
	private String title;
	private int clickedX;
	private int clickedY;
	private boolean beingClicked;
	private int scaling;
	private boolean scaleable;
	
	public GuiWindow(int x, int y, int width, int height) {
		super(x, y, width >= FINAL_MIN_WIDTH ? width : FINAL_MIN_WIDTH, height >= FINAL_MIN_HEIGHT ? height : FINAL_MIN_HEIGHT);
		guiObjects = Sets.newHashSet();
		
		this.zLevel = 1;
		this.depth = 10;
		
		title = "";
		this.scaleable = false;
		this.clickedX = 0;
		this.clickedY = 0;
		this.scaling = 0;
		
		this.minWidth = FINAL_MIN_WIDTH;
		this.minHeight = FINAL_MIN_HEIGHT;
		this.maxWidth = -1;
		this.maxHeight = -1;
	}

	@Override
	public GuiObject getGuiContainer() {
		return this;
	}

	@Override
	public int xShift() {
		return this.x + this.getXShift();
	}

	@Override
	public int yShift() {
		return this.y + this.getYShift();
	}

	@Override
	public Minecraft getMinecraft() {
		return this.mc;
	}
	
	public boolean isScaleable() {
		return this.scaleable;
	}
	
	public void setScaleable(boolean scaleable) {
		this.scaleable = scaleable;
	}

	public void add(GuiObject component) {
		if(component != null) {
			component.attatchGui(this.attactedGui);
			component.attachContainer(this);
			this.guiObjects.add(component);
			
		}
	}
	
	public void remove(GuiObject component) {
		if(component != null) {
			this.guiObjects.remove(component);
		}
	}
	
	@Override
	protected void onMouseClick(int mouseX, int mouseY, int mouseButton) {
		if(hoveredObject == null || overDrag(mouseX, mouseY)) {
			super.onMouseClick(mouseX, mouseY, mouseButton);
			this.beingClicked = true;
			this.clickedX = mouseX;
			this.clickedY = mouseY;
		} else {
//			hoveredObject.onMouseClick(mouseX, mouseY, mouseButton);
			heldObject = hoveredObject;
			
		}
	}
	
	protected void onMouseRelease(int mouseX, int mouseY, int mouseButton) {
		if(hoveredObject == null || overDrag(mouseX, mouseY)) {
			super.onMouseRelease(mouseX, mouseY, mouseButton);
			this.beingClicked = false;
			this.scaling = 0;
		} else {
//			hoveredObject.onMouseRelease(mouseX, mouseY, mouseButton);
			heldObject = null;
		}
	};
	
	private boolean overDrag(int x, int y) {
		return isPointInRegion(this.x + this.width - 7, this.y + this.height + 3, 7, 7, x, y);
	}
	
	private boolean overBottom(int x, int y) {
		return isPointInRegion(this.x, this.y + height + 9, this.width - 7, 2, x, y);
	}
	
	private boolean overBar(int x, int y) {
		return isPointInRegion(this.x, this.y, this.width + 3, 9, x, y);
	}
	
	private boolean overRight(int x, int y) {
		return isPointInRegion(this.x + this.width, this.y, 2, this.height + 2 - 7, x, y);
	}
	
	@Override
	protected void onMouseDrag(int mouseX, int mouseY, int mouseButton, long timeSinceLastClick) {
		boolean overDrag = overDrag(clickedX, clickedY);
		if(heldObject == null || overDrag) {
			super.onMouseDrag(mouseX, mouseY, mouseButton, timeSinceLastClick);
			boolean overRight = overRight(clickedX, clickedY);
			boolean overBottom = overBottom(clickedX, clickedY);
			boolean overBar = overBar(clickedX, clickedY);
			if((scaling != 0 || overDrag || overRight || overBottom) && this.isScaleable()) {
				scaling = 0;
				if(overRight || overDrag)
					scaling += 1;
				if(overBottom || overDrag)
					scaling += 2;
				
				if((scaling & 1) == 1) {
					int shiftedX = mouseX - clickedX;
					this.width += shiftedX;
					clickedX += shiftedX;
					if(this.width < this.minWidth) {
						this.clickedX += this.minWidth - this.width;
						this.width = this.minWidth;
					}
					if(this.maxWidth != -1 && this.width > this.maxWidth) {
						this.clickedX += this.maxWidth - this.width;
						this.width = this.maxWidth;
					}
				}
				if((scaling >> 1 & 1) == 1) {
					int shiftedY = mouseY - clickedY;
					this.height += shiftedY;
					clickedY += shiftedY;
					if(this.height < this.minHeight) {
						this.clickedY += this.minHeight - this.height;
						this.height = this.minHeight;
					}
					if(this.maxHeight != -1 && this.height > this.maxHeight) {
						this.clickedY += this.maxHeight - this.height;
						this.height = this.maxHeight;
					}
				}
			} else if(overBar) {
				
				int shiftedX = mouseX - clickedX;
				this.x += shiftedX;
				clickedX += shiftedX;
	//			if(this.x + this.getXShift() < 0) {
	//				this.clickedX += this.x + this.getXShift();
	//				this.x = 0 - this.getXShift();
	//			}
				int shiftedY = mouseY - clickedY;
				this.y += shiftedY;
				clickedY += shiftedY;
	//			if(this.y + this.getYShift() < 0) {
	//				this.clickedY += this.y + this.getYShift();
	//				this.y = 0 - this.getXShift();
	//			}
			} else {
				if(this.isOver(mouseX, mouseY))
					this.beingClicked = true;
				else
					this.beingClicked = false;
			}
		} else {
//			heldObject.onMouseDrag(mouseX, mouseY, mouseButton, timeSinceLastClick);
		}
	}

	@Override
	protected void onMouseHover(int mouseX, int mouseY) {
		if(hoveredObject == null || overDrag(mouseX, mouseY)) {
			super.onMouseHover(mouseX, mouseY);
		} else {
			hoveredObject.onMouseHover(mouseX, mouseY);
		}
	}
	
	@Override
	protected void onKeyTyped(char typedChar, int keyCode) {
		if(lastSelected == null) {
			super.onKeyTyped(typedChar, keyCode);
		} else {
			lastSelected.onKeyTyped(typedChar, keyCode);
		}
	}
	
	@Override
	public void update() {
        int mouseX = Mouse.getEventX() * this.attactedGui.width / this.mc.displayWidth;
        int mouseY = this.attactedGui.height - Mouse.getEventY() * this.attactedGui.height / this.mc.displayHeight - 1;
        for(GuiObject obj : guiObjects) {
        	obj.update();
        }
		if(overDrag(mouseX, mouseY) && this.showDragable < ANIMATE_TIME)
			this.showDragable = Math.min(this.showDragable + 5, ANIMATE_TIME);
		else if(this.showDragable != -1) {
			this.showDragable--;
		}
	}
	
	@Override
	public void drawBackground(int mouseX, int mouseY, float partialTicks) {
		this.mc.getTextureManager().bindTexture(DEFAULT_RESOURCE);

		int x = this.x + this.getXShift() - 1;
		int y = this.y + this.getYShift() - 1;
		if(this.width > 0 && this.height > 0) {
			this.drawTexturedBorderedModalRect(124, 42, 66, 7, 1, x, y, this.width + 2, 10);
			y += 10;
			this.drawTexturedBorderedModalRect(124, 48, 66, 66, 2, x, y, this.width + 2, this.height + 2);
			
			setClipBox(x + 2, y + 2, this.width - 2, this.height - 2);
			enableClipping();
			
			for(GuiObject object : this.guiObjects) {
				object.drawBackground(mouseX, mouseY, partialTicks);
				object.drawForeground(x + 2, y);
				
				hoveredObject = null;
				if(object.isOver(mouseX, mouseY)) {
					this.hoveredObject = object;
				}
			}
			disableClipping();
		}
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {
		int x = this.x + this.getXShift() - 1;
		int y = this.y + this.getYShift() - 1;

		this.title = "This is some rily long text to play with";
		int strlen = this.fontRender.getStringWidth(this.title);
		int dotLen = this.fontRender.getStringWidth(dots);
		if(strlen > this.width - 20 - this.fontRender.getStringWidth(dots)) {
			this.drawString(this.fontRender, this.fontRender.trimStringToWidth(this.title, this.width - 20 - dotLen) + dots, x + 2, y + 2, 0xffffff);
		} else 
			this.drawString(this.fontRender, this.title, x + 2, y + 2, 0xffffffff);
		
		y += 10;
		boolean overDrag = overDrag(mouseX, mouseY);
		int dragState = 7 * (this.isEnabled() ? (beingClicked && overDrag ? (overDrag ? 2 : 0) : 1) : 0) + (this.isEnabled() ? 0 : 65);

		this.mc.getTextureManager().bindTexture(DEFAULT_RESOURCE);
		if(this.scaleable && overDrag || showDragable != -1) {
			GlStateManager.enableAlpha();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.color(1.0f, 1.0f, 1.0f, Math.min(this.showDragable, 10) / 10.0f);
			this.drawTexturedModalRect(x + this.width - 7, y + this.height - 7, 181 + dragState, 114, 7, 7);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			GlStateManager.disableBlend();
			GlStateManager.disableAlpha();
		}
	}

	@Override
	public boolean isOver(int mouseX, int mouseY) {
		if(this.isHoverable() && !this.isHidden()) {
			return this.isPointInRegion(this.x, this.y, this.width, this.height + 6, mouseX, mouseY);
		}
		return false;
	}
	
	@Override
	public GuiObject getObject() {
		if(this.heldObject != null)
			return this.heldObject.getObject();
		if(this.hoveredObject != null)
			return this.hoveredObject.getObject();
		return this;
	}
}
