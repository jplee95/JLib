package jplee.jlib.client.gui.object;

import java.io.IOException;
import java.util.List;

import jplee.jlib.client.gui.BaseGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GuiObject extends Gui {

	protected static final ResourceLocation DEFAULT_RESOURCE = new ResourceLocation("jlib:textures/gui_components.png");
	
	protected Minecraft mc;
	protected RenderItem itemRender;
	protected FontRenderer fontRender;
	
	protected BaseGui attactedGui;
	protected IGuiContainer guiContainer;
	
	public int x, y;
	public int depth;
	public int width, height;
	
	protected boolean isHideable;
	protected boolean hidden;
	protected boolean isHoverable;
	protected boolean isEnabled;
	
	private IGuiEvent mouseClickEvent;
	private IGuiEvent mouseReleaseEvent;
	private IGuiEvent mouseDragEvent;
	private IGuiEvent mouseHoverEvent;
	
	public GuiObject(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.zLevel = 0;
		this.depth = 0;
		
		this.isHideable = false;
		this.hidden = false;
		this.isHoverable = true;
		this.isEnabled = true;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public void hide() {
		this.hidden = isHideable;
	}
	
	public void show() {
		this.hidden = false;
	}
	
	public boolean isHidden() {
		return this.hidden;
	}
	
	public boolean canHide() {
		return this.isHideable;
	}
	
	public boolean isHoverable() {
		return isHoverable;
	}
	
	public boolean isEnabled() {
		return this.isEnabled;
	}
	
	public void enable() {
		this.isEnabled = true;
	}
	
	public void disable() {
		this.isEnabled = false;
	}
	
	public boolean isOver(int mouseX, int mouseY) {
		if(this.isHoverable() && !this.isHidden()) {
			return this.isPointInRegion(this.x, this.y, this.width, this.height, mouseX, mouseY);
		}
		return false;
	}

    /**
     * Test if the 2D point is in a rectangle (relative to the GUI). Args : rectX, rectY, rectWidth, rectHeight, pointX,
     * pointY
     */
    protected boolean isPointInRegion(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY)
    {
        int i = this.getXShift();
        int j = this.getYShift();
        pointX = pointX - i;
        pointY = pointY - j;
        return pointX >= rectX - 1 && pointX < rectX + rectWidth + 1 && pointY >= rectY - 1 && pointY < rectY + rectHeight + 1;
    }

	public abstract void drawBackground(int mouseX, int mouseY, float partialTicks);
	public abstract void drawForeground(int mouseX, int mouseY);

	public void update() {
		
	}
	
	protected void onMouseClick(int mouseX, int mouseY, int mouseButton) {
		if(mouseClickEvent != null)
			mouseClickEvent.onGuiAction(this.attactedGui, mouseX, mouseY, mouseButton);
	}
	
	protected void onMouseRelease(int mouseX, int mouseY, int mouseButton) {
		if(mouseReleaseEvent != null)
			mouseReleaseEvent.onGuiAction(this.attactedGui, mouseX, mouseY, mouseButton);
	}

	protected void onMouseDrag(int mouseX, int mouseY, int mouseButton, long timeSinceLastClick) {
		if(mouseDragEvent != null)
			mouseDragEvent.onGuiAction(this.attactedGui, mouseX, mouseY, mouseButton);
		
	}
	
	protected void onMouseHover(int mouseX, int mouseY) {
		if(mouseHoverEvent != null)
			mouseHoverEvent.onGuiAction(this.attactedGui, mouseX, mouseY, -1);
		
	}

	protected void onKeyTyped(char typedChar, int keyCode) {
		
	}
	
	public void handleKeyboardInput(char charater, int key, int state) throws IOException {
		switch(state) {
		case 0: onKeyTyped(charater, key); break;
		}
	}
	
	public void handleMouseInput(int mouseX, int mouseY, int button, long timeSinceLastClick, MouseState state) throws IOException {
		switch(state) {
		case CLICK: this.onMouseClick(mouseX, mouseY, button); break;
		case RELEASE: this.onMouseRelease(mouseX, mouseY, button); break;
		case DRAG: this.onMouseDrag(mouseX, mouseY, button, timeSinceLastClick); break;
		case HOVER: this.onMouseHover(mouseX, mouseY); break;
		}
	}
	
	protected int getXShift() {
		if(this.guiContainer != null) return this.guiContainer.xShift();
		return this.attactedGui == null ? 0 : this.attactedGui.getXShift();
	}
	
	protected int getYShift() {
		if(this.guiContainer != null) return this.guiContainer.yShift();
		return this.attactedGui == null ? 0 : this.attactedGui.getYShift();
	}
	
	protected String getClipboardString() {
		return GuiScreen.getClipboardString();
	}
	
	protected void sendChatMessage(String message) {
		sendChatMessage(message, false);
	}
	
	protected void sendChatMessage(String message, boolean deletable) {
		sendChatMessage(message, deletable, true);
	}
	
	protected void sendChatMessage(String message, boolean deletable, boolean addToChat) {
		if(addToChat)
			this.mc.ingameGUI.getChatGUI().addToSentMessages(message);
		if(ClientCommandHandler.instance.executeCommand(mc.thePlayer, message) != 0) return;
		
		this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(message), 1);
	}
	
	public void setMinecraft(Minecraft mc) {
		this.mc = mc;
		this.itemRender = mc.getRenderItem();
		this.fontRender = mc.fontRendererObj;
	}
	
	/** DO NOT use this at any reason. May cause breaking of things. */
	public void attatchGui(BaseGui gui) {
		if(attactedGui == null && gui != null) {
			this.attactedGui = gui;
			if(this.mc == null && gui.mc != null)
				this.setMinecraft(gui.mc);
		}
		if(this.mc == null && this.attactedGui == null)
			this.setMinecraft(Minecraft.getMinecraft());
 	}

	/** DO NOT use this at any reason. May cause breaking of things. */
	public void attachContainer(IGuiContainer container) {
		if(guiContainer == null && container != null) {
			guiContainer = container;
			if(this.mc == null && container.getMinecraft() != null)
				this.setMinecraft(container.getMinecraft());
		}
	}
	
	public void attachMouseEvent(MouseState state, IGuiEvent event) {
		switch(state) {
		case CLICK: this.mouseClickEvent = event; break;
		case RELEASE: this.mouseReleaseEvent = event; break;
		case DRAG: this.mouseDragEvent = event; break;
		case HOVER: this.mouseHoverEvent = event; break;
		}
	}
	
	public GuiObject getObject() {
		return this;
	}
	
	private static boolean shouldClip = false;
	private static int boxX = 0;
	private static int boxY = 0;
	private static int boxWidth = 0;
	private static int boxHeight = 0;
	
	public static void setClipBox(int x, int y, int width, int height) {
		GuiObject.boxX = x;
		GuiObject.boxY = y;
		GuiObject.boxWidth = width;
		GuiObject.boxHeight = height;
	}
	
	public static void enableClipping() {
		shouldClip = true;
	}
	
	public static void disableClipping() {
		shouldClip = false;
	}
	
	public static boolean insideClipBoxRect(int x, int y, int width, int height) {
		return insideClipBoxPoint(x, y, x + width - 1, y + height - 1);
	}
	
	public static boolean insideClipBoxPoint(int x1, int y1, int x2, int y2) {
		if(boxX != -1 && boxY != -1 && boxWidth != -1 && boxHeight != -1) {
			int bx2 = boxX + boxWidth;
			int by2 = boxY + boxHeight;
			if(x2 >= boxX && y2 >= boxY && x1 <= bx2 && y1 <= by2) {
				return true;
			}
		}
		return false;
	}
	
	protected void drawTexturedBorderedModalRect(int recX, int recY, int recWidth, int recHeight, int boarderSize, int x, int y, int width, int height) {
		if(width > 0 && height > 0) {
			int bs = boarderSize;
			int irw = recWidth - 2 * bs;
			int irh = recHeight - 2 * bs;
			
			int repX = (width - 2 * bs) / irw;
			int remX = (width - 2 * bs) % irw;
			int repY = (height - 2 * bs) / irh;
			int remY = (height - 2 * bs) % irh;

			this.drawTexturedModalRect(x, y, recX, recY, bs, bs);
			this.drawTexturedModalRect(x, y + height - bs, recX, recY + recHeight - bs, bs, bs);
			this.drawTexturedModalRect(x + width - bs, y, recX + recWidth - bs, recY, bs, bs);
			this.drawTexturedModalRect(x + width - bs, y + height - bs, recX + recWidth - bs, recY + recHeight - bs, bs, bs);
			
			for(int my = 0; my <= repY; my++) {
				if(my != repY) {
					this.drawTexturedModalRect(x, y + bs + irh * my, recX, recY + bs, bs, irh);
					this.drawTexturedModalRect(x + irw * repX + remX + bs, y + bs + irh * my, recX + recWidth - bs, recY + bs, bs, irh);
				} else {
					this.drawTexturedModalRect(x, y + bs + irh * my, recX, recY + bs, bs, remY);
					this.drawTexturedModalRect(x + irw * repX + remX + bs, y + bs + irh * my, recX + recWidth - bs, recY + bs, bs, remY);
				}
				for(int mx = 0; mx <= repX; mx++) {
					if(my == 0 && mx != repX) {
						this.drawTexturedModalRect(x + bs + irw * mx, y, recX + bs, recY, irw, bs);
						this.drawTexturedModalRect(x + bs + irw * mx, y + irh * repY + remY + bs, recX + bs, recY + recHeight - bs, irw, bs);
					} else if(my == 0) {
						this.drawTexturedModalRect(x + bs + irw * mx, y, recX + bs, recY, remX, bs);
						this.drawTexturedModalRect(x + bs + irw * mx, y + irh * repY + remY + bs, recX + bs, recY + recHeight - bs, remX, bs);
					}
					if(mx != repX && my != repY)
						this.drawTexturedModalRect(x + bs + irw * mx, y + bs + irh * my, recX + bs, recY + bs, irw, irh);
					else if(mx == repX && my != repY)
						this.drawTexturedModalRect(x + bs + irw * mx, y + bs + irh * my, recX + bs, recY + bs, remX, irh);
					else if(mx != repX && my == repY)
						this.drawTexturedModalRect(x + bs + irw * mx, y + bs + irh * my, recX + bs, recY + bs, irw, remY);
					else
						this.drawTexturedModalRect(x + bs + irw * mx, y + bs + irh * my, recX + bs, recY + bs, remX, remY);
						
				}
			}
		}
	}

	protected void drawItemStack(ItemStack stack, int x, int y, String altText) {
		GlStateManager.translate(0, 0, 32.0f);
		this.itemRender.zLevel = 200.0f;
		FontRenderer font = null;
		if(stack != null) font = stack.getItem().getFontRenderer(stack);
		if(font == null) font = fontRender;
		this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
		this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y, altText);
	}

    protected void renderToolTip(ItemStack stack, int x, int y) {
        List<String> list = stack.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);

        for(int i = 0; i < list.size(); ++i) {
            if (i == 0) {
                list.set(i, stack.getRarity().rarityColor + (String)list.get(i));
            } else {
                list.set(i, TextFormatting.GRAY + (String)list.get(i));
            }
        }
        FontRenderer font = stack.getItem().getFontRenderer(stack);
        net.minecraftforge.fml.client.config.GuiUtils.preItemToolTip(stack);
        this.drawHoveringText(list, x, y, (font == null ? fontRender : font));
        net.minecraftforge.fml.client.config.GuiUtils.postItemToolTip();
    }

	protected void drawHoveringText(List<String> textLines, int x, int y) {
		drawHoveringText(textLines, x, y, this.fontRender);
	}
	
	protected void drawHoveringText(List<String> textLines, int x, int y, FontRenderer font) {
		GuiUtils.drawHoveringText(textLines, x, y, this.attactedGui.width, this.attactedGui.height, -1, font);
	}
	
	@Override
	protected void drawHorizontalLine(int startX, int endX, int y, int color) {
		if(shouldClip) {
			if(endX < startX) {
				int i = startX;
				startX = endX;
				endX = i;
			}
			if(insideClipBoxPoint(startX, y, endX + 1, y + 1))
				drawRect(startX, y, endX + 1, y + 1, color);
		} else {
			super.drawHorizontalLine(startX, endX, y, color);
		}
	}
	
	@Override
	protected void drawVerticalLine(int x, int startY, int endY, int color) {
		if(shouldClip) {
			if(endY < startY) {
				int i = startY;
				startY = endY;
				endY = i;
			}
			if(insideClipBoxPoint(x, startY, x + 1, endY + 1))
				drawRect(x, startY, x + 1, endY + 1, color);
		} else {
			super.drawVerticalLine(x, startY, endY, color);
		}
	}
	
	public static void drawRect(int left, int top, int right, int bottom, int color) {
		if(shouldClip) {
	        if(left > right) {
	            int i = left;
	            left = right;
	            right = i;
	        }
	        if(top > bottom) {
	            int j = top;
	            top = bottom;
	            bottom = j;
	        }
	        if(insideClipBoxPoint(left, top, right, bottom)) {
				int bx2 = boxX + boxWidth;
				int by2 = boxY + boxHeight;
		        left = left < boxX ? boxX : left;
		        top = top < boxY ? boxY : top;
		        right = right > bx2 ? bx2 : right;
		        bottom = bottom > by2 ? by2 : bottom;
		        Gui.drawRect(left, top, right, bottom, color);
	        }
		} else {
			Gui.drawRect(left, top, right, bottom, color);
		}
	}
	
	@Override
	public void drawString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
		if(shouldClip) {
			int textWidth = fontRendererIn.getStringWidth(text);
			if(insideClipBoxRect(x, y, textWidth, fontRendererIn.FONT_HEIGHT)) {
				if(y + fontRendererIn.FONT_HEIGHT - 1 < boxY + boxHeight) {
					int rem = boxX + boxWidth - (x);
					text = fontRendererIn.trimStringToWidth(text, rem);
					super.drawString(fontRendererIn, text, x, y, color);
				}
			}
		} else
			super.drawString(fontRendererIn, text, x, y, color);
	}
	
	@Override
	public void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
		this.drawString(fontRendererIn, text, x - fontRendererIn.getStringWidth(text) / 2, y, color);
	}
	
	@Override // TODO: FIX THIS FOR COLOR
	protected void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
		if(shouldClip) {
	        if(left > right) {
	            int i = left;
	            left = right;
	            right = i;
	        }
	        if(top > bottom) {
	            int j = top;
	            top = bottom;
	            bottom = j;
	        }
	        if(insideClipBoxPoint(left, top, right, bottom)) {
				int bx2 = boxX + boxWidth;
				int by2 = boxY + boxHeight;
				int height = bottom - top + 1;
		        int rem = by2 - top + 1;
		        float percent = (float)rem / (float)height;
		        percent = Math.min(Math.max(percent, 0), 100);
		        
		        left = left < boxX ? boxX : left;
		        top = top < boxY ? boxY : top;
		        right = right > bx2 ? bx2 : right;
		        bottom = bottom > by2 ? by2 : bottom;

//		        int a1 = startColor >> 24 & 255;
//		        int r1 = startColor >> 16 & 255;
//		        int g1 = startColor >> 8 & 255;
//		        int b1 = startColor & 255;
//		        
//		        int a2 = endColor >> 24 & 255;
//		        int r2 = endColor >> 16 & 255;
//		        int g2 = endColor >> 8 & 255;
//		        int b2 = endColor & 255;
//		        
//		        int af = Math.min((int)((float)Math.abs(a1 - a2) * percent), 255);
//		        int rf = Math.min((int)((float)Math.abs(r1 - r2) * percent), 255);
//		        int gf = Math.min((int)((float)Math.abs(g1 - g2) * percent), 255);
//		        int bf = Math.min((int)((float)Math.abs(b1 - b2) * percent), 255);
		        
//		        int finalEnd = (af << 24) + (rf << 16) + (gf >> 8) + bf;
		        super.drawGradientRect(left, top, right, bottom, startColor, endColor);
	        }
		} else if(!shouldClip) {
			super.drawGradientRect(left, top, right, bottom, startColor, endColor);
		}
	}
	
	@Override
	public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
		if(shouldClip) {
	        if(insideClipBoxRect(x, y, width, height)) {
	        	int x2 = x + width;
	        	int y2 = y + height;
				int bx2 = boxX + boxWidth;
				int by2 = boxY + boxHeight;
				int sx = boxX - x;
				int sy = boxY - y;
	        	int remW = bx2 - x;
	        	int remH = by2 - y;
	        	
	        	width = (x2 > bx2 ? remW : width) - (sx > 0 ? sx : 0);
	        	height = (y2 > by2 ? remH : height) - (sy > 0 ? sy : 0);

	        	x = x < boxX ? x + sx : x;
	        	y = y < boxY ? y + sy : y;
	        	textureX += (sx > 0 ? sx : 0);
	        	textureY += (sy > 0 ? sy : 0);
	        	
	        	super.drawTexturedModalRect(x, y, textureX, textureY, width, height);
	        }
		} else {
			super.drawTexturedModalRect(x, y, textureX, textureY, width, height);
		}
	}
	
	@Override // TODO figure out how to do this one
	public void drawTexturedModalRect(float xCoord, float yCoord, int minU, int minV, int maxU, int maxV) {
		if(shouldClip) {
			super.drawTexturedModalRect(xCoord, yCoord, minU, minV, maxU, maxV);
		} else {
			super.drawTexturedModalRect(xCoord, yCoord, minU, minV, maxU, maxV);
		}
	}
	
	@Override // TODO figure out how to do this one
	public void drawTexturedModalRect(int xCoord, int yCoord, TextureAtlasSprite textureSprite, int widthIn, int heightIn) {
		if(shouldClip) {
			super.drawTexturedModalRect(xCoord, yCoord, textureSprite, widthIn, heightIn);
		} else {
			super.drawTexturedModalRect(xCoord, yCoord, textureSprite, widthIn, heightIn);
		}
	}
	
	// TODO figure out how to do this one
	public static void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight) {
		
	}
	
	// TODO figure out how to do this one
	public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int height, float tileWidth, float tileHeight) {
		
	}
}
