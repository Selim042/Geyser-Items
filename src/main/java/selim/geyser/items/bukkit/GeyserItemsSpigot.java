package selim.geyser.items.bukkit;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_12_R1.Item;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import selim.geyser.core.bukkit.network.NetworkHandler;
import selim.geyser.core.shared.EnumComponent;
import selim.geyser.core.shared.IGeyserCorePlugin;
import selim.geyser.core.shared.IGeyserPlugin;
import selim.geyser.items.shared.GeyserItemsInfo;

public class GeyserItemsSpigot extends JavaPlugin implements Listener, IGeyserCorePlugin, IGeyserPlugin {

	public static Logger LOGGER;
	public static GeyserItemsSpigot INSTANCE;
	public static NetworkHandler NETWORK;
	public static final Item ITEM_GEYSER_ITEM;
	public static final Material ENUM_GEYSER_ITEM;
	// private static boolean newItem = false;
	// private static boolean newMaterial = false;

	static {
		if (!Item.REGISTRY.d(new MinecraftKey(GeyserItemsInfo.ID, "geyser_item"))) {
			ITEM_GEYSER_ITEM = new ItemGeyserItem();
			Item.REGISTRY.a(GeyserItemsInfo.ITEM_ID, new MinecraftKey(GeyserItemsInfo.ID, "geyser_item"),
					ITEM_GEYSER_ITEM);
			// newItem = true;
		} else
			ITEM_GEYSER_ITEM = Item.REGISTRY.get(new MinecraftKey(GeyserItemsInfo.ID, "geyser_item"));
		Material item = null;
		try {
			item = Material.valueOf("GEYSER_ITEM");
		} catch (IllegalArgumentException e) {}
		if (item == null) {
			ENUM_GEYSER_ITEM = EnumHelperBukkit.addEnum(Material.class, "GEYSER_ITEM",
					new Class<?>[] { int.class }, GeyserItemsInfo.ITEM_ID);
			// newMaterial = true;
		} else
			ENUM_GEYSER_ITEM = item;
	}

	@Override
	public void onEnable() {
		LOGGER = this.getLogger();
		INSTANCE = this;

		// if (newItem)
		// System.out.println("registering new item");
		// else
		// System.out.println("reusing old item");
		// if (newMaterial)
		// System.out.println("registering new material");
		// else
		// System.out.println("reusing old material");

		NETWORK = NetworkHandler.registerChannel(this, GeyserItemsInfo.CHANNEL);

		PluginManager manager = this.getServer().getPluginManager();
		manager.registerEvents(this, this);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		event.getPlayer().sendMessage(event.getMaterial().name());
		// event.getPlayer().sendMessage(CraftItemStack.asNMSCopy(event.getItem()).toString());
		event.getPlayer().sendMessage("id: " + event.getMaterial().getId());
		event.getPlayer().sendMessage(
				"isItem: " + ENUM_GEYSER_ITEM.isItem() + ", isBlock: " + ENUM_GEYSER_ITEM.isBlock());
	}

	private int getPing(Player player) {
		try {
			Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
			return (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | NoSuchFieldException e) {
			this.getLogger().log(Level.INFO, "Unable to get ping for " + player.getDisplayName()
					+ ", encountered a " + e.getClass().getName());
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public EnumComponent[] providedComponents() {
		return new EnumComponent[] { EnumComponent.ITEMS };
	}

	@Override
	public EnumComponent[] requiredComponents() {
		return new EnumComponent[] { EnumComponent.RESOURCES, EnumComponent.TRANSLATIONS };
	}

	@Override
	public boolean requiredOnClient(EnumComponent component) {
		return component == EnumComponent.ITEMS;
	}

}
