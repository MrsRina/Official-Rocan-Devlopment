package me.rina.rocan.client.gui.module.setting.container;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.container.Container;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.api.setting.Setting;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.module.container.ModuleContainer;
import me.rina.rocan.client.gui.module.module.widget.ModuleCategoryWidget;
import me.rina.rocan.client.gui.module.module.widget.ModuleWidget;
import me.rina.rocan.client.gui.module.mother.MotherFrame;
import me.rina.rocan.client.gui.module.setting.widget.SettingBooleanWidget;
import me.rina.rocan.client.gui.module.setting.widget.SettingNumberWidget;
import me.rina.rocan.client.gui.module.visual.LabelWidget;
import me.rina.turok.render.opengl.TurokRenderGL;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokRect;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

/**
 * @author SrRina
 * @since 2020-12-14 at 21:24
 **/
public class SettingContainer extends Container {
    private ModuleClickGUI master;
    private MotherFrame frame;

    private ModuleCategoryWidget widgetCategory;
    private ModuleContainer container;

    private ModuleWidget widgetModule;

    private float offsetX;
    private float offsetY;

    private float offsetWidth;
    private float offsetHeight;

    /**
     * Main list of widget of the setting container of type Widget.
     */
    private ArrayList<Widget> loadedWidgetList;

    /**
     * We need create the label that is the description of the module in the container.
     * Later we will need use this to get the height and reset height scroll, so, easy concept.
     */
    private LabelWidget descriptionLabel;

    private TurokRect scrollRect = new TurokRect("I will go to canada and forget everything make me bad.", 0, 0);
    private TurokRect realRect = new TurokRect("Real rect", 0, 0);

    public Flag flagMouse = Flag.MouseNotOver;
    public Flag flagMouseRealRect = Flag.MouseNotOver;
    public Flag flagDescription = Flag.MouseNotOver;

    public SettingContainer(ModuleClickGUI master, MotherFrame frame, ModuleCategoryWidget widgetCategory, ModuleContainer container, ModuleWidget widgetModule) {
        super(widgetModule.getModule().getTag());

        this.master = master;
        this.frame = frame;

        this.widgetCategory = widgetCategory;
        this.container = container;

        this.widgetModule = widgetModule;

        this.init();
    }

    public void init() {
        if (this.loadedWidgetList == null) {
            this.loadedWidgetList = new ArrayList<>();
        } else {
            this.loadedWidgetList.clear();
        }

        this.descriptionLabel = new LabelWidget(this.master, this.frame, this.widgetCategory, this.container, this.widgetModule, this, this.widgetModule.getModule().getDescription());
        this.descriptionLabel.setScroll(false);

        this.scrollRect.setHeight(0);
        this.offsetY = 3;

        for (Setting settings : this.widgetModule.getModule().getSettingList()) {
            if (settings.getValue() instanceof Boolean) {
                SettingBooleanWidget settingBooleanWidget = new SettingBooleanWidget(this.master, this.frame, this.widgetCategory, this.container, this.widgetModule, this, settings);

                settingBooleanWidget.setOffsetY(this.scrollRect.getHeight());

                this.loadedWidgetList.add(settingBooleanWidget);

                this.scrollRect.height += settingBooleanWidget.getRect().getHeight() + 1;
            }

            if (settings.getValue() instanceof Number) {
                SettingNumberWidget settingNumberWidget = new SettingNumberWidget(this.master, this.frame, this.widgetCategory, this.container, this.widgetModule, this, settings);

                settingNumberWidget.setOffsetY(this.scrollRect.getHeight());

                this.loadedWidgetList.add(settingNumberWidget);

                this.scrollRect.height += settingNumberWidget.getRect().getHeight() + 1;
            }
        }
    }

    /**
     * A quick time event to refresh the positions offsetY of widgets on loadedWidgetList.
     */
    public void onRefreshWidget() {
        // Set 0 the start up height value.
        this.scrollRect.setHeight(1);

        for (Widget widgets : this.loadedWidgetList) {
            if (widgets instanceof SettingBooleanWidget) {
                SettingBooleanWidget settingBooleanWidget = (SettingBooleanWidget) widgets;

                if (settingBooleanWidget.getSetting().isEnabled()) {
                    settingBooleanWidget.setOffsetY(this.scrollRect.getHeight());

                    this.scrollRect.height += settingBooleanWidget.getRect().getHeight() + 1;
                }
            }
        }
    }

    protected void setWidgetModule(ModuleWidget widgetModule) {
        this.widgetModule = widgetModule;
    }

    public ModuleWidget getWidgetModule() {
        return widgetModule;
    }

    public void setDescriptionLabel(LabelWidget descriptionLabel) {
        this.descriptionLabel = descriptionLabel;
    }

    public LabelWidget getDescriptionLabel() {
        return descriptionLabel;
    }

