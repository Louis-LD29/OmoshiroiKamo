package louis.omoshiroikamo.common.world;

import java.util.Random;

import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;

import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;

public class OKWorldGenerator implements IWorldGenerator {

    public static final OKWorldGenerator INSTANCE = new OKWorldGenerator();

    public static void init() {
        GameRegistry.registerWorldGenerator(INSTANCE, 0);
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator,
        IChunkProvider chunkProvider) {

    }

    public void generateOre(WorldGenMinable gen, World world, Random random, int chunkX, int chunkZ, float chance,
        int minY, int maxY) {
        if (maxY <= 0 || minY < 0 || maxY < minY || gen.numberOfBlocks <= 0 || chance <= 0) return;

        for (int i = 0; i < (chance < 1 ? 1 : (int) chance); i++) {
            if (chance >= 1 || random.nextFloat() < chance) {
                int xRand = (chunkX << 4) + random.nextInt(16);
                int yRand = MathHelper.getRandomIntegerInRange(random, minY, maxY);
                int zRand = (chunkZ << 4) + random.nextInt(16);

                gen.generate(world, random, xRand, yRand, zRand);
            }
        }
    }

}
