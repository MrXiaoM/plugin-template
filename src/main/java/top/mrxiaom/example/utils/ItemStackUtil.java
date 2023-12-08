package top.mrxiaom.example.utils;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;

import static top.mrxiaom.example.func.AbstractPluginHolder.t;

@SuppressWarnings({"unused", "deprecation"})
public class ItemStackUtil {

	private final ItemStack itemStack;
	private ItemMeta itemMeta;

	public ItemStackUtil(Material material, int amount) {
		itemStack = new ItemStack(material, amount);
		itemMeta = itemStack.getItemMeta();
	}

	public ItemStackUtil(ItemStack itemStack) {
		this.itemStack = new ItemStack(itemStack);
		itemMeta = itemStack.getItemMeta();
	}

	public ItemStackUtil(Material material) {
		this(material, 1);
		itemMeta = itemStack.getItemMeta();
	}

	public ItemStackUtil name(String displayName) {

		if (displayName == null)
			return this;

		itemMeta.setDisplayName(ColorHelper.parseColor( displayName));
		itemStack.setItemMeta(itemMeta);

		return this;
	}

	public ItemStackUtil lore(String... lore) {

		List<String> itemLore = new ArrayList<>();
		for(String s : lore) {
			itemLore.add(ColorHelper.parseColor( s));
		}
		itemMeta.setLore(itemLore);
		itemStack.setItemMeta(itemMeta);

		return this;
	}

	public ItemStackUtil lore(List<String> lore) {
		List<String> itemLore = new ArrayList<>();
		for(String s : lore) {
			itemLore.add(ColorHelper.parseColor( s));
		}
		itemMeta.setLore(itemLore);
		itemStack.setItemMeta(itemMeta);

		return this;
	}

	public ItemStackUtil addLore(String... lore) {

		List<String> itemLore = itemMeta.getLore();
		if (itemLore == null)
			return this;
		for(String s : lore) {
			itemLore.add(ColorHelper.parseColor( s));
		}

		itemMeta.setLore(itemLore);
		itemStack.setItemMeta(itemMeta);
		return this;
	}

	public ItemStackUtil addLore(List<String> lore) {

		List<String> itemLore = itemMeta.getLore();
		if (itemLore == null)
			return this;

		for(String s : lore) {
			itemLore.add(ColorHelper.parseColor( s));
		}

		itemMeta.setLore(itemLore);
		itemStack.setItemMeta(itemMeta);
		return this;
	}

	public ItemStackUtil addEnchantment(Enchantment enchantment, int level) {
		itemStack.addUnsafeEnchantment(enchantment, level);
		return this;
	}

	public ItemStackUtil addFlag(ItemFlag itemFlag) {
		itemMeta.addItemFlags(itemFlag);
		itemStack.setItemMeta(itemMeta);
		return this;
	}

	public ItemStackUtil setUnbreakable() {

		itemMeta.setUnbreakable(true);

		itemStack.setItemMeta(itemMeta);

		return this;
	}

	public ItemStack build() {
		return itemStack;
	}

	public static String itemStackArrayToBase64(ItemStack[] var1) {
		return itemStackArrayToBase64(var1, false);
	}

	public static String itemStackArrayToBase64(ItemStack[] var1, boolean ignoreException) {
		try {
			ByteArrayOutputStream var2 = new ByteArrayOutputStream();
			BukkitObjectOutputStream var3 = new BukkitObjectOutputStream(var2);
			var3.writeInt(var1.length);
			int var5 = var1.length;

			for (ItemStack var7 : var1) {
				var3.writeObject(var7);
			}

			var3.close();
			return Base64Coder.encodeLines(var2.toByteArray());
		} catch (Throwable t) {
			if (!ignoreException)
				t.printStackTrace();
			return "";
		}
	}

	public static ItemStack[] itemStackArrayFromBase64(String var1) {
		return itemStackArrayFromBase64(var1, false);
	}

	public static ItemStack[] itemStackArrayFromBase64(String var1, boolean ignoreException) {
		if (var1.isEmpty() || var1.trim().equalsIgnoreCase(""))
			return new ItemStack[0];
		try {
			ByteArrayInputStream var2 = new ByteArrayInputStream(Base64Coder.decodeLines(var1));
			BukkitObjectInputStream var3 = new BukkitObjectInputStream(var2);
			ItemStack[] var4 = new ItemStack[var3.readInt()];

			for (int var5 = 0; var5 < var4.length; ++var5) {
				var4[var5] = (ItemStack) var3.readObject();
			}

			var3.close();
			return var4;
		} catch (Throwable t) {
			if (!ignoreException)
				t.printStackTrace();
			return new ItemStack[0];
		}
	}

