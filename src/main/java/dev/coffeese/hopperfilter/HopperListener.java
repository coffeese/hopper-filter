package dev.coffeese.hopperfilter;

import java.util.Collection;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class HopperListener implements Listener {

    private Logger logger;

    public HopperListener(Logger logger) {
        this.logger = logger;
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onInventoryMoveItem(final InventoryMoveItemEvent e) {
        Inventory inv = e.getDestination();
        if (inv.getType() != InventoryType.HOPPER)
            return;

        InventoryHolder holder = inv.getHolder();
        if (!(holder instanceof Hopper))
            return;

        Block hopper = ((Hopper) holder).getBlock();
        World world = hopper.getWorld();
        Location location = hopper.getLocation();
        int available = 0;
        Collection<Entity> entities = world.getNearbyEntities(location, 2, 2, 2, entity -> entity.getType() == EntityType.ITEM_FRAME);
        for (Entity entity : entities) {
            ItemFrame frame = (ItemFrame) entity;
            Block attachedBlock = getAttachedBlock(frame);
            if (!hopper.equals(attachedBlock))
                continue;
                
            available++;
            ItemStack filteredItem = frame.getItem();
            if (filteredItem.getType().equals(Material.AIR))
                continue;

            ItemStack item = e.getItem();
            if (item.getType().equals(filteredItem.getType()))
                return;
        }
        if (available > 0)
            e.setCancelled(true);
    }

    private Block getAttachedBlock(ItemFrame frame) {
        return frame.getLocation().getBlock().getRelative(frame.getAttachedFace());
    }
}
