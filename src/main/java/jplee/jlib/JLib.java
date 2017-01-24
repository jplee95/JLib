package jplee.jlib;

import java.util.Set;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.ImmutableSet;

import jplee.jlib.server.network.PacketHandler;
import jplee.jlib.util.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

//@Mod(modid=JLib.MODID, name=JLib.NAME, version=JLib.VERSION, dependencies=JLib.DEPENDENCIES,
//	 acceptedMinecraftVersions=JLib.MINECRAFT_VERSION, useMetadata=false)
public class JLib {
	public static final String NAME = "JLib";
	public static final String MODID = "jlib";
	public static final String VERSION = "1.0.0";
	public static final String DEPENDENCIES = "required-after:Forge@[12.18.1.2099,)";
	public static final String MINECRAFT_VERSION = "[1.10.2]";
	
	public static final String CHUNK_REPLACE_TAG = "wmReplace";
	public static final String PLAYER_START_TAG = "wmStart";
	
	@Mod.Instance
	public static JLib instance;
	
	public static final Log logger = new Log();
	
	public static final PacketHandler packet = new PacketHandler(MODID);
	
	public static final Set<KeyBinding> keyBindings = ImmutableSet.<KeyBinding>builder()
		.add(new KeyBinding("key.testing.desc", Keyboard.KEY_Z, "key.jlib.category"))
		.build();
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger.attachLogger(event.getModLog());
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		if(event.getSide() == Side.CLIENT) {
			keyBindings.forEach(ClientRegistry::registerKeyBinding);
		}
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
	}
	
	@Mod.EventHandler
	public void onServerStart(FMLServerAboutToStartEvent event) {
		
	}
	
	@SubscribeEvent(receiveCanceled=true)
	public void onKeyInput(KeyInputEvent event) {
		keyBindings.forEach(key -> {
			if(key.isPressed()) {
				if(key.getKeyCode() == Keyboard.KEY_Z) {
					Minecraft mc = Minecraft.getMinecraft();
					BlockPos pos = mc.thePlayer.getPosition();
					mc.thePlayer.openGui(JLib.instance, 0, mc.theWorld, pos.getX(), pos.getY(), pos.getZ());
				}
			}
		});
	}
}