	public static String b64encode(String s) {
		return Arrays.toString(Base64.getEncoder().encode(s.getBytes()));
	}

	public static String b64decode(String s) {
		return Arrays.toString(Base64.getDecoder().decode(s.getBytes()));
	}

	public static boolean isHelmetFastBurn(ItemStack itemStack) {
		if ((itemStack == null)) return false;

		return itemStack.getType().equals(Material.LEATHER_HELMET)
				|| itemStack.getType().equals(Material.CHAINMAIL_HELMET)
				|| itemStack.getType().equals(Material.IRON_HELMET);
	}

	public static boolean isDisplayNameContains(ItemStack item, String value) {
		return getItemDisplayName(item).contains(value);
	}

	public static boolean isHelmet(ItemStack itemStack) {
		if ((itemStack == null)) return false;

		return itemStack.getType().equals(Material.LEATHER_HELMET)
				|| itemStack.getType().equals(Material.CHAINMAIL_HELMET)
				|| itemStack.getType().equals(Material.IRON_HELMET)
				|| itemStack.getType().equals(Material.GOLDEN_HELMET)
				|| itemStack.getType().equals(Material.DIAMOND_HELMET)
				|| itemStack.getType().equals(Material.TURTLE_HELMET);
	}

	public static boolean hasHelmet(Player player) {
		return isHelmet(player.getInventory().getHelmet());
	}

	public static boolean isBlockAntiSun(Material m) {
		if (m == null)
			return true;

		return !m.equals(Material.AIR) && !m.equals(Material.GLASS) && !m.equals(Material.GLASS_PANE)
				&& !m.equals(Material.BLACK_STAINED_GLASS) && !m.equals(Material.BLUE_STAINED_GLASS)
				&& !m.equals(Material.BROWN_STAINED_GLASS) && !m.equals(Material.CYAN_STAINED_GLASS)
				&& !m.equals(Material.GRAY_STAINED_GLASS) && !m.equals(Material.GREEN_STAINED_GLASS)
				&& !m.equals(Material.LIME_STAINED_GLASS) && !m.equals(Material.LIGHT_BLUE_STAINED_GLASS)
				&& !m.equals(Material.LIGHT_GRAY_STAINED_GLASS) && !m.equals(Material.MAGENTA_STAINED_GLASS)
				&& !m.equals(Material.ORANGE_STAINED_GLASS) && !m.equals(Material.PINK_STAINED_GLASS)
				&& !m.equals(Material.PURPLE_STAINED_GLASS) && !m.equals(Material.RED_STAINED_GLASS)
				&& !m.equals(Material.WHITE_STAINED_GLASS) && !m.equals(Material.YELLOW_STAINED_GLASS)
				&& !m.equals(Material.BLACK_STAINED_GLASS_PANE) && !m.equals(Material.BLUE_STAINED_GLASS_PANE)
				&& !m.equals(Material.BROWN_STAINED_GLASS_PANE) && !m.equals(Material.CYAN_STAINED_GLASS_PANE)
				&& !m.equals(Material.GRAY_STAINED_GLASS_PANE) && !m.equals(Material.GREEN_STAINED_GLASS_PANE)
				&& !m.equals(Material.LIME_STAINED_GLASS_PANE) && !m.equals(Material.LIGHT_BLUE_STAINED_GLASS_PANE)
				&& !m.equals(Material.LIGHT_GRAY_STAINED_GLASS_PANE) && !m.equals(Material.MAGENTA_STAINED_GLASS_PANE)
				&& !m.equals(Material.ORANGE_STAINED_GLASS_PANE) && !m.equals(Material.PINK_STAINED_GLASS_PANE)
				&& !m.equals(Material.PURPLE_STAINED_GLASS_PANE) && !m.equals(Material.RED_STAINED_GLASS_PANE)
				&& !m.equals(Material.WHITE_STAINED_GLASS_PANE) && !m.equals(Material.YELLOW_STAINED_GLASS_PANE);
	}

	public static String getItemDisplayName(ItemStack item) {
		if ((item == null) || !item.hasItemMeta() || item.getItemMeta() == null)
			return item != null ? item.getType().name() : "";
		return item.getItemMeta().getDisplayName();
	}

	public static List<String> getItemLore(ItemStack item) {
		if ((item == null) || !item.hasItemMeta() || item.getItemMeta() == null
				|| (item.getItemMeta().getLore() == null))
			return new ArrayList<>();
		return item.getItemMeta().getLore();
	}

	public static void reduceItemInMainHand(Player player) {
		reduceItemInMainHand(player, 1);
	}

