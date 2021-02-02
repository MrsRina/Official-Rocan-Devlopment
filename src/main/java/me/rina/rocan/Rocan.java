package me.rina.rocan;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.rina.rocan.api.command.management.CommandManager;
import me.rina.rocan.api.event.management.EventManager;
import me.rina.rocan.api.module.management.ModuleManager;
import me.rina.rocan.api.preset.Preset;
import me.rina.rocan.api.preset.management.PresetManager;
import me.rina.rocan.api.social.management.SocialManager;
import me.rina.rocan.client.command.CommandPrefix;
import me.rina.rocan.client.command.CommandToggle;
import me.rina.rocan.client.gui.GUI;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.module.misc.ModuleAutoFish;
import me.rina.rocan.client.module.misc.ModuleAutoRespawn;
import me.rina.rocan.client.module.render.ModuleBlockHighlight;
import me.rina.rocan.client.module.render.ModuleHoleESP;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * @author SrRina
 * @since 15/11/20 at 4:51pm
 */
public enum Rocan {
    INSTANCE;

    public static final String NAME        = "Rocan";
    public static final String VERSION     = "0.0.1";
    public static final String PATH_CONFIG = "/Rocan/";
    public static final String CHAT        = ChatFormatting.GOLD + "/* Rocan */ ";

    /*
     * We create one final Minecraft, there is the function Minecraft or this variable;
     */
    public static final Minecraft MC = Minecraft.getMinecraft();

    /*
     * Yoink Event Manager;
     */
    private cat.yoink.eventmanager.EventManager eventManager;

    /* All managers of the client. */
    private ModuleManager moduleManager;
    private EventManager clientEventManager;
    private CommandManager commandManager;
    private SocialManager socialManager;
    private PresetManager presetManager;

    private ModuleClickGUI moduleClickGUI;
    private GUI wrapperGUI;

    /**
     * Registry all components.
     */
    public void onRegistry() {
        // Category Client.
        this.moduleManager.registry(new me.rina.rocan.client.module.client.ModuleClickGUI());

        // Category Render.
        this.moduleManager.registry(new ModuleBlockHighlight());
        this.moduleManager.registry(new ModuleHoleESP());

        // Category Misc.
        this.moduleManager.registry(new ModuleAutoRespawn());
        this.moduleManager.registry(new ModuleAutoFish());

        // Commands.
        this.commandManager.registry(new CommandPrefix());
        this.commandManager.registry(new CommandToggle());
    }

    /**
     * Method non-static to init the client.
     */
    public void onInitClient() {
        this.presetManager.onLoad();

        // We start here the GUI, cause, all settings and modules are loaded.
        this.moduleClickGUI = new ModuleClickGUI();
        this.moduleClickGUI.init();

        // Reload the current preset or nothing.
        PresetManager.reload();
    }

    /**
     * Method static to end client, save or disable something.
     */
    public static void onEndClient() {
        // Finish the preset saving all.
        PresetManager.finish();
    }

    public void onClientStarted(FMLInitializationEvent event) {
        this.eventManager = new cat.yoink.eventmanager.EventManager();

        this.moduleManager = new ModuleManager();
        this.clientEventManager = new EventManager();
        this.commandManager = new CommandManager();
        this.socialManager = new SocialManager();
        this.presetManager = new PresetManager();

        this.wrapperGUI = new GUI();

        MinecraftForge.EVENT_BUS.register(this.clientEventManager);
        MinecraftForge.EVENT_BUS.register(this.commandManager);

        this.onRegistry();
        this.onInitClient();
    }

    public static ModuleManager getModuleManager() {
        return INSTANCE.moduleManager;
    }

    public static EventManager getClientEventManager() {
        return INSTANCE.clientEventManager;
    }

    public static CommandManager getCommandManager() {
        return INSTANCE.commandManager;
    }

    public static ModuleClickGUI getModuleClickGUI() {
        return INSTANCE.moduleClickGUI;
    }

    public static SocialManager getSocialManager() {
        return INSTANCE.socialManager;
    }

    public static GUI getWrapperGUI() {
        return INSTANCE.wrapperGUI;
    }

    public static Minecraft getMinecraft() {
        return MC;
    }

    public static cat.yoink.eventmanager.EventManager getEventManager() {
        return INSTANCE.eventManager;
    }
}