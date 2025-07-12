package com.louis.test.api.io;

import com.enderio.core.common.util.BlockCoord;
import net.minecraftforge.common.util.ForgeDirection;

public interface IIoConfigurable {

    public IoMode toggleIoModeForFace(ForgeDirection faceHit, IoType type);

    public boolean supportsMode(ForgeDirection faceHit, IoMode mode);

    public void setIoMode(ForgeDirection faceHit, IoMode mode, IoType type);

    public IoMode getIoMode(ForgeDirection face, IoType type);

    public void clearAllIoModes(IoType type);

    BlockCoord getLocation();
}
