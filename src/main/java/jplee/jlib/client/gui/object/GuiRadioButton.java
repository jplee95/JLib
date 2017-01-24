package jplee.jlib.client.gui.object;


public class GuiRadioButton extends GuiObject {

	private boolean beingClicked;
	private int state;
	private boolean isChecked;
	private String label;
	private int labelColor;
	private int labelHoverColor;
	private RadioGroup group;
	
	public GuiRadioButton(int x, int y) {
		super(x, y, 7, 7);
		this.isChecked = false;
		this.state = 0;
		this.label = "";
		this.labelColor = 0xffffff;
		this.labelHoverColor = 0xffffaa;
		this.group = null;
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
	
	public void setGroup(RadioGroup group) {
		this.group = group;
	}

	@Override
	protected void onMouseClick(int mouseX, int mouseY, int mouseButton) {
		super.onMouseClick(mouseX, mouseY, mouseButton);
		this.beingClicked = true;
	}
	
	protected void onMouseRelease(int mouseX, int mouseY, int mouseButton) {
		super.onMouseRelease(mouseX, mouseY, mouseButton);
		this.beingClicked = false;
		if(group != null)
			group.setStates();
		this.isChecked = true;
	};
	
	@Override
	protected void onMouseDrag(int mouseX, int mouseY, int mouseButton, long timeSinceLastClick) {
		super.onMouseDrag(mouseX, mouseY, mouseButton, timeSinceLastClick);
		if(this.isOver(mouseX, mouseY))
			state = state & 0b00;
//			this.beingClicked = true;
		else
			state = state & 0b00;
//			this.beingClicked = false;
	}
	
	@Override
	protected void onMouseHover(int mouseX, int mouseY) {
		super.onMouseHover(mouseX, mouseY);
		state = 0b01;
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
		
		int shift = 14 * (this.isEnabled() ? (this.state & 2) == 2 ? 2 : (this.state & 1) == 1 ? 1 : 0 : 3) + (isChecked ? 7 : 0);
		this.drawTexturedModalRect(x, y, 0 + shift, 39, 7, 7);
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {
		if(!label.isEmpty()) {
			int x = this.x + this.getXShift() - 1;
			int y = this.y + this.getYShift() - 1;
			boolean over = this.isOver(mouseX, mouseY);
			this.drawString(this.fontRender, this.label, x + 9, y, over ? this.labelHoverColor : this.labelColor);
		}
	}

	@Override
	public boolean isOver(int mouseX, int mouseY) {
		if(this.isHoverable() && !this.isHidden()) {
			int strLen = label.isEmpty() ? 0 : this.fontRender.getStringWidth(label) + 2;
			return this.isPointInRegion(this.x, this.y, this.width + strLen, this.height, mouseX, mouseY);
		}
		return false;
	}
}
