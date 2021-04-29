package tk.tuxclient.mods;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Predicate;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import tk.tuxclient.gui.mods.TuxModButton;
import tk.tuxclient.gui.screens.NewModToggle;

public class HUDConfigScreen extends GuiScreen {
	
	private final HashMap<IRenderer, ScreenPosition> renderers = new HashMap<>();
	
	private Optional<IRenderer> selectedRenderer = Optional.empty();
	
	private int prevX, prevY;
	
	public HUDConfigScreen(HUDManager api) {
		
		Collection<IRenderer> registedRenderers = api.getRegisteredRenderers();
		
		for(IRenderer ren : registedRenderers) {
			if(!ren.isEnabled()) {
				continue;
			}
			ScreenPosition pos = ren.load();
			
			if (pos == null) {
				pos = ScreenPosition.fromRelativePosition(0.5, 0.5);
			}
			adjustBounds(ren, pos);
			this.renderers.put(ren, pos);
		}
	}

	@Override
	public void initGui() {
		this.buttonList.add(new TuxModButton(1007, this.width / 2 - 100, this.height / 2, 200, 30, "Mods"));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		final float zBackup = this.zLevel;
		this.zLevel = 200;

		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("tux/icon/logo.png"));
		Gui.drawModalRectWithCustomSizedTexture(this.width / 2 - 50, this.height / 2 - 100, 0, 0, 100, 100, 100, 100);

		for(IRenderer renderer : renderers.keySet()) {
			ScreenPosition pos = renderers.get(renderer);
				
			renderer.renderDummy(pos);
				
			this.drawHollowRect(pos.getAbsoluteX(), pos.getAbsoluteY(), renderer.getWidth(), renderer.getHeight(), -1);
		}
		
		this.zLevel = zBackup;
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	private void drawHollowRect(int x, int y, int w, int h, int color) {
		this.drawHorizontalLine(x, x + w, y, color);
		this.drawHorizontalLine(x, x + w, y + h, color);
		
		this.drawVerticalLine(x, y + h, y, color);
		this.drawVerticalLine(x + w, y + h, y, color);
	}
		
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if(keyCode == Keyboard.KEY_ESCAPE) {
			renderers.forEach(IRenderConfig::save);
			this.mc.displayGuiScreen(null);
		}
	}
	
	@Override
	protected void mouseClickMove(int x, int y, int button, long time) {
		if(selectedRenderer.isPresent()) {
			moveSelectedRenderBy(x - prevX, y - prevY);
		}
		
		this.prevX = x;
		this.prevY = y;
		
	}

	private void moveSelectedRenderBy(int offsetX, int offsetY) {
		if (selectedRenderer.isPresent()) {
			IRenderer renderer = selectedRenderer.get();
			ScreenPosition pos = renderers.get(renderer);

			pos.setAbsolute(pos.getAbsoluteX() + offsetX, pos.getAbsoluteY() + offsetY);

			adjustBounds(renderer, pos);
		}

	}
	
	@Override
	public void onGuiClosed() {
		for(IRenderer renderer: renderers.keySet()) {
			renderer.save(renderers.get(renderer));
		}
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
	
	private void adjustBounds(IRenderer renderer, ScreenPosition pos) {
		
		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
		
		int screenWidth = res.getScaledWidth();
		int screenHeight = res.getScaledHeight();
		
		int absoluteX = Math.max(0, Math.min(pos.getAbsoluteX(), Math.max(screenWidth - renderer.getWidth(), 0)));
		int absoluteY = Math.max(0, Math.min(pos.getAbsoluteY(), Math.max(screenHeight - renderer.getHeight(), 0)));
		
		pos.setAbsolute(absoluteX, absoluteY);
	}
	
	@Override
	protected void mouseClicked(int x, int y, int button) throws IOException {
		this.prevX = x;
		this.prevY = y;
		
		loadMouseOver(x, y);
		super.mouseClicked(x, y, button);
	}

	private void loadMouseOver(int x, int y) {
		this.selectedRenderer = renderers.keySet().stream().filter(new MouseOverFinder(x, y)).findFirst();
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 1007) {
			this.mc.displayGuiScreen(new NewModToggle());
		}
	}

	private class MouseOverFinder implements Predicate<IRenderer> {
		
		private int mouseX, mouseY;
		
		public MouseOverFinder(int x, int y) {
			this.mouseX = x;
			this.mouseY = y;
		}
		
		@Override
		public boolean test(IRenderer renderer) {

			ScreenPosition pos = renderers.get(renderer);
			
			int absoluteX = pos.getAbsoluteX();
			int absoluteY = pos.getAbsoluteY();
			
			if(mouseX >= absoluteX && mouseX <= absoluteX + renderer.getWidth()) {

				return mouseY >= absoluteY && mouseY <= absoluteY + renderer.getHeight();
				
			}
			
			return false;
		}
	}
	
}
