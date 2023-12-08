package top.mrxiaom.example.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import top.mrxiaom.example.ExamplePlugin;

public interface IGui {
	Player getPlayer();
	Inventory newInventory();
	void onClick(InventoryAction action, ClickType click, InventoryType.SlotType slotType, int slot, ItemStack currentItem, ItemStack cursor, InventoryView view, InventoryClickEvent event);
	void onDrag(InventoryView view, InventoryDragEvent event);
	void onClose(InventoryView view);
	default void refresh(){
		ExamplePlugin.getInstance().getGuiManager().openGui(this);
	}
}
