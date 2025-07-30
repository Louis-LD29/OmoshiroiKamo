package louis.omoshiroikamo.common.fluid;


import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;

import java.util.HashMap;
import java.util.Map;

public class BucketHandler {

    public static BucketHandler instance = new BucketHandler();

    static {
        MinecraftForge.EVENT_BUS.register(instance);
    }

    private Map<Block, Item> buckets = new HashMap<Block, Item>();

    private BucketHandler() {}

    public void registerFluid(Block fluidBlock, Item fullBucket) {
        buckets.put(fluidBlock, fullBucket);
    }

    @SubscribeEvent
    public void onBucketFill(FillBucketEvent event) {
        // no instanceof check, someone may subclass the vanilla bucket
        if (event.current != null && event.current.getItem() == Items.bucket && event.current.stackSize > 0) {
            ItemStack res = getFilledBucket(event.world, event.target);
            if (res != null) {
                event.result = res;
                event.setResult(Event.Result.ALLOW);
            }
        }
    }

    private ItemStack getFilledBucket(World world, MovingObjectPosition pos) {

        Block block = world.getBlock(pos.blockX, pos.blockY, pos.blockZ);
        Item bucket = buckets.get(block);
        if (bucket != null && world.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ) == 0) {
            world.setBlockToAir(pos.blockX, pos.blockY, pos.blockZ);
            return new ItemStack(bucket);
        } else {
            return null;
        }
    }

}