	public static void reduceItemInMainHand(Player player, int amount) {
		ItemStack im = player.getInventory().getItemInMainHand();
		if (im.getAmount() - amount > 0) {
			im.setAmount(im.getAmount() - amount);
			player.getInventory().setItemInMainHand(im);
		} else {
			player.getInventory().setItemInMainHand(null);
		}
	}

	public static void reduceItemInOffHand(Player player) {
		reduceItemInOffHand(player, 1);
	}

	public static void reduceItemInOffHand(Player player, int amount) {
		ItemStack im = player.getInventory().getItemInOffHand();
		if (im.getAmount() - amount > 0) {
			im.setAmount(im.getAmount() - amount);
			player.getInventory().setItemInOffHand(im);
		} else {
			player.getInventory().setItemInOffHand(null);
		}
	}

	public static boolean hasLore(ItemStack item, String lore) {
		if (item == null || item.getItemMeta() == null || item.getItemMeta().getLore() == null
				|| item.getItemMeta().getLore().isEmpty())
			return false;
		return item.getItemMeta().getLore().contains(ColorHelper.parseColor( lore));
	}

	public static boolean containsLore(ItemStack item, String lore) {
		if (item == null || item.getItemMeta() == null || item.getItemMeta().getLore() == null
				|| item.getItemMeta().getLore().isEmpty())
			return false;
		for (String s : item.getItemMeta().getLore()) {
			if (s.contains(ColorHelper.parseColor( lore))) {
				return true;
			}
		}
		return false;
	}

