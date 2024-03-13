package ca.awoo.deathchest;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DeathListener implements Listener{
    
    private final DeathChest plugin;

    public DeathListener(DeathChest plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        //Copy event.getDrop() to a new list and clear the original list
        List<ItemStack> drops = new ArrayList<>(event.getDrops());
        Location deathLoc = event.getEntity().getLocation();
        World world = deathLoc.getWorld();
        event.getDrops().clear();
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Pair<Location, Location> locations = getFreeSpaceForChest(deathLoc);
            world.getBlockAt(locations.getFirst()).setType(Material.CHEST);
            world.getBlockAt(locations.getSecond()).setType(Material.CHEST);
            Inventory inv1 = ((Chest)world.getBlockAt(locations.getFirst()).getState()).getInventory();
            Inventory inv2 = ((Chest)world.getBlockAt(locations.getSecond()).getState()).getInventory();
            for(int i = 0; i < drops.size(); i++){
                if(i < inv1.getSize()){
                    inv1.setItem(i, drops.get(i));
                }else{
                    inv2.setItem(i - inv1.getSize(), drops.get(i));
                }
            }
        }, 1L);
    }

    private Pair<Location, Location> getFreeSpaceForChest(Location deathLoc){
        BlockFace[] faces = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
        while(true){
            while(deathLoc.getBlock().getType().isSolid() || deathLoc.getWorld().getMinHeight() > deathLoc.getBlockY()){
                deathLoc.add(0, 1, 0);
            }
            for(BlockFace face : faces){
                if(!deathLoc.getBlock().getRelative(face).getType().isSolid()){
                    return new Pair<Location, Location>(deathLoc, deathLoc.getBlock().getRelative(face).getLocation());
                }
            }
        }
    }
}
