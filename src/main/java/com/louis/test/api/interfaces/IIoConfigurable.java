package com.louis.test.api.interfaces;

import net.minecraftforge.common.util.ForgeDirection;

import com.enderio.core.common.util.BlockCoord;
import com.louis.test.api.enums.IoMode;
import com.louis.test.api.enums.IoType;

public interface IIoConfigurable {

    public IoMode toggleIoModeForFace(ForgeDirection faceHit);

    public IoMode toggleIoModeForFace(ForgeDirection faceHit, IoType type);

    public boolean supportsMode(ForgeDirection faceHit, IoMode mode);

    public void setIoMode(ForgeDirection faceHit, IoMode mode);

    public void setIoMode(ForgeDirection faceHit, IoMode mode, IoType type);

    public IoMode getIoMode(ForgeDirection face);

    public IoMode getIoMode(ForgeDirection face, IoType type);

    public void clearAllIoModes();

    public void clearAllIoModes(IoType type);

    BlockCoord getLocation();
}
