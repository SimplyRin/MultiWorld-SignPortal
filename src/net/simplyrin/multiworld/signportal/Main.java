package net.simplyrin.multiworld.signportal;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by SimplyRin on 2018/04/23
 *
 * MIT License
 *
 * Copyright (c) 2018 SimplyRin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class Main extends JavaPlugin implements Listener {

	private static Main instance;

	@Override
	public void onEnable() {
		instance = this;
		instance.getServer().getPluginManager().registerEvents(instance, instance);
	}

	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		Player player = event.getPlayer();

		if(!player.hasPermission("multiworld.signportal")) {
			return;
		}

		String prefix = ChatColor.translateAlternateColorCodes('&', event.getLine(1));

		if(prefix.equalsIgnoreCase("[MW]") || prefix.equalsIgnoreCase("[MultiWorld]")) {
			prefix = prefix.replace("[mw]", "§9[§4MW§9]");
			prefix = prefix.replace("[MW]", "§9[§4MW§9]");
			prefix = prefix.replace("[multiworld]", "§9[§4MultiWorld§9]");
			prefix = prefix.replace("[MultiWorld]", "§9[§4MultiWorld§9]");
			event.setLine(1, prefix);
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action action = event.getAction();
		Block block = event.getClickedBlock();

		if(block == null) {
			return;
		}

		if(action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.LEFT_CLICK_BLOCK)) {
			if(block.getState() == null) {
				return;
			}

			if(!(block.getState() instanceof Sign)) {
				return;
			}

			Sign sign = (Sign) block.getState();

			if(sign.getLine(1).equals("§9[§4MW§9]") || sign.getLine(1).equals("§9[§4MultiWorld§9]")) {
				World world = instance.getServer().getWorld(sign.getLine(2));
				Location location;
				try {
					location = new Location(world, world.getSpawnLocation().getX(), world.getSpawnLocation().getY(), world.getSpawnLocation().getZ());;
				} catch (Exception e) {
					player.sendMessage("§7[§cMultiWorld-SignPortal§7] §cThis world does not exist!");
					return;
				}
				instance.getServer().getScheduler().runTask(instance, new Runnable() {
					@Override
					public void run() {
						player.sendMessage("§7[§cMultiWorld-SignPortal§7] §aTeleported to §b" + world.getName() + "§a!");
						player.teleport(location);
					}
				});
			}
		}

	}

}
