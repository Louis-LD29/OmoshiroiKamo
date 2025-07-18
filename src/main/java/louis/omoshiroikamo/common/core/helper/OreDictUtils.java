package louis.omoshiroikamo.common.core.helper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import codechicken.nei.NEIServerUtils;
import cpw.mods.fml.common.registry.GameRegistry;

public class OreDictUtils {

    public static boolean isOreDictMatch(ItemStack a, ItemStack b) {
        if (a == null || b == null) return false;

        int[] idsA = OreDictionary.getOreIDs(a);
        int[] idsB = OreDictionary.getOreIDs(b);

        for (int idA : idsA) {
            for (int idB : idsB) {
                if (idA == idB) return true;
            }
        }

        return NEIServerUtils.areStacksSameTypeCrafting(a, b);
    }

    public static ItemStack getOreDictRepresentative(ItemStack stack) {
        if (stack == null) return null;

        int[] oreIDs = OreDictionary.getOreIDs(stack);
        if (oreIDs.length == 0) return stack;

        String oreName = OreDictionary.getOreName(oreIDs[0]);
        List<ItemStack> ores = OreDictionary.getOres(oreName);
        for (ItemStack candidate : ores) {
            if (candidate != null && candidate.getItem() != null) {
                ItemStack rep = candidate.copy();
                rep.stackSize = stack.stackSize;
                return rep;
            }
        }
        return stack;
    }

    public static void registerOreDictConversionToOreDict(ItemStack from, String oreDictName) {
        List<ItemStack> oreDictStacks = OreDictionary.getOres(oreDictName);

        if (oreDictStacks.isEmpty()) {
            return;
        }

        boolean fromBelongs = false;
        for (ItemStack candidate : oreDictStacks) {
            if (OreDictUtils.isOreDictMatch(candidate, from)) {
                fromBelongs = true;
                break;
            }
        }

        if (!fromBelongs) {
            return;
        }

        ItemStack to = oreDictStacks.get(0)
            .copy();

        if (ItemStack.areItemStacksEqual(from, to) && ItemStack.areItemStackTagsEqual(from, to)) {
            return;
        }

        GameRegistry.addShapedRecipe(from, "   ", " N ", "   ", 'N', to);
        GameRegistry.addShapedRecipe(to, "   ", " N ", "   ", 'N', from);
    }

    private static Set<String> getOreDictNames(ItemStack stack) {
        Set<String> result = new HashSet<>();
        for (int id : OreDictionary.getOreIDs(stack)) {
            result.add(OreDictionary.getOreName(id));
        }
        return result;
    }

}
