package tk.tuxclient.gui.cosmetic;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;

import tk.tuxclient.Tux;
import tk.tuxclient.cosmetics.CosmeticManager;
import tk.tuxclient.gui.cosmetic.button.TuxEnabledCosmeticButton;
import tk.tuxclient.gui.cosmetic.button.TuxEnabledCosmeticButton2;
import tk.tuxclient.gui.mods.TuxEnabledButton;
import tk.tuxclient.gui.mods.TuxHudSwitcherButton;
import tk.tuxclient.gui.screens.NewModToggle;
import tk.tuxclient.mods.Mod;
import tk.tuxclient.mods.ModInstances;
import tk.tuxclient.mods.Mods;
import tk.tuxclient.util.TuxDrawUtils;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class CosmeticToggle extends GuiScreen {

    private final List<Mod> listOfMods = new Mods().listOfMods;
    int number = 0;

    private void addButtons() {
        this.buttonList.add(new TuxHudSwitcherButton(1017, 165, 30, 160, 60, "Mods"));
        this.buttonList.add(new TuxHudSwitcherButton(1018, 330, 30, 160, 60, "Cosmetics"));
        this.buttonList.add(new TuxHudSwitcherButton(1019, 495, 30, 160, 60, "Settings"));
    }

    @Override
    public void initGui() {
        addButtons();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        // double x, double y, double x1, double y1, double radius, int color
        TuxDrawUtils.drawRoundedRect(150, 25, this.width - 150,this.height - 25, 10, new Color(27, 30, 26, 230).getRGB());
        TuxDrawUtils.drawRoundedRect(160, 100, this.width - 300,this.height - 35, 10, new Color(115,120,140, 70).getRGB());
        for (int i = 0; i < Tux.INSTANCE.cosmeticManager.cosmetics.size() + Tux.INSTANCE.cosmeticManager.cosmeticModels.size(); i++) {
            if (i < 3) {
                TuxDrawUtils.drawRoundedRect(165 + (i * 165), 105, 325 + (i * 165), 255, 10, new Color(115, 120, 140, 70).getRGB());
            } if (i >=3 && i < 6) {
                if (i == 3) {
                    number = 0;
                }
                if (i == 4) {
                    number = 1;
                }
                if (i == 5) {
                    number = 2;
                }
                TuxDrawUtils.drawRoundedRect(165 + (number * 165), 275, 325 + (number * 165), 425, 10, new Color(115, 120, 140, 70).getRGB());
            }

            // Buttons
            if (i == 0) {
                this.buttonList.add(new TuxEnabledCosmeticButton(1011, 165, 235, 160, 20,  CosmeticManager.snowmanCape.name, CosmeticManager.snowmanCape));
            }
            if (i == 1) {
                this.buttonList.add(new TuxEnabledCosmeticButton2(1012, 330, 235, 160, 20, CosmeticManager.cosmeticWings.name, CosmeticManager.cosmeticWings));
            }
           

            // float x, float y, float u, float v, int width, int height, float textureWidth, float textureHeight

           /* this.mc.getTextureManager().bindTexture(listOfMods.get(i).icon);
            if (i < 3) {
                Gui.drawModalRectWithCustomSizedTexture(200 + (i * 165), 120, 0, 0, 100, 100, 100, 100);
            } if (i >= 3 && i < 6) {
                Gui.drawModalRectWithCustomSizedTexture(200 + (number * 165), 290, 0, 0, 100, 100, 100, 100);

            }
            GL11.glColor4f(1f, 1f, 1f, 1f);*/
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1011) {
        	CosmeticManager.snowmanCape.toggleWearing();
        }
        if (button.id == 1012) {
        	CosmeticManager.cosmeticWings.toggleWearing();
        }
        if (button.id == 1017) {
            this.mc.displayGuiScreen(new NewModToggle());
        }
    }

}
