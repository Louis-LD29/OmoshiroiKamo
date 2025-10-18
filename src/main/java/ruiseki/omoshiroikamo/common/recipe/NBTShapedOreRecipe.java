package ruiseki.omoshiroikamo.common.recipe;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class NBTShapedOreRecipe extends ShapedOreRecipe {

    private final Set<ItemStack> allowedSources = new HashSet<>();
    private final Set<String> allowedTags = new HashSet<>();
    private final Set<String> excludedTags = new HashSet<>();

    private boolean allowAllTags = false;
    private boolean allowAllExcept = false;

    private final NBTTagCompound extraResultNBT = new NBTTagCompound();

    public NBTShapedOreRecipe(Block result, Object... recipe) {
        this(new ItemStack(result), recipe);
    }

    public NBTShapedOreRecipe(Item result, Object... recipe) {
        this(new ItemStack(result), recipe);
    }

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

    public NBTShapedOreRecipe withExtraNBT(String key, NBTBase value) {
        if (key != null && value != null) {
            this.extraResultNBT.setTag(key, value);
        }
        return this;
    }

    public NBTShapedOreRecipe withString(String key, String value) {
        this.extraResultNBT.setString(key, value);
        return this;
    }

    public NBTShapedOreRecipe withInt(String key, int value) {
        this.extraResultNBT.setInteger(key, value);
        return this;
    }

    public NBTShapedOreRecipe withBoolean(String key, boolean value) {
        this.extraResultNBT.setBoolean(key, value);
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
                    for (String key : inputTag.func_150296_c()) {
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

                for (String key : this.extraResultNBT.func_150296_c()) {
                    copy.setTag(key, this.extraResultNBT.getTag(key));
                }

                if (!copy.hasNoTags()) {
                    result.setTagCompound(copy);
                }
                break;
            }
        }

        if (!this.extraResultNBT.hasNoTags()) {
            NBTTagCompound tag = result.hasTagCompound() ? result.getTagCompound() : new NBTTagCompound();
            for (String key : this.extraResultNBT.func_150296_c()) {
                tag.setTag(key, this.extraResultNBT.getTag(key));
            }
            result.setTagCompound(tag);
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