    public float getWidthScale() {
        float currentScaleX = (5 * this.frame.getScale());
        float scale = (2 * this.frame.getScale());

        return (this.frame.getRect().getWidth() - this.container.getRect().getWidth() - currentScaleX - scale + 1);
    }

    public void setScrollRect(TurokRect scrollRect) {
        this.scrollRect = scrollRect;
    }

    public TurokRect getScrollRect() {
        return scrollRect;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetWidth(float offsetWidth) {
        this.offsetWidth = offsetWidth;
    }

    public float getOffsetWidth() {
        return offsetWidth;
    }

    public void setOffsetHeight(float offsetHeight) {
        this.offsetHeight = offsetHeight;
    }

    public float getOffsetHeight() {
        return offsetHeight;
    }

    @Override
    public void onClose() {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onClose();
        }
    }

    @Override
    public void onOpen() {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onOpen();
        }
    }

    @Override
    public void onKeyboard(char character, int key) {
        for (Widget widgets : this.loadedWidgetList) {
            if (widgets instanceof SettingBooleanWidget) {
                SettingBooleanWidget settingBooleanWidget = (SettingBooleanWidget) widgets;

                if (settingBooleanWidget.getSetting().isEnabled()) {
                    widgets.onKeyboard(character, key);
                }
            }

            if (widgets instanceof SettingNumberWidget) {
                SettingNumberWidget settingNumberWidget = (SettingNumberWidget) widgets;

                if (settingNumberWidget.getSetting().isEnabled()) {
                    widgets.onKeyboard(character, key);
                }
            }
        }
    }

    @Override
    public void onCustomKeyboard(char character, int key) {
        for (Widget widgets : this.loadedWidgetList) {
            if (widgets instanceof SettingBooleanWidget) {
                SettingBooleanWidget settingBooleanWidget = (SettingBooleanWidget) widgets;

                if (settingBooleanWidget.getSetting().isEnabled()) {
                    widgets.onCustomKeyboard(character, key);
                }
            }

            if (widgets instanceof SettingNumberWidget) {
                SettingNumberWidget settingNumberWidget = (SettingNumberWidget) widgets;

                if (settingNumberWidget.getSetting().isEnabled()) {
                    widgets.onCustomKeyboard(character, key);
                }
            }
        }
    }

    @Override
    public void onMouseReleased(int button) {
        for (Widget widgets : this.loadedWidgetList) {
            if (widgets instanceof SettingBooleanWidget) {
                SettingBooleanWidget settingBooleanWidget = (SettingBooleanWidget) widgets;

                if (settingBooleanWidget.getSetting().isEnabled()) {
                    widgets.onMouseReleased(button);
                }
            }

            if (widgets instanceof SettingNumberWidget) {
                SettingNumberWidget settingNumberWidget = (SettingNumberWidget) widgets;

                if (settingNumberWidget.getSetting().isEnabled()) {
                    widgets.onMouseReleased(button);
                }
            }
        }
    }

    @Override
    public void onCustomMouseReleased(int button) {
        if (this.widgetModule.isSelected()) {
            for (Widget widgets : this.loadedWidgetList) {
                if (widgets instanceof SettingBooleanWidget) {
                    SettingBooleanWidget settingBooleanWidget = (SettingBooleanWidget) widgets;

                    if (settingBooleanWidget.getSetting().isEnabled()) {
                        widgets.onCustomMouseReleased(button);
                    }
                }

                if (widgets instanceof SettingNumberWidget) {
                    SettingNumberWidget settingNumberWidget = (SettingNumberWidget) widgets;

                    if (settingNumberWidget.getSetting().isEnabled()) {
                        widgets.onCustomMouseReleased(button);
                    }
                }
            }
        }
    }

    @Override
    public void onMouseClicked(int button) {
        for (Widget widgets : this.loadedWidgetList) {
            if (widgets instanceof SettingBooleanWidget) {
                SettingBooleanWidget settingBooleanWidget = (SettingBooleanWidget) widgets;

                if (settingBooleanWidget.getSetting().isEnabled()) {
                    widgets.onMouseClicked(button);
                }
            }

            if (widgets instanceof SettingNumberWidget) {
                SettingNumberWidget settingNumberWidget = (SettingNumberWidget) widgets;

                if (settingNumberWidget.getSetting().isEnabled()) {
                    widgets.onMouseClicked(button);
                }
            }
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {
        if (this.widgetModule.isSelected()) {
            for (Widget widgets : this.loadedWidgetList) {
                if (widgets instanceof SettingBooleanWidget) {
                    SettingBooleanWidget settingBooleanWidget = (SettingBooleanWidget) widgets;

                    if (settingBooleanWidget.getSetting().isEnabled()) {
                        widgets.onCustomMouseClicked(button);
                    }
                }

                if (widgets instanceof SettingNumberWidget) {
                    SettingNumberWidget settingNumberWidget = (SettingNumberWidget) widgets;

                    if (settingNumberWidget.getSetting().isEnabled()) {
                        widgets.onCustomMouseClicked(button);
                    }
                }
            }
        }
    }

    @Override
    public void onRender() {
        float positionXScaled = (this.container.getRect().getX() + this.container.getRect().getWidth()) + (2 * this.frame.getScale());
        float positionYScaled = this.container.getRect().getY();

        float realScrollHeight = this.descriptionLabel.getRect().getHeight() + 1;

        this.rect.setX(positionXScaled);
        this.rect.setY(positionYScaled);

        this.scrollRect.setX(this.rect.getX());
        this.scrollRect.setY((int) TurokMath.lerp(this.scrollRect.getY(), this.rect.getY() + realScrollHeight + this.offsetY, this.master.getPartialTicks()));

        this.realRect.setX(this.rect.getX());
        this.realRect.setY(this.rect.getY() + realScrollHeight);

        this.realRect.setWidth(this.rect.getWidth());
        this.realRect.setHeight(this.rect.getHeight() - realScrollHeight);

        // Its the offset geometry problem.
        int offsetFixOutline = 1;

        if (this.widgetModule.isSelected()) {
            this.flagMouseRealRect = this.realRect.collideWithMouse(this.master.getMouse()) ? Flag.MouseOver : Flag.MouseNotOver;
            this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MouseOver : Flag.MouseNotOver;

            // The fully background rect.
            TurokRenderGL.color(Rocan.getWrapperGUI().colorContainerBackground[0], Rocan.getWrapperGUI().colorContainerBackground[1], Rocan.getWrapperGUI().colorContainerBackground[2], Rocan.getWrapperGUI().colorContainerBackground[3]);
            TurokRenderGL.drawSolidRect(this.rect);

            // Render the description label widget.
            this.descriptionLabel.setOffsetY(1);
            this.descriptionLabel.onRender();

            float minimumScroll = this.rect.getHeight() - this.scrollRect.getHeight() - realScrollHeight - 2;
            float maximumScroll = 3;

            boolean isScrollLimit = this.scrollRect.getY() + this.scrollRect.getHeight() >= this.rect.getY() + this.rect.getHeight() - realScrollHeight - 3;

            if (this.flagMouseRealRect == Flag.MouseOver && this.master.getMouse().hasWheel() && isScrollLimit) {
                this.offsetY -= this.master.getMouse().getScroll();

                if (this.offsetY <= minimumScroll) {
                    this.offsetY = minimumScroll;
                }
            }

            if (this.offsetY >= maximumScroll) {
                this.offsetY = maximumScroll;
            }

            // We need push matrix to start scissor test.
            TurokRenderGL.pushMatrix();
            TurokRenderGL.enable(GL11.GL_SCISSOR_TEST);
            TurokRenderGL.drawScissor(this.rect.getX() - offsetFixOutline, (this.rect.getY() + realScrollHeight), this.rect.getWidth() + (offsetFixOutline * 2), this.rect.getHeight() - (realScrollHeight));

            for (Widget widgets : this.loadedWidgetList) {
                if (widgets instanceof SettingBooleanWidget) {
                    SettingBooleanWidget settingBooleanWidget = (SettingBooleanWidget) widgets;

                    if (settingBooleanWidget.getSetting().isEnabled()) {
                        widgets.onRender();
                    }
                }

                if (widgets instanceof SettingNumberWidget) {
                    SettingNumberWidget settingNumberWidget = (SettingNumberWidget) widgets;

                    if (settingNumberWidget.getSetting().isEnabled()) {
                        widgets.onRender();
                    }
                }
            }

            TurokRenderGL.disable(GL11.GL_SCISSOR_TEST);
            TurokRenderGL.popMatrix();
        } else {
            this.flagMouseRealRect = Flag.MouseNotOver;
        }

        if (this.flagDescription == Flag.MouseNotOver) {
            this.descriptionLabel.setText(this.widgetModule.getModule().getDescription());
        }

        this.flagDescription = Flag.MouseNotOver;
    }

    @Override
    public void onCustomRender() {
        for (Widget widgets : this.loadedWidgetList) {
            if (widgets instanceof SettingBooleanWidget) {
                SettingBooleanWidget settingBooleanWidget = (SettingBooleanWidget) widgets;

                if (settingBooleanWidget.getSetting().isEnabled()) {
                    widgets.onCustomRender();
                }
            }

            if (widgets instanceof SettingNumberWidget) {
                SettingNumberWidget settingNumberWidget = (SettingNumberWidget) widgets;

                if (settingNumberWidget.getSetting().isEnabled()) {
                    widgets.onCustomRender();
                }
            }
        }
    }
}
