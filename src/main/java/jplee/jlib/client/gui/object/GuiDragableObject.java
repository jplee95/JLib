package jplee.jlib.client.gui.object;

public abstract class GuiDragableObject extends GuiObject {

	private int clickedX = 0;
	private int clickedY = 0;
	private boolean dragged = false;
	
	public GuiDragableObject(int x, int y, int w, int h) {
		super(x, y, w, h);
	}

	@Override
	protected void onMouseClick(int mouseX, int mouseY, int mouseButton) {
		super.onMouseClick(mouseX, mouseY, mouseButton);
		clickedX = mouseX;
		clickedY = mouseY;
		dragged = false; 
	}
	
	@Override
	protected void onMouseRelease(int mouseX, int mouseY, int mouseButton) {
		if(!this.dragged)
			super.onMouseRelease(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void onMouseDrag(int mouseX, int mouseY, int mouseButton, long timeSinceLastClick) {
		super.onMouseDrag(mouseX, mouseY, mouseButton, timeSinceLastClick);
		this.dragged = true;
		int shiftedX = mouseX - clickedX;
		int shiftedY = mouseY - clickedY;
		this.x += shiftedX;
		this.y += shiftedY;
		clickedX += shiftedX;
		clickedY += shiftedY;
	}
	
}
