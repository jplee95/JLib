package jplee.jlib.client.gui.object;

public class GuiButton extends GuiObject {
	
	private int state;
	private String label;
	private int labelColor;
	private int labelHoverColor;
	
	public GuiButton(int x, int y, int w, int h) {
		super(x, y, w, h);
		state = 0;
		this.label = "";
		this.labelColor = 0xffffff;
		this.labelHoverColor = 0xffffaa;
		this.depth = 1;
		
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public void setLabelColor(int color) {
		this.labelColor = color;
	}

	public void setLabelHoverColor(int color) {
		this.labelHoverColor = color;
	}
	
	@Override
	protected void onMouseClick(int mouseX, int mouseY, int mouseButton) {
		super.onMouseClick(mouseX, mouseY, mouseButton);
		this.state |= 0b10;
	}
	
	protected void onMouseRelease(int mouseX, int mouseY, int mouseButton) {
		super.onMouseRelease(mouseX, mouseY, mouseButton);
		this.state &= 0b01;
	};
	
	@Override
	protected void onMouseDrag(int mouseX, int mouseY, int mouseButton, long timeSinceLastClick) {
		super.onMouseDrag(mouseX, mouseY, mouseButton, timeSinceLastClick);
		if(this.isOver(mouseX, mouseY))
			this.state |= 0b10;
		else
			this.state &= 0b01;
	}
	
	@Override
	protected void onMouseHover(int mouseX, int mouseY) {
		super.onMouseHover(mouseX, mouseY);
		if(this.isOver(mouseX, mouseY))
			this.state |= 0b01;
		else
			this.state &= 0b10;
	}
	
	@Override
	public void update() {
		if((this.state & 0b01) == 1)
			this.state &= 0b10;
		
	}
	
	@Override
	public void drawBackground(int mouseX, int mouseY, float partialTicks) {
		this.mc.getTextureManager().bindTexture(DEFAULT_RESOURCE);

		int x = this.x + this.getXShift() - 1;
		int y = this.y + this.getYShift() - 1;
		
		if(this.width > 0 && this.height > 0) {
			int shift = 32 * (this.isEnabled() ? (this.state & 2) == 2 ? 2 : (this.state & 1) == 1 ? 1 : 0 : 3);
			this.drawTexturedBorderedModalRect(0 + shift, 0, 32, 32, 1, x, y, width, height);
		}
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {
		if(!label.isEmpty()) {
			int x = this.x + this.getXShift() - 1;
			int y = this.y + this.getYShift() - 1;
			boolean over = this.isOver(mouseX, mouseY);
			this.drawCenteredString(this.fontRender, this.label, x + width / 2 + 1, y + height / 2 - 3, over ? this.labelHoverColor : this.labelColor);
		}
	}

}
