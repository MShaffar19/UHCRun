package net.zyuiop.uhcrun.generator;

import net.samagames.gameapi.GameAPI;
import net.zyuiop.uhcrun.UHCRun;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;

public class WorldLoader {
    public static BukkitTask task;
    public static int lastShow = -1;
    public static int numberChunk = 0;

    public static void begin(final World world) {
        task = Bukkit.getScheduler().runTaskTimer(UHCRun.instance, new Runnable() {
            private int todo = (1200*1200)/(16*16);
            private int x = -600;
            private int z = -600;

            @Override
            public void run() {
                int i = 0;
                while (i < 50) {
                    world.getChunkAt(world.getBlockAt(x, 64, z)).load(true);
                    int percentage = (numberChunk * 100 / todo);
                    if (percentage > lastShow && percentage % 10 == 0) {
                        lastShow = percentage;
                        GameAPI.getManager().sendArena(GameAPI.getManager().buildJson(GameAPI.getArena(), percentage));
                    }

                    z+=16;
                    if (z >= 600) {
                        z = - 600;
                        x += 16;
                    }

                    if (x >= 600)  {
                        WorldLoader.finish();
                        return;
                    }

                    numberChunk++;
                    i++;
                }
            }
        }, 1L, 1L);
    }

    private static void finish() {
        task.cancel();
        UHCRun.instance.finishGeneration();
    }
}