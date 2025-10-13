package louis.omoshiroikamo.plugin.structureLib;

import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.structure.AutoPlaceEnvironment;
import com.gtnewhorizon.structurelib.structure.ICustomBlockSetting;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

public class StructureLibUtils {

    public static <T> IStructureElement<T> ofBlockAdderWithPos(IBlockAdderWithPos<T> iBlockAdderWithPos,
        Block defaultBlock, int defaultMeta) {

        if (defaultBlock instanceof ICustomBlockSetting) {
            return new IStructureElement<T>() {

                @Override
                public boolean check(T t, World world, int x, int y, int z) {
                    Block worldBlock = world.getBlock(x, y, z);
                    return iBlockAdderWithPos.apply(t, worldBlock, worldBlock.getDamageValue(world, x, y, z), x, y, z);
                }

                @Override
                public boolean couldBeValid(T t, World world, int x, int y, int z, ItemStack trigger) {
                    // calling ofBlockAdderWithPos can potentially modify external state
                    // therefore we assume this can always be valid.
                    return true;
                }

                @Override
                public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                    ((ICustomBlockSetting) defaultBlock).setBlock(world, x, y, z, defaultMeta);
                    return true;
                }

                @Override
                public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                    StructureLibAPI.hintParticle(world, x, y, z, defaultBlock, defaultMeta);
                    return true;
                }

                @Override
                @Deprecated
                public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
                    AutoPlaceEnvironment env) {
                    if (world.getBlock(x, y, z) == defaultBlock && world.getBlockMetadata(x, y, z) == defaultMeta) {
                        return PlaceResult.SKIP;
                    }
                    return StructureUtility.survivalPlaceBlock(
                        defaultBlock,
                        defaultMeta,
                        world,
                        x,
                        y,
                        z,
                        env.getSource(),
                        env.getActor(),
                        env.getChatter());
                }
            };
        } else {
            return new IStructureElement<T>() {

                @Override
                public boolean check(T t, World world, int x, int y, int z) {
                    Block worldBlock = world.getBlock(x, y, z);
                    return iBlockAdderWithPos.apply(t, worldBlock, worldBlock.getDamageValue(world, x, y, z), x, y, z);
                }

                @Override
                public boolean couldBeValid(T t, World world, int x, int y, int z, ItemStack trigger) {
                    // calling ofBlockAdderWithPos can potentially modify external state
                    // therefore we assume this can always be valid.
                    return true;
                }

                @Override
                public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                    world.setBlock(x, y, z, defaultBlock, defaultMeta, 2);
                    return true;
                }

                @Override
                public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                    StructureLibAPI.hintParticle(world, x, y, z, defaultBlock, defaultMeta);
                    return true;
                }

                @Override
                @Deprecated
                public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
                    AutoPlaceEnvironment env) {
                    if (world.getBlock(x, y, z) == defaultBlock && world.getBlockMetadata(x, y, z) == defaultMeta) {
                        return PlaceResult.SKIP;
                    }
                    return StructureUtility.survivalPlaceBlock(
                        defaultBlock,
                        defaultMeta,
                        world,
                        x,
                        y,
                        z,
                        env.getSource(),
                        env.getActor(),
                        env.getChatter());
                }
            };
        }
    }

    public static <T> IStructureElement<T> ofBlockAdderWithPos(IBlockAdderWithPos<T> iBlockAdderWithPos, int dots) {
        return ofBlockAdderWithPos(iBlockAdderWithPos, StructureLibAPI.getBlockHint(), dots - 1);
    }

    public interface IBlockAdderWithPos<T> {

        boolean apply(T t, Block block, int meta, int x, int y, int z);
    }

    public static class UpgradeEntry {

        public final Block block;
        public final int meta;
        public final int x, y, z;

        public UpgradeEntry(Block block, int meta, int x, int y, int z) {
            this.block = block;
            this.meta = meta;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof UpgradeEntry)) {
                return false;
            }
            UpgradeEntry other = (UpgradeEntry) o;
            return x == other.x && y == other.y && z == other.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }

}
