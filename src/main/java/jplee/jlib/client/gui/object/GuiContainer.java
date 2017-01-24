package jplee.jlib.client.gui.object;

import java.util.Set;

import com.google.common.collect.Sets;

import jplee.jlib.JLib;

public class GuiContainer extends GuiObject {

	private static final int FINAL_MIN_WIDTH = 100;
	private static final int FINAL_MIN_HEIGHT = 40;

	private Set<GuiObject> guiObjects;
	
	protected GuiObject hoveredObject;
	private GuiObject heldObject;
	
	private int minWidth;
	private int minHeight;
	private int maxWidth;
	private int maxHeight;
	
	private int clickedX;
	private int clickedY;
	private boolean beingClicked;
	private int scaling;
	private boolean scaleable;
	
	public GuiContainer(int x, int y, int width, int height) {
		super(x, y, width >= FINAL_MIN_WIDTH ? width : FINAL_MIN_WIDTH, height >= FINAL_MIN_HEIGHT ? height : FINAL_MIN_HEIGHT);
		guiObjects = Sets.newHashSet();
		
		this.scaleable = false;
		this.clickedX = 0;
		this.clickedY = 0;
		this.scaling = 0;
		
		this.minWidth = FINAL_MIN_WIDTH;
		this.minHeight = FINAL_MIN_HEIGHT;
		this.maxWidth = -1;
		this.maxHeight = -1;
	}
	
	public boolean isScaleable() {
		return this.scaleable;
	}
	
	public void setScaleable(boolean scaleable) {
		this.scaleable = scaleable;
	}

	protected void add(GuiObject component) {
		if(component != null) {
			component.attatchGui(this.attactedGui);
			this.guiObjects.add(component);
			
		}
	}
	
	protected void remove(GuiObject component) {
		if(component != null) {
			this.guiObjects.remove(component);
		}
	}
	
	@Override
	protected void onMouseClick(int mouseX, int mouseY, int mouseButton) {
		super.onMouseClick(mouseX, mouseY, mouseButton);
		this.beingClicked = true;
		this.clickedX = mouseX;
		this.clickedY = mouseY;
	}
	
	protected void onMouseRelease(int mouseX, int mouseY, int mouseButton) {
		super.onMouseRelease(mouseX, mouseY, mouseButton);
		this.beingClicked = false;
		this.scaling = 0;
	};
	
	@Override
	protected void onMouseDrag(int mouseX, int mouseY, int mouseButton, long timeSinceLastClick) {
		super.onMouseDrag(mouseX, mouseY, mouseButton, timeSinceLastClick);
		boolean overDrag = isPointInRegion(this.width - 8, this.height - 7, 7, 7, clickedX, clickedY);
		boolean overRight = isPointInRegion(this.x + this.width - 3, this.y, 2, this.height - 7, clickedX, clickedY);
		boolean overBottom = isPointInRegion(this.x, this.y + height - 3, this.width - 7, 2, clickedX, clickedY);
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
		} else {
			if(this.isOver(mouseX, mouseY))
				this.beingClicked = true;
			else
				this.beingClicked = false;
		}
	}
	
	@Override
	public void drawBackground(int mouseX, int mouseY, float partialTicks) {
		this.mc.getTextureManager().bindTexture(DEFAULT_RESOURCE);

		int x = this.x + this.getXShift() - 1;
		int y = this.y + this.getYShift() - 1;
		
		boolean overDrag = isPointInRegion(this.width - 8, this.height - 7, 7, 7, mouseX, mouseY);
		int dragState = 7 * (this.isEnabled() ? beingClicked && overDrag ? (overDrag ? 1 : 0) : 2 : 0) + (this.isEnabled() ? 0 : 65);
		
		if(this.width > 0 && this.height > 0) {
			int shift = 65 * (this.isEnabled() ?  0 : 1);
			this.drawTexturedBorderedModalRect(125 + shift, 49, 64, 64, 1, x, y, width, height);
			if(this.scaleable)
				this.drawTexturedModalRect(x + this.width - 8, y + this.height - 7, 181 + dragState, 114, 7, 7);
		}
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {
		
	}

}
