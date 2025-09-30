package louis.omoshiroikamo.common.recipes;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class NBTShapedOreRecipe extends ShapedOreRecipe {

    private final Set<ItemStack> allowedSources = new HashSet<>();
    private final Set<String> allowedTags = new HashSet<>();
    private final Set<String> excludedTags = new HashSet<>();

    private boolean allowAllTags = false;
    private boolean allowAllExcept = false;

    public NBTShapedOreRecipe(ItemStack result, Object... recipe) {
        super(result, recipe);
    }

    public NBTShapedOreRecipe allowNBTFrom(ItemStack... stacks) {
        for (ItemStack stack : stacks) {
            if (stack != null) {
                allowedSources.add(stack.copy());
            }
        }
        return this;
    }

    public NBTShapedOreRecipe allowTags(String... tags) {
        Collections.addAll(this.allowedTags, tags);
        this.allowAllTags = false;
        this.allowAllExcept = false;
        return this;
    }

    public NBTShapedOreRecipe allowAllTags() {
        this.allowAllTags = true;
        this.allowAllExcept = false;
        return this;
    }

    public NBTShapedOreRecipe allowAllExceptTags(String... tags) {
        Collections.addAll(this.excludedTags, tags);
        this.allowAllTags = false;
        this.allowAllExcept = true;
        return this;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting crafting) {
        ItemStack result = super.getCraftingResult(crafting);
        if (result == null) {
            return null;
        }

        for (int i = 0; i < crafting.getSizeInventory(); i++) {
            ItemStack input = crafting.getStackInSlot(i);
            if (input != null && input.hasTagCompound() && matchesAllowed(input)) {
                NBTTagCompound inputTag = input.getTagCompound();
                NBTTagCompound copy = new NBTTagCompound();

                if (allowAllTags) {
                    copy = (NBTTagCompound) inputTag.copy();
                } else if (allowAllExcept) {
                    for (Object obj : inputTag.func_150296_c()) { // getKeySet()
                        String key = (String) obj;
                        if (!excludedTags.contains(key)) {
                            copy.setTag(key, inputTag.getTag(key));
                        }
                    }
                } else {
                    for (String tag : allowedTags) {
                        if (inputTag.hasKey(tag)) {
                            copy.setTag(tag, inputTag.getTag(tag));
                        }
                    }
                }

                if (!copy.hasNoTags()) {
                    result.setTagCompound(copy);
                }
                break;
            }
        }

        return result;
    }

    private boolean matchesAllowed(ItemStack stack) {
        for (ItemStack allowed : allowedSources) {
            if (allowed.getItem() == stack.getItem()) {
                int meta = allowed.getItemDamage();
                if (meta == OreDictionary.WILDCARD_VALUE || meta == stack.getItemDamage()) {
                    return true;
                }
            }
        }
        return false;
    }
}
