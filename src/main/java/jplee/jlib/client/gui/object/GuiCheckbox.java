package jplee.jlib.client.gui.object;


public class GuiCheckbox extends GuiObject {

	private boolean beingClicked;
	private boolean isChecked;
	private String label;
	private int labelColor;
	private int labelHoverColor;
	
	public GuiCheckbox(int x, int y) {
		super(x, y, 7, 7);
		this.isChecked = false;
		this.label = "";
		this.labelColor = 0xffffff;
		this.labelHoverColor = 0xffffaa;
	}

	public boolean isChecked() {
		return this.isChecked;
	}
	
	public void setChecked(boolean check) {
		this.isChecked = check;
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
		this.beingClicked = true;
	}
	
	protected void onMouseRelease(int mouseX, int mouseY, int mouseButton) {
		super.onMouseRelease(mouseX, mouseY, mouseButton);
		this.beingClicked = false;
		this.isChecked = !this.isChecked;
	};
	
	@Override
	protected void onMouseDrag(int mouseX, int mouseY, int mouseButton, long timeSinceLastClick) {
		super.onMouseDrag(mouseX, mouseY, mouseButton, timeSinceLastClick);
		if(this.isOver(mouseX, mouseY))
			this.beingClicked = true;
		else
			this.beingClicked = false;
	}
	
	@Override
	public void drawBackground(int mouseX, int mouseY, float partialTicks) {
		this.mc.getTextureManager().bindTexture(DEFAULT_RESOURCE);

		int x = this.x + this.getXShift() - 1;
		int y = this.y + this.getYShift() - 1;
		
		int shift = 14 * (this.isEnabled() ? beingClicked ? 2 : this.isOver(mouseX, mouseY) ? 1 : 0 : 3) + (isChecked ? 7 : 0);
		this.drawTexturedModalRect(x, y, 0 + shift, 32, 7, 7);
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {
		int x = this.x + this.getXShift() - 1;
		int y = this.y + this.getYShift() - 1;

		boolean over = this.isOver(mouseX, mouseY);
		this.drawString(this.fontRender, this.label, x + 9, y, over ? this.labelHoverColor : this.labelColor);
	}

	@Override
	public boolean isOver(int mouseX, int mouseY) {
		return this.isPointInRegion(this.x, this.y, this.width + this.fontRender.getStringWidth(label) + 2, this.height, mouseX, mouseY);
	}
}
