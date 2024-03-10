package ca.awoo.deathchest;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;

public class DeathListener implements Listener{
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Pair<Location, Location> locations = getFreeSpaceForChest(event.getEntity().getLocation());
        event.getEntity().getWorld().getBlockAt(locations.getFirst()).setType(Material.CHEST);
        event.getEntity().getWorld().getBlockAt(locations.getSecond()).setType(Material.CHEST);
        Inventory inv1 = ((Chest)event.getEntity().getWorld().getBlockAt(locations.getFirst()).getState()).getInventory();
        Inventory inv2 = ((Chest)event.getEntity().getWorld().getBlockAt(locations.getSecond()).getState()).getInventory();
        for(int i = 0; i < event.getDrops().size(); i++){
            if(i < inv1.getSize()){
                inv1.setItem(i, event.getDrops().get(i));
            }else{
                inv2.setItem(i - inv1.getSize(), event.getDrops().get(i));
            }
        }
        event.getDrops().clear();
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