	public static boolean containsLoreIgnoreCase(ItemStack item, String lore) {
		if (item == null || item.getItemMeta() == null || item.getItemMeta().getLore() == null
				|| item.getItemMeta().getLore().isEmpty())
			return false;
		for (String s : item.getItemMeta().getLore()) {
			if (s.toLowerCase().contains(ColorHelper.parseColor( lore).toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	public static void setItemDisplayName(ItemStack item, String name) {
		if (item == null)
			return;
		ItemMeta im = item.getItemMeta() == null ? getItemMeta(item.getType()) : item.getItemMeta();
		if (im == null)
			return;
		im.setDisplayName(ColorHelper.parseColor( name));
		item.setItemMeta(im);
	}

	public static void setItemLore(ItemStack item, String... lore){
		setItemLore(item, Lists.newArrayList(lore));
	}

	public static void setItemLore(ItemStack item, List<String> lore) {
		if (item == null)
			return;
		ItemMeta im = item.getItemMeta() == null ? getItemMeta(item.getType()) : item.getItemMeta();
		if (im == null)
			return;
		List<String> newLore = new ArrayList<>();
		lore.forEach(s-> newLore.add(ColorHelper.parseColor( s)));
		im.setLore(newLore);
		item.setItemMeta(im);
	}

	private static final Integer[] frameSmallSlots = new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
			/* 10, 11, 12, 13, 14, 15, 16, */17, 18, 19, 20, 21, 22, 23, 24, 25, 26 };

	public static void setFrameItemsSmall(Inventory inv, ItemStack item) {
		for (int slot : frameSmallSlots) {
			inv.setItem(slot, item);
		}
	}

	public static ItemStack buildFrameItem(Material material) {
		return buildItem(material, "&f&l*", Lists.newArrayList());
	}

	public static ItemStack buildItem(Material material, String name) {
		return buildItem(material, name, Lists.newArrayList());
	}

	public static ItemStack buildItem(Material material, String name, List<String> lore) {
		ItemStack item = new ItemStack(material, 1);
		ItemMeta im = getItemMeta(material);
		im.setDisplayName(ColorHelper.parseColor(name));
		if (!lore.isEmpty()) {
			List<String> l = new ArrayList<>();
			for (String s : lore) {
				l.add(ColorHelper.parseColor( s));
			}
			im.setLore(l);
		}
		item.setItemMeta(im);
		return item;
	}

	@NotNull
	public static ItemMeta getItemMeta(@NotNull ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		return meta == null ? getItemMeta(item.getType()) : meta;
	}
	@NotNull
	public static ItemMeta getItemMeta(Material material) {
		return Bukkit.getItemFactory().getItemMeta(material);
	}

	public static ItemStack buildItem(Material material, String name, String... lore) {
		return buildItem(material, name, Lists.newArrayList(lore));
	}

	private static final Integer[] frameSlots54 = new Integer[] {
			0, 1, 2, 3, 4, 5, 6, 7, 8,
			9,/* 10, 11, 12, 13, 14, 15, 16, */17,
			18, /* 19, 20, 21, 22, 23, 24, 25, */26,
			27,/* 28, 29, 30, 31, 32, 33, 34, */35,
			36, /* 37, 38, 39, 40, 41, 42, 43, */44,
			45, 46, 47, 48, 49, 50, 51, 52, 53
	};
	private static final Integer[] frameSlots45 = new Integer[] {
			0, 1, 2, 3, 4, 5, 6, 7, 8,
			9,/* 10, 11, 12, 13, 14, 15, 16, */17,
			18, /* 19, 20, 21, 22, 23, 24, 25, */26,
			27,/* 28, 29, 30, 31, 32, 33, 34, */35,
			36, 37, 38, 39, 40, 41, 42, 43, 44
	};
	private static final Integer[] frameSlots36 = new Integer[] {
			0, 1, 2, 3, 4, 5, 6, 7, 8,
			9,/* 10, 11, 12, 13, 14, 15, 16, */17,
			18, /* 19, 20, 21, 22, 23, 24, 25, */26,
			27, 28, 29, 30, 31, 32, 33, 34, 35
	};
	private static final Integer[] frameSlots27 = new Integer[] {
			0, 1, 2, 3, 4, 5, 6, 7, 8,
			9,/* 10, 11, 12, 13, 14, 15, 16, */17,
			18, 19, 20, 21, 22, 23, 24, 25, 26
	};

	public static void setFrameItems(Inventory inv, ItemStack item) {
		Integer[] frameSlots;
		switch (inv.getSize()) {
			case 27:
				frameSlots = frameSlots27;
				break;
			case 36:
				frameSlots = frameSlots36;
				break;
			case 45:
				frameSlots = frameSlots45;
				break;
			case 54:
				frameSlots = frameSlots54;
				break;
			default:
				return;
		}
		for (int slot : frameSlots) {
			inv.setItem(slot, item);
		}
	}

	public static void setRowItems(Inventory inv, int row, ItemStack item) {
		for (int i = 0; i < 9; i++) {
			inv.setItem(((row - 1) * 9) + i, item);
		}
	}

	public static ItemStack getEnchantedBook(Enchantment ench, int level) {
		Map<Enchantment, Integer> map = new HashMap<>();
		map.put(ench, level);
		return getEnchantedBook(map);
	}

	public static ItemStack getEnchantedBook(Map<Enchantment, Integer> map) {
		ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
		ItemMeta im = item.getItemMeta();
		if (im == null)
			return item;
		for (Enchantment ench : map.keySet()) {
			((org.bukkit.inventory.meta.EnchantmentStorageMeta) im).addStoredEnchant(ench, map.get(ench), true);
		}
		item.setItemMeta(im);
		return item;
	}
	public static void giveItemToPlayer(final Player player, final List<ItemStack> items) {
		giveItemToPlayer(player, items.toArray(ItemStack[]::new));
	}
	public static void giveItemToPlayer(final Player player, final ItemStack... items) {
		giveItemToPlayer(player, "", "", items);
	}
	public static void giveItemToPlayer(final Player player, final String msg, final String msgFull,
			final List<ItemStack> items) {
		giveItemToPlayer(player, msg, msgFull, items.toArray(ItemStack[]::new));
	}
	public static void giveItemToPlayer(final Player player, final String msg, final String msgFull,
			final ItemStack... items) {
		final Collection<ItemStack> last = player.getInventory().addItem(items).values();
		if (msg.length() > 0 || (!last.isEmpty() && msgFull.length() > 0)) {
			t(player, msg + (last.isEmpty() ? "" : ("\n&r" + msgFull)));
		}
		for (final ItemStack item : last) {
			player.getWorld().dropItem(player.getLocation(), item);
		}
	}

	public static boolean isSign(Material m) {
		return isStandSign(m) || isWallSign(m);
	}

	public static boolean isStandSign(Material m) {
		return m.equals(Material.ACACIA_SIGN) || m.equals(Material.BIRCH_SIGN) || m.equals(Material.DARK_OAK_SIGN)
				|| m.equals(Material.JUNGLE_SIGN) || m.equals(Material.OAK_SIGN) || m.equals(Material.SPRUCE_SIGN);
	}

	public static boolean isWallSign(Material m) {
		return m.equals(Material.ACACIA_WALL_SIGN) || m.equals(Material.BIRCH_WALL_SIGN)
				|| m.equals(Material.DARK_OAK_WALL_SIGN) || m.equals(Material.JUNGLE_WALL_SIGN)
				|| m.equals(Material.OAK_WALL_SIGN) || m.equals(Material.SPRUCE_WALL_SIGN);
	}
}
